package com.pratham.rag_document_qa.repository;

import com.pratham.rag_document_qa.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
