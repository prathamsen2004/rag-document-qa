# 📄 AI-Powered Document Question Answering System (RAG)

An AI-powered backend application built with **Java Spring Boot** that enables users to upload PDF documents and ask natural language questions about their content using **Retrieval-Augmented Generation (RAG)**.

Instead of sending the entire document to the LLM, the application retrieves only the most relevant chunks using **vector embeddings** and **semantic similarity search**, resulting in more accurate, faster, and context-aware responses.

---

## 🚀 Features

- 📄 Upload PDF documents
- ✂️ Intelligent text chunking
- 🧠 Generate embeddings using Google Gemini
- 🗄️ Store embeddings in PostgreSQL with pgvector
- 🔍 Perform semantic similarity search
- 🤖 Generate context-aware responses using OpenRouter LLM
- ⚡ RESTful APIs built with Spring Boot
- 🏗️ Clean layered architecture

---

# 🏗️ System Architecture

```text
                Upload PDF
                     │
                     ▼
              Apache PDFBox
                     │
                     ▼
              Text Chunking
                     │
                     ▼
        Google Gemini Embeddings
                     │
                     ▼
         PostgreSQL + pgvector
                     │
                     ▼
      Semantic Similarity Search
                     │
                     ▼
        Retrieved Relevant Chunks
                     │
                     ▼
             OpenRouter LLM
                     │
                     ▼
          Context-Aware Answer
```

---

# 🛠️ Tech Stack

| Category | Technologies |
|----------|--------------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Database | PostgreSQL |
| Vector Database | pgvector |
| PDF Processing | Apache PDFBox |
| AI Embeddings | Google Gemini Embeddings API |
| LLM | OpenRouter |
| Build Tool | Maven |
| API Testing | Postman |

---

# 📂 Project Structure

```
src
├── controller
├── service
├── repository
├── entity
├── dto
├── resources
└── application.properties
```

---

# ⚙️ Installation

### Clone Repository

```bash
git clone https://github.com/prathamsen2004/rag-document-qa.git
```

---

### Navigate

```bash
cd rag-document-qa
```

---

### Configure

Create

```
application.properties
```

using

```
application.properties.example
```

Fill in:

- PostgreSQL credentials
- Gemini API Key
- OpenRouter API Key

---

### Run

```bash
mvn spring-boot:run
```

---

# 📡 API Endpoints

## Upload PDF

```
POST /documents/upload
```

---

## Ask Question

```
POST /chat/ask
```

Example

```json
{
  "question": "Summarize the uploaded document."
}
```

---

# 🧠 How RAG Works

1. Upload a PDF document.
2. Extract text using Apache PDFBox.
3. Split text into smaller chunks.
4. Generate vector embeddings using Google Gemini.
5. Store embeddings in PostgreSQL (pgvector).
6. Convert user question into an embedding.
7. Retrieve the most relevant chunks using semantic similarity search.
8. Send retrieved context along with the question to the LLM.
9. Return an accurate, context-aware answer.

---

# 📸 Demo

### Upload PDF & Ask Questions

> *(Add your Postman screenshot here.)*

---

# 🔮 Future Improvements

- Multi-document support
- Chat history
- Authentication & Authorization
- Docker support
- AWS Deployment
- Streaming responses
- Hybrid search (Keyword + Vector)
- Frontend using React

---

# 👨‍💻 Author

**Pratham Sen**

If you found this project useful, consider giving it a ⭐ on GitHub.
