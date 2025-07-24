package com.cloudhumans.smartchat.controller.docs;

import com.cloudhumans.smartchat.dto.conversation.ConversationRequest;
import com.cloudhumans.smartchat.dto.conversation.ConversationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Conversation API",
        description = "Endpoints for AI-powered conversation handling"
)
public interface ConversationControllerDoc {

    @Operation(
            summary = "Get AI conversation completion",
            description = "Processes user messages and returns AI-generated responses with contextual awareness",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Basic Query",
                                            value = """
                            {
                              "helpdeskId": 12345,
                              "projectName": "tesla_motors",
                              "messages": [
                                {"role": "user", "content": "Hello! How long does a Tesla battery last before it needs to be replaced?"}
                              ]
                            }
                            """
                                    )
                            },
                            schema = @Schema(implementation = ConversationRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful AI response",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ConversationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Invalid project reference",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = """
                            {
                              "status": "UNPROCESSABLE_ENTITY",
                              "message": "Project not found",
                              "timestamp": "2024-07-15T14:30:45"
                            }
                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request format"
                    )
            }
    )
    ConversationResponse createCompletion(@RequestBody @Valid ConversationRequest request);
}
