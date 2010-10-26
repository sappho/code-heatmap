package uk.org.sappho.code.heatmap.engine;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filename {

    private static final Pattern JAVA_REGEX = Pattern
            .compile("^.+?/((com|org|net|edu|gov|mil|biz|info)/.+)/(.+?)\\.java$");
    private final String filename;
    private final Matcher javaMatcher;

    public Filename(String filename) {

        this.filename = filename;
        javaMatcher = JAVA_REGEX.matcher(filename);
    }

    public String getFullFilename() {

        return filename;
    }

    public String getFilename() {

        return new File(filename).getName();
    }

    public String getDirectory() {

        return new File(filename).getParent();
    }

    public boolean isJava() {

        return javaMatcher.matches();
    }

    public String getPackageName() {

        return javaMatcher.group(1).replace('/', '.');
    }

    public String getClassName() {

        return javaMatcher.group(3);
    }
}
