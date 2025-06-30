# 🧠 CodeSense AI – Backend

An intelligent code review assistant that leverages Gemini AI to analyze GitHub pull requests and provide structured, categorized feedback directly as GitHub comments.

This backend handles GitHub webhook events, interacts with Gemini LLM for feedback generation, and manages PR and feedback data storage using PostgreSQL.

---

## 🚀 Features

- 🔁 GitHub Webhook Integration (auto-triggered on pull request events)
- 💬 Gemini API integration to generate review comments
- 🧾 Structured and categorized feedback (e.g., `Readability`, `Security`, `Best Practices`, etc.)
- 🗃️ PostgreSQL database to store PR data and AI responses
- 🛡️ Secure and modular Java Spring Boot codebase
- 🧩 RESTful API layer for frontend dashboard consumption
- 🌐 Deployment-ready (supports platforms like Render, Railway, etc.)

---

## 📐 Architecture Overview

```text
 GitHub PR Event
        ↓
  Webhook Controller
        ↓
 PullRequestService
        ↓
 GeminiService  →  Gemini AI API (LLM)
        ↓
Feedback Structuring
        ↓
 PostgreSQL DB ⟷ Frontend APIs
        ↓
GitHub Comment Service → GitHub PR Feedback
```

## ⚙️ Tech Stack
Backend Framework: Java 17 + Spring Boot

AI Integration: Gemini API (Google AI)

Database: PostgreSQL

Cloud/Hosting: Render

Tools: GitHub Webhooks, Postman, REST API, Git

## 📦 Project Structure

```text
src/
├── controller/        # GitHub webhook and API endpoints
├── service/           # GeminiService, PullRequestService, etc.
├── model/             # Entities: PullRequest, Feedback
├── repository/        # Spring Data JPA Repositories (PostgreSQL)
├── config/            # Application config and constants
└── utils/             # Utility classes, helpers
```

## 🔐 Environment Variables

Create a .env file or configure these as environment variables:
```
GEMINI_API_KEY=your_gemini_api_key
GITHUB_ACCESS_TOKEN=your_github_token
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<database>
SPRING_DATASOURCE_USERNAME=your_db_user
SPRING_DATASOURCE_PASSWORD=your_db_password
```

## 🧪 Running Locally

1. Clone the repository
```
git clone https://github.com/vandan09/codesense-ai-backend.git
cd codesense-ai-backend
```

2. Configure environment variables
```
cp .env.example .env  # or manually create .env file
```

3. Build and run
```
./mvnw spring-boot:run
```

## 🌍 Deployment
Deployed on Render with environment configuration

Webhooks configured to point to /webhook endpoint

PostgreSQL database hosted via Render / Railway / ElephantSQL or any preferred provider

## 📌 Future Enhancements
🔐 Add user authentication and roles

📊 Feedback analytics dashboard (already being developed in frontend)

🧠 Expand to support other LLMs (OpenAI, Claude, etc.)

🧵 Threaded PR feedback support on GitHub
