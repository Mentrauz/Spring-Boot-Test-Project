# PowerShell Test Script for Gemini-integrated chatbot

Write-Host "Testing AI Troubleshooting Chatbot with Gemini Integration" -ForegroundColor Green
Write-Host "===========================================================" -ForegroundColor Green
Write-Host ""

# Wait for the application to start
Write-Host "Waiting for application to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Test 1: Internet issue
Write-Host "Test 1: Internet connectivity issue" -ForegroundColor Cyan
Write-Host "------------------------------------" -ForegroundColor Cyan
$body1 = @{
    message = "My internet is not working and I cant connect to any websites"
} | ConvertTo-Json

$response1 = Invoke-RestMethod -Uri "http://localhost:8080/api/chat/message" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body1

$response1 | ConvertTo-Json -Depth 10
Write-Host ""
Write-Host ""

# Test 2: Application crash
Write-Host "Test 2: Application crash issue" -ForegroundColor Cyan
Write-Host "--------------------------------" -ForegroundColor Cyan
$body2 = @{
    message = "My application keeps crashing when I try to open it"
} | ConvertTo-Json

$response2 = Invoke-RestMethod -Uri "http://localhost:8080/api/chat/message" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body2

$response2 | ConvertTo-Json -Depth 10
Write-Host ""
Write-Host ""

# Test 3: Performance issue
Write-Host "Test 3: Performance issue" -ForegroundColor Cyan
Write-Host "-------------------------" -ForegroundColor Cyan
$body3 = @{
    message = "My computer is running very slow and programs take forever to load"
} | ConvertTo-Json

$response3 = Invoke-RestMethod -Uri "http://localhost:8080/api/chat/message" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body3

$response3 | ConvertTo-Json -Depth 10
Write-Host ""
Write-Host "Tests completed!" -ForegroundColor Green
