package main;

import utils.CheckUpdate;

public class EdgeUpdateService {
    public static void main(String[] args) {
        System.out.println("Edge Update Service v1.0.0");
        if (args.length != 0) {
            for (String arg : args) {
                if (arg.equals("--generate-service") || arg.equals("-gs")) {
                    utils.SystemOps.generateServiceFile();
                } else if (arg.equals("--uninstall-eus") || arg.equals("-u")) {
                    utils.SystemOps.selfUninstall();
                } else {
                    System.out.println("Invalid argument: " + arg);
                }
            }
        } else {
            updateEdge();
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
}
