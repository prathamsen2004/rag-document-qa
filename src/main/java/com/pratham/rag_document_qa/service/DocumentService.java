package com.pratham.rag_document_qa.service;

import com.pratham.rag_document_qa.entity.Document;
import com.pratham.rag_document_qa.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    public Document saveDocument(Document document) {
        return repository.save(document);
    }

    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    public Document getDocumentById(Long id) {
        return repository.findById(id).orElse(null);
    }


    public void deleteDocument(Long id) {
        repository.deleteById(id);
    }

    public Document updateDocument(Long id, Document updatedDocument) {

        Document existingDocument = repository.findById(id).orElse(null);

        if (existingDocument != null) {
            existingDocument.setFileName(updatedDocument.getFileName());
            existingDocument.setFileType(updatedDocument.getFileType());

            return repository.save(existingDocument);
        }

        return null;
    }




}
