$env:RAZORPAY_KEY = "rzp_test_RpnzkeU0auAn5n"
$env:RAZORPAY_SECRET = "DoLzsqBtOwNNlfUu4BlZ6MOp"

Write-Output "Starting Spring Boot app with Razorpay credentials..."
Write-Output "RAZORPAY_KEY: $env:RAZORPAY_KEY"
Write-Output "RAZORPAY_SECRET: [redacted]"

& java -jar "target\hotel-management-frontend1-1.0-SNAPSHOT.jar"
