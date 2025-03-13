package main;

import utils.CheckUpdate;

public class EdgeUpdateService {
    public static void main(String[] args) {
        System.out.println("Edge Update Service v1.0.0");
        if (args.length != 0) {
            for (String arg : args) {
                if (arg.equals("--generate-service") || arg.equals("-gs")) {
                    utils.SystemOps.generateServiceFile();
                }
            }
        } else {
            updateEdge();
        }

    }

    public static void updateEdge() {
        String latestPkg = CheckUpdate.getLatestPkgName();
        if (latestPkg == null) {
            notifyUser("No Edge update available.");
        } else {
            System.out.println("Latest Package: " + latestPkg);
            notifyUser("New Edge update is available.\nDownloading the latest package...");
            System.out.println("Downloading the latest package...");
            utils.SystemOps.downloadEdgePackage("https://packages.microsoft.com/repos/edge/pool/main/m/microsoft-edge-beta/" + latestPkg);
            notifyUser("New Edge update is available.\nInstalling the latest package...\nPlease close all Edge windows.");
            System.out.println("Installing the latest package...");
            utils.SystemOps.installEdgePackage(latestPkg);
            System.out.println("Cleaning up...");
            utils.SystemOps.cleanup();
            System.out.println("Update completed.");
            notifyUser("Update completed.");
        }
    }

    public static void notifyUser(String text) {
        ProcessBuilder pb = new ProcessBuilder("zenity", "--info", "--text=" + text);
        pb.inheritIO();
        try {
            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
