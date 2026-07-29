package com.pratham.rag_document_qa.service;

import com.pratham.rag_document_qa.dto.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.pratham.rag_document_qa.dto.ChatResponse;
import java.util.List;

@Service
public class EmbeddingService {

    @Value("${google.ai.api-key}")
    private String apiKey;

    @Value("${openrouter.api.key}")
    private String openRouterApiKey;

    private final RestClient restClient;
    private final JdbcTemplate jdbcTemplate;

    public EmbeddingService(JdbcTemplate jdbcTemplate) {
        this.restClient = RestClient.builder().build();
        this.jdbcTemplate = jdbcTemplate;
    }

    public float[] generateEmbedding(String text) {

        EmbeddingResponse response = restClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .body("""
                {
                  "model": "models/gemini-embedding-001",
                  "content": {
                    "parts": [
                      {
                        "text": "%s"
                      }
                    ]
                  }
                }
                """.formatted(text))
                .retrieve()
                .body(EmbeddingResponse.class);

        if (response == null || response.getEmbedding() == null) {
            throw new RuntimeException("Failed to generate embedding.");
        }

        List<Float> values = response.getEmbedding().getValues();

        float[] embedding = new float[values.size()];

        for (int i = 0; i < values.size(); i++) {
            embedding[i] = values.get(i);
        }

        return embedding;
    }

    public void saveEmbedding(Long chunkId, float[] embedding) {

        StringBuilder vector = new StringBuilder("[");

        for (int i = 0; i < embedding.length; i++) {

            vector.append(embedding[i]);

            if (i < embedding.length - 1) {
                vector.append(",");
            }
        }

        vector.append("]");

        jdbcTemplate.update(
                "UPDATE document_chunks SET embedding = ?::vector WHERE id = ?",
                vector.toString(),
                chunkId
        );
    }

    public String generateAnswer(String context, String question) {

        String prompt = """
            You are a helpful AI assistant.

            Answer ONLY from the provided context.

            If the answer is not present in the context, reply:
            "I couldn't find that information in the uploaded document."

            Context:
            %s

            Question:
            %s
            """.formatted(context, question);

        String requestBody = """
{
  "model": "inclusionai/ling-3.0-flash:free",
  "messages": [
    {
      "role": "user",
      "content": %s
    }
  ]
}
""".formatted("\"" + prompt.replace("\"", "\\\"") + "\"");

        ChatResponse response = restClient.post()
                .uri("https://openrouter.ai/api/v1/chat/completions")
                .header("Authorization", "Bearer " + openRouterApiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(ChatResponse.class);

        if (response == null
                || response.getChoices() == null
                || response.getChoices().isEmpty()) {
            throw new RuntimeException("Failed to generate answer.");
        }

        return response.getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}