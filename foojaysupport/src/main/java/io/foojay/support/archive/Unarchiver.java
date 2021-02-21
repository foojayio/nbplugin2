package io.foojay.support.archive;

import java.io.File;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openide.windows.InputOutput;

public interface Unarchiver {

    public boolean isSupported(@NonNull File input);

    public void uncompress(@NonNull File zip, @NonNull File targetDir, InputOutput io) throws IOException, InterruptedException;

}
