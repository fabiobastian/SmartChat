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

import static com.cloudhumans.smartchat.util.TextUtils.removeGreetings;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RequiredArgsConstructor
@Service
public class ConversationService {

    public static final String ERROR_PROJECT_NOT_FOUND = "Project not found";
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
                UNPROCESSABLE_ENTITY, ERROR_PROJECT_NOT_FOUND
        ));

        Conversation conversation = conversationRepository
                .findConversationByProjectAndHelpdeskId(project, request.helpdeskId()).orElseGet(() -> {
                    Conversation newConv = new Conversation();
                    newConv.setProject(project);
                    newConv.setHelpdeskId(request.helpdeskId());
                    return conversationRepository.save(newConv);
                });

        String fullMessage = request.messages().stream()
                .filter(MessageDTO -> Role.USER == MessageDTO.role())
                .map(MessageDTO::content)
                .collect(Collectors.joining("\n"));

        String cleanText = removeGreetings(fullMessage);

        Message message = new Message();
        message.setConversation(conversation);
        message.setRole(Role.USER);
        message.setContent(fullMessage);
        messageRepository.save(message);

        EmbeddingRequest embeddingRequest =
                new EmbeddingRequest(MODEL_EMBEDDING, cleanText);
        EmbeddingResponse embeddingResponse = embeddingService.generateEmbedding(embeddingRequest);

        List<VectorQuery> vectorQueries = new ArrayList<>();
        vectorQueries.add(new VectorQuery(embeddingResponse.data().getFirst().embedding(), 3, "embeddings", "vector"));
        String filter = String.format("projectName eq '%s'", project.getName());

        SearchRequest searchRequest =
                new SearchRequest(true, "content, type", 10, filter, vectorQueries);
        SearchResponse searchResponse = searchService.searchRelevantQuestions(searchRequest);

        if (searchResponse != null && searchResponse.odataCount() > 0) {

            boolean handover = searchResponse.value().stream()
                    .anyMatch(resultItem -> "N2".equalsIgnoreCase(resultItem.type()));

            if (handover) {
                Message chatMessage = new Message();
                chatMessage.setConversation(message.getConversation());
                chatMessage.setRole(Role.ASSISTANT);
                chatMessage.setContent("I'm sorry, this issue should be handled by a human assistant.");
                messageRepository.save(chatMessage);

                List<MessageDTO> messages = List.of(
                        new MessageDTO(Role.ASSISTANT, message.getContent()),
                        new MessageDTO(Role.ASSISTANT, chatMessage.getContent())
                );
                return new ConversationResponse(true, messages, searchResponse.value());
            }

            List<SectionRetrieved> sectionRetrieveds = searchResponse.value().stream()
                    .map(value -> new SectionRetrieved(message, value.searchScore(), value.content()))
                    .toList();

            sectionRetrievedRepository.saveAll(sectionRetrieveds);

            String context = sectionRetrieveds.stream()
                    .map(s -> String.format("- %s", s.getContent()))
                    .collect(Collectors.joining("\n"));

            return getConversationResponse(context, message, searchResponse);
        }
        // TODO Pensar o que a aplicação deve fazer se o banco não retornar correspondencias

        return null;
    }

    private ConversationResponse getConversationResponse(String context, Message message, SearchResponse searchResponse) {
        ChatCompletionResponse chatCompletionResponse = chatService
                .getChatAnswer(getChatCompletionRequest(context, message));

        Message chatMessage = new Message();
        chatMessage.setConversation(message.getConversation());
        chatMessage.setRole(Role.ASSISTANT);
        chatMessage.setContent(chatCompletionResponse.choices().stream()
                .map(choice -> choice.message().content())
                .collect(Collectors.joining("\n")));
        messageRepository.save(chatMessage);

        List<MessageDTO> messages = new ArrayList<>();
        messages.add(new MessageDTO(message.getRole(), message.getContent()));
        messages.addAll(chatCompletionResponse.choices().stream()
                        .map(Choice::message)
                        .toList()
        );

        return new ConversationResponse(false, messages, searchResponse.value());
    }

    private static ChatCompletionRequest getChatCompletionRequest(String context, Message message) {
        String systemPrompt = """
        You are Claudia, Tesla's official support assistant. Your role is to provide accurate, friendly, and concise answers based on Tesla's documentation. Follow these rules strictly:
        
        1. **Persona**:
           - Always introduce yourself: "I'm Claudia, your Tesla support assistant 😊".
           - Use a warm but professional tone.
        
        2. **Response Format**:
           - If user greets, respond with a greeting.
           - End with: "Let me know if you need anything else!" + brand-related emoji.
        
        **Context**:
        %s
        """.formatted(context);

        List<MessageDTO> messages = List.of(
                new MessageDTO(Role.SYSTEM, systemPrompt),
                new MessageDTO(Role.USER, message.getContent())
        );

        return new ChatCompletionRequest(MODEL_GPT, 0.5, 400, messages);
    }
}
