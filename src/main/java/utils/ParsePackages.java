package utils;

import java.util.ArrayList;
import java.util.List;

public class ParsePackages {
    private ParsePackages() {}

    public static List<String> getPackages(String response) {
        List<String> packages = new ArrayList<>();
        String[] lines = response.split("\n");
        for (String line : lines) {
            if (line.contains("microsoft-edge-beta") && line.contains("amd64.deb")) {
                String[] parts = line.split(">");
                String[] parts2 = parts[1].split("<");
                packages.add(parts2[0]);
            }
        }
        return packages;
    }
}
