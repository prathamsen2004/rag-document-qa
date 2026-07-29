package com.pratham.rag_document_qa.service;

import com.pratham.rag_document_qa.entity.DocumentChunk;
import com.pratham.rag_document_qa.repository.DocumentChunkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentChunkService {

    @Autowired
    private DocumentChunkRepository repository;

    public DocumentChunk saveChunk(DocumentChunk chunk) {
        return repository.save(chunk);
    }
}