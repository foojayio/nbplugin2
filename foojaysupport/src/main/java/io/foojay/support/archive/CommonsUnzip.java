package io.foojay.support.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.openide.windows.InputOutput;

public class CommonsUnzip implements Unarchiver {

    @Override
    public void uncompress(File zip, File targetDir, InputOutput io) throws IOException {
        try ( ZipArchiveInputStream i = new ZipArchiveInputStream(new FileInputStream(zip))) {
            ZipArchiveEntry entry = null;
            while ((entry = i.getNextZipEntry()) != null) {
                if (!i.canReadEntryData(entry))
                    // log something?
                    continue;
                File f = new File(targetDir, entry.getName()).getAbsoluteFile();
                if (!isAncestor(targetDir, f)) //bad entry?

                    continue;
                if (entry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs())
                        throw new IOException("Could not create dirs" + f);
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs())
                        throw new IOException("Could not create dirs" + parent);
                    try ( OutputStream o = Files.newOutputStream(f.toPath())) {
                        IOUtils.copy(i, o);
                    }
                    if (entry.getUnixMode() != 0)
                        //System.out.println("Entry " + entry.getName() + " has mode " + entry.getUnixMode());
                        if ((entry.getUnixMode() & 1) != 0)
                            f.setExecutable(true);

                }
            }
        }
    }

    private static boolean isAncestor(File targetDir, File child) {
        for (; child.getParentFile() != null; child = child.getParentFile()) {
            if (child.getParentFile().equals(targetDir))
                return true;
        }
        return false;
    }
}
