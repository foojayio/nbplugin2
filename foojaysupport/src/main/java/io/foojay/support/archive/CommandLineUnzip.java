package io.foojay.support.archive;

import java.io.File;

public class CommandLineUnzip extends AbstractCommandLineUnarchiver {

    @Override
    protected String[] getProbeCommand() {
        return new String[]{"unzip"};
    }

    @Override
    protected String getName() {
        return "unzip";
    }

    @Override
    protected String[] getCommand(File zipFile, File targetDir) {
        return new String[]{
            "unzip",
            "-o", //overwrite output, we expect the output dir to be empty
            zipFile.getAbsolutePath(),
            "-d",
            targetDir.getAbsolutePath()
        };
    }

    @Override
    public boolean isSupported(File input) {
        //TODO: We could just send the ArchiveType as argument
        return input.getName().toLowerCase().endsWith(".zip");
    }

}
