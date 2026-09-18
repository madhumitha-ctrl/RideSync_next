package com.ridesync.backend.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private static final String SYSTEM_INSTRUCTION =
            "You are the RideSync assistant. Help users understand posting rides, searching rides, "
                    + "booking rides, booking history, profiles, and general RideSync usage. "
                    + "Keep answers concise and helpful.";

    private static final Pattern TEXT_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");

    private final String apiKey;
    private final String model;
    private final HttpClient httpClient;

    public AiService(
            @Value("${ridesync.gemini.api-key:}") String apiKey,
            @Value("${ridesync.gemini.model:gemini-2.0-flash}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    public Map<String, Object> chat(String message) {
        Map<String, Object> response = new HashMap<>();
        if (apiKey == null || apiKey.isBlank()) {
            response.put("response", fallback(message));
            response.put("fallback", true);
            return response;
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model
                    + ":generateContent?key="
                    + apiKey;

            String body = """
                    {
                      "system_instruction": {
                        "parts": [{ "text": %s }]
                      },
                      "contents": [{
                        "parts": [{ "text": %s }]
                      }]
                    }
                    """.formatted(jsonString(SYSTEM_INSTRUCTION), jsonString(message));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
                String text = extractFirstText(httpResponse.body());
                if (text != null && !text.isBlank()) {
                    response.put("response", text.trim());
                    response.put("fallback", false);
                    return response;
                }
            }
        } catch (Exception ignored) {
            // Fall through to local fallback so the demo never crashes.
        }

        response.put("response", fallback(message));
        response.put("fallback", true);
        return response;
    }

    private String extractFirstText(String body) {
        Matcher matcher = TEXT_PATTERN.matcher(body);
        if (matcher.find()) {
            return matcher.group(1)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
        }
        return null;
    }

    private String jsonString(String value) {
        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
        return "\"" + escaped + "\"";
    }

    private String fallback(String message) {
        String lower = message == null ? "" : message.toLowerCase(Locale.ROOT);
        if (lower.contains("book")) {
            return "To book a ride: open Search Ride, enter pickup and destination, pick a ride, then click Book Ride. "
                    + "You need to be logged in first.";
        }
        if (lower.contains("post")) {
            return "To post a ride: go to Post Ride, enter pickup, destination, date, time, seats, vehicle type, and fare, "
                    + "then submit. RideSync creates a vehicle record automatically if needed.";
        }
        if (lower.contains("search") || lower.contains("find")) {
            return "Use Search Ride with your pickup and destination. Only scheduled rides with available seats are shown.";
        }
        if (lower.contains("history") || lower.contains("booking")) {
            return "Open Booking History to see your accepted bookings, including route, driver, fare, and status.";
        }
        if (lower.contains("profile") || lower.contains("register") || lower.contains("login")) {
            return "Register with your college email, then Login. Your profile shows name, email, phone, gender, and college ID.";
        }
        return "I'm RideSync AI. Ask me about posting rides, searching rides, booking, booking history, or your profile. "
                + "(Gemini key missing or unavailable — using local help.)";
    }
}
