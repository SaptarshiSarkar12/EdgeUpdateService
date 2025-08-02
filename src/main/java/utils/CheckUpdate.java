package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CheckUpdate {
    private static final String version = utils.SystemOps.getCurrentEdgeVersion();
    private static final List<String> packages = utils.FetchPackages.getPackages();
    private static String channel = detectChannel(); // Detect and set the channel

    public CheckUpdate() {
        System.out.println("Check Update");
        System.out.println("Current Edge Version: " + version);
        System.out.println("Available Edge Packages: " + packages);
    }

    public static String getLatestPkgName() {
        String latestPkg = version; // Default value
        int index = -1; // Default value
        for (String pkg : Objects.requireNonNull(packages)) {
            String v = pkg.split("_")[1].split("-")[0];
            int cmp = compareVersions(v, Objects.requireNonNull(latestPkg));
            if (cmp > 0) {
                latestPkg = v;
                index = packages.indexOf(pkg);
            }
        }
        if (index == -1) {
            return null;
        }
        return packages.get(index);
    }

    public static int compareVersions(String v1, String v2) {
        int[] version1 = Arrays.stream(v1.split("\\."))
                .mapToInt(CheckUpdate::parseInteger)
                .toArray();
        int[] version2 = Arrays.stream(v2.split("\\."))
                .mapToInt(CheckUpdate::parseInteger)
                .toArray();

        for (int i = 0; i < Math.max(version1.length, version2.length); i++) {
            int num1 = i < version1.length ? version1[i] : 0;
            int num2 = i < version2.length ? version2[i] : 0;
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }

    private static int parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Error while parsing integer: " + e.getMessage());
            return 0;
        }
    }

    public static String detectChannel() {
        String detectedChannel = "stable"; // Default to stable

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("microsoft-edge", "--version");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferReader(new InputStreamReader(process.getInputStream())));
            String line; 
            while ((line = reader.readLine()) != null) {
                if (line.contains("beta")) {
                    detectedChannel = "beta";
                    break;
                } else if (line.contains("dev")) {
                    detectedChannel = "dev";
                    break;
                } else if (line.contains("canary")) {
                    detectedChannel = "canary";
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error detecting channel: " + e.getMessage());
            // Fallback check for insider executables
            detectedChannel = checkInsiderExecutables();
        }

        return detectedChannel;
    }

    private static String checkInsiderExecutables() {
        List<String> insiders = List.of("microsoft-edge-beta", "microsoft-edge-dev", "microsoft-edge-canary");
        for (String exe : insiders) {
            try {
                ProcessBuilder pb = new ProcessBuilder(exe, "--version");
                Process process = pb.start();
                if (process.waitFor() == 0) {
                    if (exe.contains("beta")) return "beta";
                    if (exe.contains("dev")) return "dev";
                    if (exe.contains("canary")) return "canary";
                }
            } catch (IOException | InterruptedException ignored) {
            }

            return "stable";
        }
    }
}
