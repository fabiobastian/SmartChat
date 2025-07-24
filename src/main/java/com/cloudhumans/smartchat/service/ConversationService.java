package com.cloudhumans.smartchat.service;

import com.cloudhumans.smartchat.dto.chat.ChatCompletionRequest;
import com.cloudhumans.smartchat.dto.chat.ChatCompletionResponse;
import com.cloudhumans.smartchat.dto.chat.Choice;
import com.cloudhumans.smartchat.dto.chat.MessageDTO;
import com.cloudhumans.smartchat.dto.conversation.ConversationRequest;
import com.cloudhumans.smartchat.dto.conversation.ConversationResponse;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingRequest;
import com.cloudhumans.smartchat.dto.embedding.EmbeddingResponse;
import com.cloudhumans.smartchat.dto.search.SearchResponse;
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

    private static final String ERROR_PROJECT_NOT_FOUND = "Project not found";
    private static final String MODEL_EMBEDDING= "text-embedding-3-large";
    private static final String MODEL_GPT= "gpt-4o";
    private static final double TEMPERATURE_GPT = 0.6;
    private static final int MAX_TOKENS_GPT = 500;

    private final EmbeddingService embeddingService;
    private final SearchService searchService;
    private final ChatService chatService;

    private final ProjectRepository projectRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final SectionRetrievedRepository sectionRetrievedRepository;

    public ConversationResponse generateCompletion(ConversationRequest request) {

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

        SearchResponse searchResponse = searchService.searchByEmbedding(
                embeddingResponse.data().getFirst().embedding(),
                project.getName()
        );

        if (searchResponse != null && searchResponse.odataCount() > 0) {

            ConversationResponse escalationResponse = handleN2Escalation(searchResponse, message);
            if (escalationResponse != null) {
                conversation.setHandoverToHumanNeeded(true);
                conversationRepository.save(conversation);
                return escalationResponse;
            }

            List<SectionRetrieved> sectionRetrieveds = searchResponse.value().stream()
                    .map(value -> new SectionRetrieved(message, value.searchScore(), value.content()))
                    .toList();
            sectionRetrievedRepository.saveAll(sectionRetrieveds);

            String questionsContext = sectionRetrieveds.stream()
                    .map(s -> String.format("- %s", s.getContent()))
                    .collect(Collectors.joining("\n"));

            return getConversationResponse(questionsContext, message, searchResponse, project.getDisplayName());
        }

        return new ConversationResponse(true, getMessagesN2Escalation(message), List.of());
    }

    private ConversationResponse handleN2Escalation(SearchResponse searchResponse, Message message) {
        boolean handover = searchResponse.value().stream()
                .anyMatch(resultItem -> "N2".equalsIgnoreCase(resultItem.type()));

        if (handover) {
            List<MessageDTO> messages = getMessagesN2Escalation(message);
            return new ConversationResponse(true, messages, searchResponse.value());
        }
        return null;
    }

    private List<MessageDTO> getMessagesN2Escalation(Message message) {
        Message chatMessage = new Message();
        chatMessage.setConversation(message.getConversation());
        chatMessage.setRole(Role.ASSISTANT);
        chatMessage.setContent("I'm sorry I couldn't resolve this for you! 😊 Let me connect you with one of our " +
                "human specialists who can give this the attention it deserves. Please hold while I transfer you!");
        messageRepository.save(chatMessage);

        return List.of(
                new MessageDTO(Role.USER, message.getContent()),
                new MessageDTO(Role.ASSISTANT, chatMessage.getContent())
        );
    }

    private ConversationResponse getConversationResponse(String context, Message message, SearchResponse searchResponse, String brandDisplayName) {
        ChatCompletionResponse chatCompletionResponse = chatService
                .generateResponse(getChatCompletionRequest(context, message, brandDisplayName));

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

    private static ChatCompletionRequest getChatCompletionRequest(String context, Message message, String brandDisplayName) {
        String systemPrompt = """
        You are Claudia, the official virtual support assistant for %s. Your role is to provide accurate, friendly, and concise answers based on the company's documentation. Follow these rules strictly:
        
        1. **Persona**:
           - Always introduce yourself: "I'm Claudia, your %s support assistant 😊".
           - Use a warm but professional tone.
        
        2. **Response Format**:
           - If user greets, respond with a greeting.
           - End with: "Let me know if you need anything else!" + brand-related emoji.
        
        **Context**:
        %s
        """.formatted(
                brandDisplayName,
                brandDisplayName,
                context
        );

        List<MessageDTO> messages = List.of(
                new MessageDTO(Role.SYSTEM, systemPrompt),
                new MessageDTO(Role.USER, message.getContent())
        );

        return new ChatCompletionRequest(MODEL_GPT, TEMPERATURE_GPT, MAX_TOKENS_GPT, messages);
    }
}
