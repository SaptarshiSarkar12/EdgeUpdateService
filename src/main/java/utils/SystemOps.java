package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemOps {
    // private static String latestPkg;

    private SystemOps() {}

    public static String getCurrentEdgeVersion() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("microsoft-edge", "--version");
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line; 
                if ((line = reader.readLine()) != null) {
                    return extractVersion(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error fetching current Edge version: " + e.getMessage());
        }

        return "0.0.0.0"; // Default if version cannot be fetched
    }

    private static String extractVersion(String versionString) {
        String[] parts = versionString.split(" ");
        if (parts.length > 1) {
            return parts[1];
        }

        return "0.0.0.0";
    }

    public static void downloadEdgePackage(String pkgLink) {
        ProcessBuilder pb = new ProcessBuilder(getCommandPath("wget"), "--progress=bar:force", "--no-check-certificate", pkgLink, "-P", "/tmp/");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Downloaded: " + pkgLink);
            }
        } catch (Exception e) {
            System.out.println("Error while downloading Edge using wget: " + e.getMessage());
        }
    }

    public static void installEdgePackage(String pkgName) {
        latestPkg = pkgName;
        ProcessBuilder pb = new ProcessBuilder(getCommandPath("sudo"), "apt", "install", "/tmp/" + pkgName);
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Installed: " + pkgName);
            }
        } catch (Exception e) {
            System.out.println("Error while installing edge via apt: " + e.getMessage());
        }
    }

    public static void cleanup() {
        ProcessBuilder pb = new ProcessBuilder(getCommandPath("rm"), "/tmp/" + latestPkg);
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Cleanup completed.");
            }
        } catch (Exception e) {
            System.out.println("Error while cleaning up: " + e.getMessage());
        }
    }

    public static void generateServiceFile() {
        String serviceFileContents = """
                [Unit]
                Description=Edge Update Service
                Requires=network-online.target
                After=network-online.target user@.service
                
                [Service]
                ExecStart=/usr/bin/eus
                Restart=on-failure
                
                [Install]
                WantedBy=default.target""";
        File serviceFile = new File("/etc/systemd/system/edge-update.service");
        if (serviceFile.exists()) {
            System.out.println("Service file already exists.");
            return;
        }
        try {
            Files.writeString(serviceFile.toPath(), serviceFileContents);
            System.out.println("Service file generated.");
            System.out.println("Reloading the daemon...");
            reloadDaemon();
            System.out.println("Enabling the service...");
            enableService();
        } catch (Exception e) {
            System.out.println("Error while generating service file: " + e.getMessage());
        }
    }

    private static void reloadDaemon() {
        ProcessBuilder pb = new ProcessBuilder(getCommandPath("sudo"), "systemctl", "daemon-reload");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Daemon reloaded.");
            }
        } catch (Exception e) {
            System.out.println("Error while reloading daemon: " + e.getMessage());
        }
    }

    private static void enableService() {
        ProcessBuilder pb = new ProcessBuilder(getCommandPath("sudo"), "systemctl", "enable", "edge-update.service");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Service enabled.");
            }
        } catch (Exception e) {
            System.out.println("Error while enabling service: " + e.getMessage());
        }
    }

    public static void selfUninstall() {
        ProcessBuilder pb = new ProcessBuilder(getCommandPath("sudo"), "systemctl", "disable", "edge-update.service");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Service disabled.");
            }
        } catch (Exception e) {
            System.out.println("Error while disabling service: " + e.getMessage());
        }
        File serviceFile = new File("/etc/systemd/system/edge-update.service");
        if (serviceFile.exists()) {
            try {
                Files.delete(serviceFile.toPath());
                System.out.println("Service file deleted.");
            } catch (Exception e) {
                System.out.println("Error while deleting service file: " + e.getMessage());
            }
        }
        System.out.println("Self-uninstall completed. Please remove the binary file manually from \"/usr/bin/eus\"");
    }

    private static String getCommandPath(String command) {
        String path = "/usr/bin/" + command; // Common default path on .deb systems
        try {
            ProcessBuilder pb = new ProcessBuilder("/usr/bin/which", command);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String foundPath = reader.readLine();
                if (foundPath != null && !foundPath.isBlank()) {
                    return foundPath;  // Use the detected path if available
                }
            }
        } catch (IOException e) {
            System.out.println("Error while trying to find command location: " + e.getMessage());
        }
        return path; // Fallback to the default path if `which` fails
    }

    public static boolean isCommandAvailable(String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder("/usr/bin/which", command);
            Process process = pb.start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
