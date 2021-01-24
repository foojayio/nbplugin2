package io.foojay.support.archive;

import java.io.File;
import java.io.IOException;
import org.openide.windows.InputOutput;

public class UnarchiveUtils {

    private final static Unarchiver[] zipUnarchivers = new Unarchiver[]{
        new CommandLineUnzip(),
        new JDKCommonsUnzip()
    };

    private static File getSafeOutputDir(File file) {
        File parent = file.getParentFile();
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        String baseName = dot == -1 ? name : name.substring(0, dot);

        File outputFile = new File(parent, baseName);
        int counter = 1;
        while (outputFile.exists()) {
            outputFile = new File(parent, baseName + "_" + counter);
            counter++;
        }

        return outputFile;
    }

    public static File unarchive(File file, InputOutput io) throws IOException, InterruptedException {
        File outputDir = getSafeOutputDir(file);
        unarchive(file, outputDir, io);
        return outputDir;
    }

    public static void unarchive(File file, File outputDir, InputOutput io) throws IOException, InterruptedException {
        if (file.getName().toLowerCase().endsWith(".zip"))
            unarchive(file, outputDir, zipUnarchivers, io);
        else
            throw new UnsupportedOperationException("Unknown archive");
    }

    private static void unarchive(File file, File outputDir, Unarchiver[] zipUnarchivers, InputOutput io) throws IOException, InterruptedException {
        for (Unarchiver u : zipUnarchivers) {
            try {
                u.uncompress(file, outputDir, io);
                return;
            } catch (UnsupportedOperationException uoe) {
                //ignore, the archiver didn't like something, try the next one
            }
        }
    }

}
