package com.pratham.rag_document_qa.controller;

import com.pratham.rag_document_qa.dto.AskRequest;
import com.pratham.rag_document_qa.dto.AskResponse;
import com.pratham.rag_document_qa.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public AskResponse askQuestion(@RequestBody AskRequest request) {

        String answer = chatService.askQuestion(request.getQuestion());

        return new AskResponse(answer);
    }
}
