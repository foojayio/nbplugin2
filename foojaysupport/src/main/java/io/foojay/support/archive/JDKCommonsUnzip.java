package io.foojay.support.archive;

import java.io.File;
import java.io.IOException;
import org.openide.windows.InputOutput;

public class JDKCommonsUnzip extends CommonsUnzip {

    @Override
    public void uncompress(File zip, File targetDir, InputOutput io) throws IOException {
        super.uncompress(zip, targetDir, io);

        //TODO: This is hack to set permissions until I see why Apache Compress does not read them.
        File bin = findBin(targetDir);

        for (File exe : bin.listFiles()) {
            if (exe.isFile())
                exe.setExecutable(true);
        }
    }

    public static File findBin(File outputDir) {
        for (File f : outputDir.listFiles()) {
            if (f.isDirectory() && f.getName().equals("bin"))
                return f;
        }
        for (File f : outputDir.listFiles()) {
            if (f.isDirectory()) {
                File sub = findBin(f);
                if (sub != null)
                    return sub;
            }
        }
        return null;
    }

}
