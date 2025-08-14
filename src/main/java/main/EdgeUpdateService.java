package main;

import utils.CheckUpdate;
import utils.SystemOps;

import java.util.ArrayList;
import java.util.List;

public class EdgeUpdateService {
    public final static String VERSION = "1.0.0";

    public static void main(String[] args) {
        System.out.println("Edge Update Service v" + VERSION);
        checkInitDeps();
        if (args.length != 0) {
            for (String arg : args) {
                switch (arg) {
                    case "--generate-service", "-gs" -> SystemOps.generateServiceFile();
                    case "--uninstall-eus", "-u" -> SystemOps.selfUninstall();
                    case "--help", "-h" -> printHelp();
                    case "--version", "-v" -> System.out.println(VERSION);
                    default -> {
                        System.out.println("Invalid argument: " + arg);
                        printHelp();
                    }
                }
            }
        } else {
            // Detect installed edge channel (stable, beta, dev, canary)
            String detectedChannel = CheckUpdate.detectInstalledChannel();
            utils.FetchPackages.setChannel(detectedChannel);

            updateEdge();
        }
    }

    private static void checkInitDeps() {
        ArrayList<String> requiredDeps = new ArrayList<>(List.of(
            "microsoft-edge-beta", "wget", "apt", "dpkg", "systemctl", "rm", "sudo"
        ));
        requiredDeps.removeIf(SystemOps::isCommandAvailable);
        if (!requiredDeps.isEmpty()) {
            System.out.print("Critical dependencies missing: Install ");
            if (requiredDeps.size() == 1) {
                System.out.println(requiredDeps.getFirst() + " before running the service.");
            } else {
                System.out.println(String.join(", ", requiredDeps.subList(0, requiredDeps.size() - 1)) + " and " + requiredDeps.getLast() + " before running the service.");
            }
        }
    }

    public static void updateEdge() {
        String latestPkg = CheckUpdate.getLatestPkgName();
        if (latestPkg == null) {
            System.out.println("No Edge update available.");
        } else {
            System.out.println("Latest Package: " + latestPkg);
            System.out.println("New Edge update is available.");
            System.out.println("Downloading the latest package...");
            utils.SystemOps.downloadEdgePackage("https://packages.microsoft.com/repos/edge/pool/main/m/microsoft-edge-beta/" + latestPkg);
            System.out.println("Installing the latest package...\nPlease close all Edge windows.");
            utils.SystemOps.installEdgePackage(latestPkg);
            System.out.println("Cleaning up...");
            utils.SystemOps.cleanup();
            System.out.println("Update completed.");
        }
    }

    public static void printHelp() {
        System.out.println("Usage: eus [--generate-service | -gs] [--uninstall-eus | -u] [--help | -h] [--version | -v]");
        System.out.println("  --generate-service, -gs  Generate Edge Update Service file.");
        System.out.println("  --uninstall-eus, -u      Uninstall Edge Update Service (EUS).");
        System.out.println("  --help, -h               Show this help message.");
        System.out.println("  --version, -v            Show version information.");
        System.out.println("  No arguments             Check for Edge updates and install if available.");
        System.out.println("For more information, visit: https://github.com/SaptarshiSarkar12/EdgeUpdateService");
    }
}
