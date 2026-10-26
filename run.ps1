Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "Starting Online Examination System in Terminal..." -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Cyan

$mvnPath = "C:\Program Files\NetBeans-25\netbeans\java\maven\bin\mvn.cmd"
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    $mvnPath = "mvn"
}

& $mvnPath compile exec:java
