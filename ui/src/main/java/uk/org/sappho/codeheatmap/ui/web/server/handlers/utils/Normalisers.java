package uk.org.sappho.codeheatmap.ui.web.server.handlers.utils;

public class Normalisers {

    public static String normaliseFilename(String filename) {
        String[] split = filename.split("/");
        return split[split.length - 2] + "/" + split[split.length - 1];
    }

    public static String extractPackage(String filename) {
        String[] split = filename.split("/");
        int indexOfCom = findCom(split);
        String result = "";
        for (int i = indexOfCom; i < split.length - 1; i++) {
            result += split[i] + ".";
        }
        if (result.length() == 0) {
            return "no data found";
        }
        return result.substring(0, result.length() - 1);
    }

    private static int findCom(String[] split) {
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("com")) {
                return i;
            }
        }
        return split.length - 1;
    }

}
