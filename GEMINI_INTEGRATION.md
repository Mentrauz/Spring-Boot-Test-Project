# Gemini Integration - Quick Start Guide

## What Was Done

I've successfully integrated Google Gemini 1.5 Flash AI into your Spring Boot troubleshooting chatbot. Here's what changed:

### 1. New Dependencies Added (`pom.xml`)
- `google-cloud-vertexai` - For Gemini API integration
- `gson` - For JSON processing

### 2. Configuration (`application.properties`)
Your API key has been added:
```properties
gemini.api.key=AIzaSyCfxhu0AA4yKjxktM_RGu4KyDrLGpbSJYM
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent
gemini.model=gemini-1.5-flash
```

### 3. New Service Created (`GeminiService.java`)
- Handles all communication with Gemini API
- Builds intelligent prompts with context
- Parses and returns AI-generated responses
- Includes error handling and logging

### 4. Updated AIService (`AIService.java`)
- Now uses Gemini AI instead of rule-based responses
- Combines knowledge base context with AI intelligence
- Provides more natural, conversational responses

## How to Run

### Step 1: Build the Project
```bash
mvn clean install
```

### Step 2: Start the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Step 3: Test the Chatbot

#### Using PowerShell (Windows):
```powershell
.\test-chatbot.ps1
```

#### Using cURL:
```bash
curl -X POST http://localhost:8080/api/chat/message ^
  -H "Content-Type: application/json" ^
  -d "{\"message\": \"My internet is not working\"}"
```

#### Using PowerShell Invoke-RestMethod:
```powershell
$body = @{
    message = "My internet is not working"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/chat/message" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

## Expected Response Format

```json
{
  "response": "I understand you're having internet connectivity issues. Let me help you troubleshoot this step by step:\n\n1. **Check Your Router**\n   - Verify the router is powered on...",
  "sessionId": "abc-123-def-456",
  "resolved": false
}
```

## Key Features

### 1. Context-Aware Responses
The AI receives context from your knowledge base, making responses more accurate and specific.

### 2. Natural Conversation
Gemini provides human-like, conversational responses instead of rigid templates.

### 3. Multi-Turn Conversations
Each session maintains history, allowing for follow-up questions and clarifications.

### 4. Intelligent Diagnostics
The AI can:
- Analyze complex technical issues
- Provide step-by-step troubleshooting
- Ask clarifying questions when needed
- Adapt responses based on user feedback

## Testing Examples

### Test 1: Internet Issue
```json
{
  "message": "My internet is not working and I cant connect to any websites"
}
```

### Test 2: Application Crash
```json
{
  "message": "My application keeps crashing when I try to open it",
  "sessionId": "test-session-123"
}
```

### Test 3: Performance Problem
```json
{
  "message": "My computer is running very slow"
}
```

### Test 4: Follow-up Question
```json
{
  "message": "I tried restarting but it still doesn't work",
  "sessionId": "test-session-123"
}
```

## Troubleshooting

### If you get "Connection refused":
- Make sure the application is running (`mvn spring-boot:run`)
- Check that port 8080 is not in use
- Wait a few seconds for the application to fully start

### If you get API errors:
- Verify your Gemini API key is correct in `application.properties`
- Check your internet connection
- Ensure the API key has not exceeded rate limits

### To view logs:
Check the console output where you ran `mvn spring-boot:run` for detailed logging.

### To access the database:
Go to `http://localhost:8080/h2-console`:
- JDBC URL: `jdbc:h2:mem:chatbot`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Send Message
```
POST /api/chat/message
Content-Type: application/json

{
  "message": "Your troubleshooting question",
  "sessionId": "optional-session-id"
}
```

### Get Conversation History
```
GET /api/chat/history/{sessionId}
```

## What's Different from Before?

**Before (Rule-based):**
- Simple keyword matching
- Pre-defined template responses
- Limited flexibility
- No natural language understanding

**After (Gemini AI):**
- Advanced natural language processing
- Dynamic, context-aware responses
- Can understand complex queries
- Provides personalized troubleshooting steps
- Learns from knowledge base but isn't limited to it

## Next Steps

1. **Run the application**: `mvn spring-boot:run`
2. **Test with the provided script**: `.\test-chatbot.ps1`
3. **Try your own queries** using cURL or Postman
4. **Add more knowledge base entries** in `DataInitializer.java`
5. **Customize the AI prompt** in `GeminiService.java` for your specific use case

Enjoy your AI-powered troubleshooting chatbot!
