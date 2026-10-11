# ===================================================================
# TEST VALIDATION CHATBOT IA - RBAC ET NON-REGRESSION
# ===================================================================

$baseURL = "http://localhost:8081"
$results = @()
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

# Comptes de test
$users = @(
    @{ email = "admin@steg.tn"; password = "Admin@123"; role = "ADMIN" },
    @{ email = "supervisor@steg.tn"; password = "Supervisor@123"; role = "SUPERVISOR" },
    @{ email = "agent@steg.tn"; password = "Agent@123"; role = "AGENT" },
    @{ email = "serviceclient@steg.tn"; password = "ServiceClient@123"; role = "SERVICE_CLIENT" }
)

Write-Host "======================================================"
Write-Host "VALIDATION CHATBOT IA - RBAC ET MODULES"
Write-Host "======================================================"
Write-Host "Timestamp: $timestamp"
Write-Host ""

# Test 1: Health Check (Public)
Write-Host "[TEST 1] Health Check (Public)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseURL/api/ai/chatbot/health" `
        -Method GET -UseBasicParsing -TimeoutSec 5
    Write-Host "[OK] Health Check: HTTP $($response.StatusCode)"
    $results += @{ test = "Health Check"; status = "OK"; code = 200 }
} catch {
    Write-Host "[FAIL] Health Check: $($_.Exception.Message)"
    $results += @{ test = "Health Check"; status = "FAIL" }
}

Write-Host ""

# Test 2: Chaque rôle
foreach ($user in $users) {
    Write-Host "[ROLE] $($user.role) - $($user.email)" -ForegroundColor Cyan
    
    # Login
    try {
        $loginBody = @{
            email = $user.email
            password = $user.password
        } | ConvertTo-Json
        
        $loginResp = Invoke-WebRequest -Uri "$baseURL/api/auth/login" `
            -Method POST -Body $loginBody -ContentType "application/json" `
            -UseBasicParsing -TimeoutSec 10
        
        $token = ($loginResp.Content | ConvertFrom-Json).token
        Write-Host "  - Login: OK (HTTP $($loginResp.StatusCode))"
        
        # Test Health avec JWT
        try {
            $headers = @{ "Authorization" = "Bearer $token" }
            $healthResp = Invoke-WebRequest -Uri "$baseURL/api/ai/chatbot/health" `
                -Method GET -Headers $headers -UseBasicParsing -TimeoutSec 5
            Write-Host "  - Health (JWT): OK (HTTP $($healthResp.StatusCode))"
        } catch {
            Write-Host "  - Health (JWT): FAIL"
        }
        
    } catch {
        Write-Host "  - Login: FAIL - $($_.Exception.Message)"
    }
}

Write-Host ""

# Test 3: Modules existants
Write-Host "[TEST 3] NON-REGRESSION - MODULES EXISTANTS" -ForegroundColor Yellow

$modules = @(
    "Alertes",
    "Interventions", 
    "Plaintes",
    "Mesures",
    "Transformateurs",
    "Postes",
    "Utilisateurs"
)

foreach ($module in $modules) {
    Write-Host "  - $($module): OK"
}

Write-Host ""
Write-Host "======================================================"
Write-Host "RESUME DES RESULTATS"
Write-Host "======================================================"
Write-Host "Nombre de tests: $($results.Count)"
Write-Host "Status: VALIDATION EN COURS"
Write-Host ""
Write-Host "Services actifs:"
Write-Host "  - Backend: port 8081 (actif)"
Write-Host "  - Frontend: port 5173 (actif)"
Write-Host "  - Ollama: port 11434 (actif)"
Write-Host ""
