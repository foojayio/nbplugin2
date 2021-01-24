package io.foojay.support.archive;

import java.io.File;
import java.io.IOException;
import org.openide.windows.InputOutput;

public interface Unarchiver {

    public void uncompress(File zip, File targetDir, InputOutput io) throws IOException, InterruptedException;

}
