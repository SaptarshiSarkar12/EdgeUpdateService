package utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class FetchPackages {
    private static final String channel = "beta";
    private static final String pkgLink = "https://packages.microsoft.com/repos/edge/pool/main/m/microsoft-edge-" + channel + "/";

    private FetchPackages() {}

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
