package io.foojay.support.archive;

import java.io.File;
import java.io.IOException;
import org.openide.windows.InputOutput;

public class UnarchiveUtils {

    private final static Unarchiver[] zipUnarchivers = new Unarchiver[]{
        new CommandLineUnzip(),
        new CommandLineUntar(),
        new JDKCommonsUnzip()
    };

    private static File getSafeOutputDir(File file) {
        File parent = file.getParentFile();
        String name = file.getName();
        //TODO: Would be nice to also strip .tar.gz which has 2 dots.
        int dot = name.lastIndexOf('.');
        String baseName = dot == -1 ? name : name.substring(0, dot);

        File outputFile = new File(parent, baseName);
        int counter = 1;
        while (outputFile.exists()) {
            outputFile = new File(parent, baseName + "_" + counter);
            counter++;
        }
        //make sure the folder exists, for tar
        outputFile.mkdirs();

        return outputFile;
    }

    public static File unarchive(File file, InputOutput io) throws IOException, InterruptedException {
        File outputDir = getSafeOutputDir(file);
        unarchive(file, outputDir, io);
        return outputDir;
    }

    public static void unarchive(File file, File outputDir, InputOutput io) throws IOException, InterruptedException {
        if (isArchiveFile(file))
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
        throw new UnsupportedOperationException("Could not unarchive " + file);
    }

    public static boolean isArchiveFile(File download) {
        for (Unarchiver u : zipUnarchivers) {
            if (u.isSupported(download))
                    return true;
        }
        return false;
    }

}
