#!/bin/bash

# Test script for Gemini-integrated chatbot

echo "Testing AI Troubleshooting Chatbot with Gemini Integration"
echo "==========================================================="
echo ""

# Wait for the application to start
echo "Waiting for application to start..."
sleep 5

# Test 1: Internet issue
echo "Test 1: Internet connectivity issue"
echo "------------------------------------"
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"message": "My internet is not working and I cant connect to any websites"}' \
  | python -m json.tool

echo ""
echo ""

# Test 2: Application crash
echo "Test 2: Application crash issue"
echo "--------------------------------"
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"message": "My application keeps crashing when I try to open it"}' \
  | python -m json.tool

echo ""
echo ""

# Test 3: Performance issue
echo "Test 3: Performance issue"
echo "-------------------------"
curl -X POST http://localhost:8080/api/chat/message \
  -H "Content-Type: application/json" \
  -d '{"message": "My computer is running very slow and programs take forever to load"}' \
  | python -m json.tool

echo ""
echo "Tests completed!"
