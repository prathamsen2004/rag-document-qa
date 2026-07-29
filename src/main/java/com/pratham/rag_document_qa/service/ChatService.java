package com.pratham.rag_document_qa.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final EmbeddingService embeddingService;
    private final SimilaritySearchService similaritySearchService;

    public ChatService(EmbeddingService embeddingService,
                       SimilaritySearchService similaritySearchService) {

        this.embeddingService = embeddingService;
        this.similaritySearchService = similaritySearchService;
    }

    public String askQuestion(String question) {

        // Generate embedding for the user's question
        float[] questionEmbedding = embeddingService.generateEmbedding(question);

        // Find top 5 similar chunks
        List<String> chunks = similaritySearchService.findRelevantChunks(questionEmbedding);

        // Print retrieved chunks for debugging
        System.out.println("\n========== Retrieved Chunks ==========");
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("\nChunk " + (i + 1) + ":");
            System.out.println(chunks.get(i));
        }

        // Combine all chunks into one context
        String context = String.join("\n\n", chunks);

        // Print final context sent to Gemini
        System.out.println("\n========== Final Context ==========");
        System.out.println(context);

        // Generate answer using Gemini
        return embeddingService.generateAnswer(context, question);
    }
}