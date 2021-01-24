package io.foojay.support.archive;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOColorLines;
import org.openide.windows.InputOutput;

public class CommandLineUnzip implements Unarchiver {

    private boolean unzipExists() {
        try {
            Process probe = new ProcessBuilder().command("unzip")
                    .start();
            if (!probe.waitFor(10, TimeUnit.SECONDS) || probe.exitValue() != 0) {
                probe.destroyForcibly();

                return false;
            }
        } catch (IOException | InterruptedException ioe) {
            return false;
        }
        return true;
    }

    @Override
    public void uncompress(File zipFile, File targetDir, InputOutput io) throws InterruptedException, IOException {
        if (!unzipExists())
            throw new UnsupportedOperationException("unzip not available");
        ProcessBuilder pb = new ProcessBuilder();
        Process unzip = pb.command(
                "unzip",
                "-o", //overwrite output, we expect the output dir to be empty
                zipFile.getAbsolutePath(),
                "-d",
                targetDir.getAbsolutePath())
                .start();
        io.getOut().println("Running : " + pb.command().toString());
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream cliIO = unzip.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(cliIO));
                    String s;
                    while ((s = br.readLine()) != null) {
                        io.getOut().println(s);
                    }
                } catch (IOException ex) {
                    io.getOut().append("Exception ").append(ex.toString()).println();
                }
            }
        });
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream cliIO = unzip.getErrorStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(cliIO));
                    String s;
                    while ((s = br.readLine()) != null) {
                        if (IOColorLines.isSupported(io))
                            IOColorLines.println(io, s, Color.RED);
                        else
                            io.getOut().println(s);
                    }
                } catch (IOException ex) {
                    io.getOut().append("Exception ").append(ex.toString()).println();
                }
            }
        });
        int exitCode = unzip.waitFor();
        io.getOut().println("Exit code: " + exitCode);
        if (0 != exitCode)
            throw new IOException("unzip failed");
    }

}
