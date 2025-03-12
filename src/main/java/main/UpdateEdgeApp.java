package main;

import utils.CheckUpdate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateEdgeApp {
    public static void main(String[] args) {
        System.out.println("Update Edge App");
        String latestPkg = CheckUpdate.getLatestPkgName();
        if (latestPkg == null) {
            System.out.println("No update available.");
        } else {
            System.out.println("Latest Package: " + latestPkg);
            System.out.println("Downloading the latest package...");
            utils.SystemOps.downloadEdgePackage("https://packages.microsoft.com/repos/edge/pool/main/m/microsoft-edge-beta/" + latestPkg);
            System.out.println("Installing the latest package...");
            utils.SystemOps.installEdgePackage(latestPkg);
            System.out.println("Cleaning up...");
            utils.SystemOps.cleanup();
            System.out.println("Update completed.");
        }
    }
}
