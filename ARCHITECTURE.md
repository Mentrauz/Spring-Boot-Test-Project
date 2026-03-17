# Architecture Overview

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT                                   │
│  (REST API Client - cURL, Postman, Frontend App)                │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            │ HTTP POST /api/chat/message
                            │ { message, sessionId }
                            │
┌───────────────────────────▼─────────────────────────────────────┐
│                    ChatController                                │
│  • Receives HTTP requests                                        │
│  • Validates input                                               │
│  • Returns ChatResponse                                          │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            │ processMessage()
                            │
┌───────────────────────────▼─────────────────────────────────────┐
│                      ChatService                                 │
│  • Creates/manages sessionId                                     │
│  • Saves user message to DB                                      │
│  • Gets conversation history                                     │
│  • Calls AI service                                              │
│  • Saves AI response to DB                                       │
└──────────┬────────────────────────────────┬─────────────────────┘
           │                                │
           │ generateResponse()             │ save()
           │                                │
┌──────────▼────────────────┐   ┌───────────▼──────────────────┐
│      AIService             │   │  ChatMessageRepository       │
│  • Extract keywords        │   │  (H2 Database)               │
│  • Search knowledge base   │   │  • Stores chat history       │
│  • Build context           │   │  • Tracks sessions           │
│  • Call Gemini AI          │   └──────────────────────────────┘
└──────────┬────────────────┘
           │
           │ findByKeyword()
           │
┌──────────▼───────────────────────────────────────────────────────┐
│            TroubleshootingKnowledgeRepository                     │
│  (H2 Database)                                                    │
│  • Pre-loaded troubleshooting solutions                           │
│  • Categories: Network, Application, Performance, Audio           │
│  • Provides context for AI responses                              │
└──────────┬───────────────────────────────────────────────────────┘
           │
           │ Context data
           │
┌──────────▼───────────────────────────────────────────────────────┐
│                      GeminiService                                │
│  • Builds intelligent prompt with context                         │
│  • Calls Google Gemini API                                        │
│  • Parses AI response                                             │
│  • Error handling                                                 │
└──────────┬───────────────────────────────────────────────────────┘
           │
           │ HTTPS POST
           │
┌──────────▼───────────────────────────────────────────────────────┐
│             Google Gemini 1.5 Flash API                           │
│  https://generativelanguage.googleapis.com/v1beta/models/...     │
│  • Natural language processing                                    │
│  • Context-aware response generation                              │
│  • Troubleshooting logic                                          │
└───────────────────────────────────────────────────────────────────┘
```

## Data Flow Example

### Example: "My internet is not working"

```
1. User Request
   POST /api/chat/message
   {
     "message": "My internet is not working",
     "sessionId": null
   }

2. ChatController receives request
   ↓
3. ChatService processes:
   • Generates sessionId: "abc-123-def"
   • Saves user message to DB
   • Retrieves conversation history (empty for new session)
   
4. AIService processes:
   • Extracts keywords: ["internet", "not", "working"]
   • Searches knowledge base → Finds "Internet not working" entry
   • Builds context:
     "Relevant solutions from knowledge base:
      Solution 1 - Internet not working:
      1. Check if your router is powered on
      2. Restart your router by unplugging it for 30 seconds
      3. Check if other devices can connect
      4. Reset network settings on your device"

5. GeminiService:
   • Creates prompt:
     "You are a helpful technical troubleshooting assistant...
      Context from knowledge base: [above context]
      User's issue: My internet is not working
      Please provide: 1. Diagnosis 2. Steps 3. Tips"
   
   • Sends to Gemini API
   • Receives AI response:
     "I understand you're experiencing internet connectivity issues.
      Let me help you troubleshoot this step by step:
      
      1. Check Your Router
         • Verify the power light is on...
      2. Test Your Connection
         • Try connecting another device...
      3. Reset Network Settings
         • On Windows: Go to Settings > Network..."

6. ChatService:
   • Saves AI response to DB
   • Returns ChatResponse

7. User receives:
   {
     "response": "I understand you're experiencing...",
     "sessionId": "abc-123-def",
     "resolved": false
   }
```

## Component Responsibilities

### Controller Layer
- **ChatController**: HTTP endpoint handling, validation, response formatting

### Service Layer
- **ChatService**: Business logic orchestration, session management
- **AIService**: Knowledge base search, context building, AI coordination
- **GeminiService**: Direct API communication with Gemini

### Repository Layer
- **ChatMessageRepository**: Conversation persistence
- **TroubleshootingKnowledgeRepository**: Knowledge base queries

### Model Layer
- **ChatMessage**: Conversation entity (id, sessionId, role, content, timestamp)
- **TroubleshootingKnowledge**: Solution entity (id, category, problem, solution, keywords)
- **ChatRequest**: DTO for incoming requests
- **ChatResponse**: DTO for outgoing responses

## Technology Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Framework | Spring Boot 3.2.0 | Application framework |
| Web | Spring Web | REST API |
| Persistence | Spring Data JPA | Database operations |
| Database | H2 (in-memory) | Development database |
| AI | Google Gemini 1.5 Flash | Natural language AI |
| HTTP Client | Java HttpClient | API communication |
| JSON | Gson | JSON parsing |
| Utilities | Lombok | Boilerplate reduction |
| Build | Maven | Dependency management |

## Configuration Files

```
application.properties
├── Server Configuration (port: 8080)
├── Database Configuration (H2 in-memory)
├── JPA Configuration (Hibernate)
├── H2 Console (enabled for dev)
├── Logging (DEBUG level)
└── Gemini API (key, URL, model)
```

## Database Schema

### chat_messages
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| session_id | VARCHAR | Session identifier |
| role | VARCHAR | USER or ASSISTANT |
| content | TEXT | Message content |
| timestamp | TIMESTAMP | Creation time |

### troubleshooting_knowledge
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key |
| category | VARCHAR | Problem category |
| problem | VARCHAR | Problem description |
| solution | TEXT | Solution steps |
| keywords | TEXT | Search keywords |

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/chat/message | Send message, get AI response |
| GET | /api/chat/history/{sessionId} | Get conversation history |
| GET | /h2-console | Access database console |

## Security Considerations

1. **API Key**: Currently in properties file (move to env vars for production)
2. **CORS**: Enabled for all origins (restrict in production)
3. **Input Validation**: Using Jakarta validation annotations
4. **Rate Limiting**: Not implemented (add for production)
5. **Authentication**: Not implemented (add for production)

## Scalability Considerations

### Current Setup (Development)
- In-memory H2 database
- Single instance
- No caching
- Synchronous processing

### Production Recommendations
- PostgreSQL/MySQL for persistence
- Redis for session caching
- Load balancer for multiple instances
- Async processing for long-running AI calls
- Message queue for high traffic
- CDN for static assets (if frontend added)

## Extension Points

1. **Add more AI providers**: OpenAI, Claude, etc.
2. **Frontend**: React/Vue.js chat interface
3. **Authentication**: JWT, OAuth2
4. **Analytics**: Track resolution rates, common issues
5. **Feedback loop**: User ratings to improve responses
6. **File upload**: Screenshot analysis for visual issues
7. **Voice input**: Speech-to-text integration
8. **Multi-language**: i18n support
