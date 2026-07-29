package com.pratham.rag_document_qa.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimilaritySearchService {

    private final JdbcTemplate jdbcTemplate;

    public SimilaritySearchService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findRelevantChunks(float[] questionEmbedding) {

        // Convert float[] -> pgvector string
        StringBuilder vector = new StringBuilder("[");

        for (int i = 0; i < questionEmbedding.length; i++) {

            vector.append(questionEmbedding[i]);

            if (i < questionEmbedding.length - 1) {
                vector.append(",");
            }
        }

        vector.append("]");

        String sql = """
                SELECT chunk_text
                FROM document_chunks
                ORDER BY embedding <=> CAST(? AS vector)
                LIMIT 5
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("chunk_text"),
                vector.toString()
        );
    }
}
