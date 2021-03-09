package io.foojay.support;

import io.foojay.api.discoclient.pkg.Architecture;
import io.foojay.api.discoclient.pkg.OperatingSystem;
import org.openide.util.Utilities;

public class OS {

    public static OperatingSystem getOperatingSystem() {
        if (Utilities.isMac())
            return OperatingSystem.MACOS;
        if (Utilities.isWindows())
            return OperatingSystem.WINDOWS;
        if (Utilities.isUnix()) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("sunos"))
                return OperatingSystem.SOLARIS;
            else
                return OperatingSystem.LINUX;
        }
        return OperatingSystem.NONE;
    }

    public static Architecture getArchitecture() {
        Architecture arch = Architecture.fromText(System.getProperty("os.arch"));
        if (arch == Architecture.NOT_FOUND)
            return Architecture.NONE;
        return arch;
    }

}
