package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.dto.*;
import com.cloudhumans.smartchat.entity.*;
import com.cloudhumans.smartchat.repository.ConversationRepository;
import com.cloudhumans.smartchat.repository.MessageRepository;
import com.cloudhumans.smartchat.repository.ProjectRepository;
import com.cloudhumans.smartchat.repository.SectionRetrievedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RequiredArgsConstructor
@Service
public class ConversationService {

    public static final String MODEL_EMBEDDING= "text-embedding-3-large";
    public static final String MODEL_GPT= "gpt-4o";

    private final EmbeddingService embeddingService;
    private final SearchService searchService;
    private final ChatService chatService;

    private final ProjectRepository projectRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SectionRetrievedRepository sectionRetrievedRepository;

    public ConversationResponse getChatComplemention(ConversationRequest request) {

        Project project = projectRepository.findProjectByName(request.projectName())
                .orElseThrow(() -> new ResponseStatusException(
                UNPROCESSABLE_ENTITY, "Project not found"
        ));

        Conversation conversation = conversationRepository
                .findConversationByProjectAndHelpdeskId(project, request.helpdeskId()).orElseGet(() -> {
                    Conversation newConv = new Conversation();
                    newConv.setProject(project);
                    newConv.setHelpdeskId(request.helpdeskId());
                    return conversationRepository.save(newConv);
                });

        Message message = new Message();
        message.setConversation(conversation);
        message.setContent(request.messages().get(0).content()); // TODO se tiver mais mensagens ?
        message.setRole(request.messages().get(0).role()); // TODO se tiver mais mensagens ?
        messageRepository.save(message);

        EmbeddingRequest embeddingRequest =
                new EmbeddingRequest(MODEL_EMBEDDING, request.messages().get(0).content()); // TODO se tiver mais mensagens ?
        EmbeddingResponse embeddingResponse = embeddingService.generateEmbedding(embeddingRequest);

        List<VectorQuery> vectorQueries = new ArrayList<>();
        vectorQueries.add(new VectorQuery(embeddingResponse.data().get(0).embedding(), 3, "embeddings", "vector"));
        String filter = String.format("projectName eq '%s'", project.getName());

        SearchRequest searchRequest =
                new SearchRequest(true, "content, type", 10, filter, vectorQueries);
        SearchResponse searchResponse = searchService.searchRelevantQuestions(searchRequest);
        // TODO ver se retornou algo e mudar o resultado na message retrieved_section=true

        if (searchResponse != null && searchResponse.odataCount() > 0) {
            List<SectionRetrieved> sectionRetrieveds = searchResponse.value().stream()
                    .map(value -> new SectionRetrieved(message, value.searchScore(), value.content()))
                    .collect(Collectors.toList());

            sectionRetrievedRepository.saveAll(sectionRetrieveds);

            String context = sectionRetrieveds.stream()
                    .map(s -> String.format("- %s", s.getContent()))
                    .collect(Collectors.joining("\n"));

            List<MessageDTO> messageDTO = new ArrayList<>();
            messageDTO.add(new MessageDTO(Role.SYSTEM,
                    "You are a Tesla technical support assistant. Use the following information to provide the most accurate and helpful answers to the user's questions:\n\n" + context));

            messageDTO.add(new MessageDTO(Role.USER, message.getContent()));

            ChatCompletionRequest chatCompletionRequest =
                    new ChatCompletionRequest(MODEL_GPT, 0.5, 400, messageDTO);
            ChatCompletionResponse chatCompletionResponse = chatService.getChatAnswer(chatCompletionRequest);

            return new ConversationResponse(false, searchResponse.value(), chatCompletionResponse.choices().get(0).message());
        }
        // TODO Pensar o que a aplicação deve fazer se o banco não retornar correspondencias

        return null;
    }
}
