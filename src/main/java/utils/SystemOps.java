package utils;

import java.io.File;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemOps {
    private static String latestPkg;

    private SystemOps() {}

    public static String getCurrentEdgeVersion() {
        ProcessBuilder pb = new ProcessBuilder("microsoft-edge", "--version");
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String v = new String(process.getInputStream().readAllBytes()).trim();
                String regex = "Edge\\s(\\d+\\.\\d+\\.\\d+\\.\\d+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(v);

                if (matcher.find()) {
                    return matcher.group(1);
                } else {
                    System.out.println("No version found in the input string.");
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static void downloadEdgePackage(String pkgLink) {
        ProcessBuilder pb = new ProcessBuilder("wget", "--progress=bar:force", "--no-check-certificate", pkgLink, "-P", "/tmp/");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Downloaded: " + pkgLink);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void installEdgePackage(String pkgName) {
        latestPkg = pkgName;
        ProcessBuilder pb = new ProcessBuilder("sudo", "apt", "install", "/tmp/" + pkgName);
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Installed: " + pkgName);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void cleanup() {
        ProcessBuilder pb = new ProcessBuilder("/bin/rm", "/tmp/" + latestPkg);
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Cleanup completed.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void reloadDaemon() {
        ProcessBuilder pb = new ProcessBuilder("sudo", "systemctl", "daemon-reload");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Daemon reloaded.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void enableService() {
        ProcessBuilder pb = new ProcessBuilder("sudo", "systemctl", "enable", "edge-update.service");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Service enabled.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void selfUninstall() {
        ProcessBuilder pb = new ProcessBuilder("sudo", "systemctl", "disable", "edge-update.service");
        pb.inheritIO();
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Service disabled.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        File serviceFile = new File("/etc/systemd/system/edge-update.service");
        if (serviceFile.exists()) {
            try {
                Files.delete(serviceFile.toPath());
                System.out.println("Service file deleted.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Self-uninstall completed. Please remove the binary file manually from \"/usr/bin/eus\"");
    }
}
