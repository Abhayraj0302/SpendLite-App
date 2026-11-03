package com.example.spendlite.core.utils

object AuthConstants {
    const val GOOGLE_WEB_CLIENT_ID =
        "484456075825-eicbp8p2um4n5b7t6pvcpo1k8h1k3qbv.apps.googleusercontent.com"

    // Use 10.0.2.2 for standard Android Emulator to connect to host machine's localhost (Spring Boot/MongoDB server on port 8080).
    // For physical devices or custom IP setups, replace 10.0.2.2 with your development host machine's local IP (e.g. http://192.168.x.x:8080/).
    const val BASE_URL = "http://10.148.97.27:8080/"
}