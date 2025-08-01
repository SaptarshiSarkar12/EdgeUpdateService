package utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class FetchPackages {
    private static String channel = "beta"; // Default if not overridden

    private FetchPackages() {}

    // Set detected channel dynamically
    public static void setChannel(String newChannel) {
        if (List.of("stable", "beta", "dev", "canary").contains(newChannel)) {
            channel = newChannel;
        } else {
            System.out.println("Invalid channel. Using default: beta.");
        }
    }

    private static String getResponse() {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(pkgLink))
                    .GET()
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static List<String> getPackages() {
        String response = getResponse();
        if (response == null) {
            return null;
        }
        return ParsePackages.getPackages(response);
    }
}
