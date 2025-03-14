package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CheckUpdate {
    private static final String version = utils.SystemOps.getCurrentEdgeVersion();
    private static final List<String> packages = utils.FetchPackages.getPackages();

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
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] version2 = Arrays.stream(v2.split("\\."))
                .mapToInt(Integer::parseInt)
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
}
