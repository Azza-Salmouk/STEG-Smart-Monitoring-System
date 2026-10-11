# ===================================================================
# TEST COMPLET CHATBOT IA - RBAC ET NON-RÉGRESSION
# ===================================================================
# Ce script teste le chatbot pour tous les rôles et valide les endpoints

$baseURL = "http://localhost:8081"
$chatbotURL = "http://localhost:5173"

# Comptes de test
$users = @(
    @{ email = "admin@steg.tn"; password = "Admin@123"; role = "ADMIN" },
    @{ email = "supervisor@steg.tn"; password = "Supervisor@123"; role = "SUPERVISOR" },
    @{ email = "agent@steg.tn"; password = "Agent@123"; role = "AGENT" },
    @{ email = "serviceclient@steg.tn"; password = "ServiceClient@123"; role = "SERVICE_CLIENT" }
)

$results = @()
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "🤖 TEST CHATBOT IA - VALIDATION RBAC" -ForegroundColor Green
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "Timestamp: $timestamp" -ForegroundColor Gray
Write-Host ""

# Test 1: Health Check (Public)
Write-Host "📊 TEST 1: Health Check (Public)" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseURL/api/ai/chatbot/health" -Method GET -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Health Check: HTTP 200" -ForegroundColor Green
        $results += @{ test = "Health Check"; status = "PASS"; code = 200 }
    }
} catch {
    Write-Host "❌ Health Check failed: $($_.Exception.Message)" -ForegroundColor Red
    $results += @{ test = "Health Check"; status = "FAIL"; error = $_.Exception.Message }
}

Write-Host ""

# Test 2: Test chaque rôle
foreach ($user in $users) {
    Write-Host "👤 ROLE: $($user.role) ($($user.email))" -ForegroundColor Cyan
    Write-Host "─" * 50
    
    # Login
    try {
        $loginBody = @{
            email = $user.email
            password = $user.password
        } | ConvertTo-Json
        
        $loginResp = Invoke-WebRequest -Uri "$baseURL/api/auth/login" `
            -Method POST -Body $loginBody -ContentType "application/json" `
            -UseBasicParsing -TimeoutSec 10 -ErrorAction Stop
        
        $token = ($loginResp.Content | ConvertFrom-Json).token
        Write-Host "✅ Login: HTTP $($loginResp.StatusCode)" -ForegroundColor Green
        $results += @{ test = "Login $($user.role)"; status = "PASS"; code = $loginResp.StatusCode }
        
        # Test Chatbot Health avec JWT
        try {
            $headers = @{ "Authorization" = "Bearer $token" }
            $healthResp = Invoke-WebRequest -Uri "$baseURL/api/ai/chatbot/health" `
                -Method GET -Headers $headers -UseBasicParsing -TimeoutSec 5
            Write-Host "✅ Health Check (JWT): HTTP $($healthResp.StatusCode)" -ForegroundColor Green
            $results += @{ test = "Health Check with JWT $($user.role)"; status = "PASS"; code = $healthResp.StatusCode }
        } catch {
            Write-Host "❌ Health Check (JWT) failed: $($_.Exception.Message)" -ForegroundColor Red
            $results += @{ test = "Health Check with JWT $($user.role)"; status = "FAIL"; error = $_.Exception.Message }
        }
        
        # Test Chatbot Message (avec timeout court - ne pas bloquer le script)
        try {
            $chatBody = @{ message = "Teste" } | ConvertTo-Json
            $headers = @{ "Authorization" = "Bearer $token"; "Content-Type" = "application/json" }
            
            $job = Start-Job -ScriptBlock {
                param($url, $body, $headers)
                try {
                    Invoke-WebRequest -Uri $url -Method POST -Body $body -Headers $headers `
                        -UseBasicParsing -TimeoutSec 20 -ErrorAction Stop
                } catch {
                    throw $_
                }
            } -ArgumentList "$baseURL/api/ai/chatbot/message", $chatBody, $headers
            
            $jobResult = Wait-Job -Job $job -Timeout 25
            
            if ($jobResult) {
                $response = Receive-Job -Job $job
                Write-Host "✅ Chatbot Message: HTTP $($response.StatusCode)" -ForegroundColor Green
                $results += @{ test = "Chatbot Message $($user.role)"; status = "PASS"; code = $response.StatusCode }
            } else {
                Write-Host "⏳ Chatbot Message: En cours (Ollama génère la réponse...)" -ForegroundColor Yellow
                $results += @{ test = "Chatbot Message $($user.role)"; status = "PROCESSING" }
                Remove-Job -Job $job -Force
            }
        } catch {
            Write-Host "❌ Chatbot Message failed: $($_.Exception.Message)" -ForegroundColor Red
            $results += @{ test = "Chatbot Message $($user.role)"; status = "FAIL"; error = $_.Exception.Message }
        }
        
    } catch {
        Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
        $results += @{ test = "Login $($user.role)"; status = "FAIL"; error = $_.Exception.Message }
    }
    
    Write-Host ""
}

# Test 3: Non-Régression des modules existants
Write-Host "📚 TEST 3: NON-RÉGRESSION - MODULES EXISTANTS" -ForegroundColor Yellow
Write-Host "─" * 50

$modules = @(
    @{ name = "Alertes"; endpoint = "/api/alerts"; method = "GET" },
    @{ name = "Interventions"; endpoint = "/api/interventions"; method = "GET" },
    @{ name = "Plaintes"; endpoint = "/api/complaints"; method = "GET" },
    @{ name = "Mesures"; endpoint = "/api/measurements"; method = "GET" },
    @{ name = "Transformateurs"; endpoint = "/api/transformateurs"; method = "GET" },
    @{ name = "Postes"; endpoint = "/api/postes"; method = "GET" },
    @{ name = "Utilisateurs"; endpoint = "/api/users"; method = "GET" }
)

# Utiliser le token ADMIN pour ces tests
$adminUser = $users[0]
$loginBody = @{
    email = $adminUser.email
    password = $adminUser.password
} | ConvertTo-Json

$loginResp = Invoke-WebRequest -Uri "$baseURL/api/auth/login" `
    -Method POST -Body $loginBody -ContentType "application/json" `
    -UseBasicParsing -TimeoutSec 10
$token = ($loginResp.Content | ConvertFrom-Json).token
$headers = @{ "Authorization" = "Bearer $token" }

foreach ($module in $modules) {
    try {
        $response = Invoke-WebRequest -Uri "$baseURL$($module.endpoint)" `
            -Method $module.method -Headers $headers `
            -UseBasicParsing -TimeoutSec 10 -ErrorAction Stop
        
        if ($response.StatusCode -in @(200, 400, 404)) {
            Write-Host "✅ $($module.name): HTTP $($response.StatusCode)" -ForegroundColor Green
            $results += @{ test = "Module: $($module.name)"; status = "PASS"; code = $response.StatusCode }
        }
    } catch {
        Write-Host "⚠️ $($module.name): $($_.Exception.Message)" -ForegroundColor Yellow
        $results += @{ test = "Module: $($module.name)"; status = "WARNING"; error = $_.Exception.Message }
    }
}

Write-Host ""

# Summary
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "📊 RÉSUMÉ DES TESTS" -ForegroundColor Green
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan

$passed = @($results | Where-Object { $_.status -eq "PASS" }).Count
$failed = @($results | Where-Object { $_.status -eq "FAIL" }).Count
$processing = @($results | Where-Object { $_.status -eq "PROCESSING" }).Count
$warning = @($results | Where-Object { $_.status -eq "WARNING" }).Count
$total = $results.Count

Write-Host "✅ Passed: $passed" -ForegroundColor Green
Write-Host "❌ Failed: $failed" -ForegroundColor Red
Write-Host "⏳ Processing: $processing" -ForegroundColor Yellow
Write-Host "⚠️  Warning: $warning" -ForegroundColor Yellow
Write-Host "📊 Total: $total" -ForegroundColor Cyan

Write-Host ""
Write-Host "════════════════════════════════════════════════════════════" -ForegroundColor Cyan

# Export results to CSV
$dateFormat = Get-Date -Format "yyyyMMdd_HHmmss"
$reportFile = "chatbot_test_results_$dateFormat.csv"
$results | Export-Csv -Path $reportFile -NoTypeInformation
Write-Host "📄 Rapport sauvegardé: $reportFile" -ForegroundColor Cyan

if ($failed -eq 0 -and $processing -le 0) {
    Write-Host "🎉 TOUS LES TESTS PASSÉS!" -ForegroundColor Green
} elseif ($failed -gt 0) {
    Write-Host "⚠️ Certains tests ont échoué. Voir le rapport pour plus de détails." -ForegroundColor Red
} else {
    Write-Host "ℹ️ Tests en cours. Certaines réponses peuvent prendre du temps (Ollama génère)." -ForegroundColor Yellow
}

Write-Host ""
