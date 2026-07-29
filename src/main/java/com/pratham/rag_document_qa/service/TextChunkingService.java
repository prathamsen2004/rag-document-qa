package com.pratham.rag_document_qa.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkingService {

    public List<String> chunkText(String text) {

        List<String> chunks = new ArrayList<>();

        int chunkSize = 1000;

        for (int i = 0; i < text.length(); i += chunkSize) {

            int end = Math.min(i + chunkSize, text.length());

            chunks.add(text.substring(i, end));
        }

        return chunks;
    }
}