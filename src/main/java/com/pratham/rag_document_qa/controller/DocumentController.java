package com.pratham.rag_document_qa.controller;

import com.pratham.rag_document_qa.entity.Document;
import com.pratham.rag_document_qa.entity.DocumentChunk;
import com.pratham.rag_document_qa.service.DocumentChunkService;
import com.pratham.rag_document_qa.service.DocumentService;
import com.pratham.rag_document_qa.service.EmbeddingService;
import com.pratham.rag_document_qa.service.TextChunkingService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @Autowired
    private DocumentChunkService chunkService;

    @Autowired
    private TextChunkingService textChunkingService;

    @Autowired
    private EmbeddingService embeddingService;

    @PostMapping
    public Document saveDocument(@RequestBody Document document) {
        return service.saveDocument(document);
    }

    @GetMapping
    public List<Document> getAllDocuments() {
        return service.getAllDocuments();
    }

    @GetMapping("/{id}")
    public Document getDocumentById(@PathVariable Long id) {
        return service.getDocumentById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDocument(@PathVariable Long id) {
        service.deleteDocument(id);
        return "Document deleted successfully";
    }

    @PutMapping("/{id}")
    public Document updateDocument(@PathVariable Long id,
                                   @RequestBody Document document) {
        return service.updateDocument(id, document);
    }

    @PostMapping("/upload")
    public String uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {

        // Create uploads folder
        File uploadDirectory = new File(System.getProperty("user.dir"), "uploads");

        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Save uploaded PDF
        File destinationFile = new File(uploadDirectory, file.getOriginalFilename());
        file.transferTo(destinationFile);

        // Extract PDF text
        PDDocument pdfDocument = Loader.loadPDF(destinationFile);

        PDFTextStripper pdfStripper = new PDFTextStripper();
        String extractedText = pdfStripper.getText(pdfDocument);

        pdfDocument.close();

        // Save document details
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setUploadTime(LocalDateTime.now());

        document = service.saveDocument(document);

        // Chunk text
        List<String> chunks = textChunkingService.chunkText(extractedText);

        System.out.println("Total Chunks : " + chunks.size());

        // Save each chunk and its embedding
        for (int i = 0; i < chunks.size(); i++) {

            DocumentChunk chunk = new DocumentChunk();
            chunk.setChunkText(chunks.get(i));
            chunk.setChunkIndex(i + 1);
            chunk.setDocument(document);

            // Save chunk
            DocumentChunk savedChunk = chunkService.saveChunk(chunk);

            // Generate embedding
            float[] embedding = embeddingService.generateEmbedding(savedChunk.getChunkText());

            // Save embedding in PostgreSQL vector column
            embeddingService.saveEmbedding(savedChunk.getId(), embedding);

            System.out.println("Chunk " + (i + 1) + " saved with embedding.");
        }

        return "File uploaded successfully : " + file.getOriginalFilename();
    }
}