package io.foojay.support.archive;

import java.io.File;

public class CommandLineUntar extends AbstractCommandLineUnarchiver {

    @Override
    protected String[] getProbeCommand() {
        return new String[]{"tar", "--version"};
    }

    @Override
    protected String getName() {
        return "tar";
    }

    @Override
    protected String[] getCommand(File zipFile, File targetDir) {
        return new String[]{
            "tar",
            isGzip(zipFile) ? "xvfz" : "xvf",
            zipFile.getAbsolutePath(),
            "-C",
            targetDir.getAbsolutePath()
        };
    }

    @Override
    public boolean isSupported(File input) {
        //TODO: We could just send the ArchiveType as argument
        String name = input.getName().toLowerCase();
        return name.endsWith(".tar") || name.endsWith(".tar.gz") || name.endsWith(".tgz");
    }

    private boolean isGzip(File input) {
        String name = input.getName().toLowerCase();
        return name.endsWith(".tar.gz") || name.endsWith(".tgz");
    }
}
