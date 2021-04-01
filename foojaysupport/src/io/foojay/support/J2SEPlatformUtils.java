package io.foojay.support;

import java.io.File;
import java.io.IOException;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.java.platform.JavaPlatform;
import org.netbeans.modules.java.j2seplatform.api.J2SEPlatformCreator;
import org.openide.filesystems.FileUtil;

public class J2SEPlatformUtils {

    @NonNull
    public static JavaPlatform register(@NonNull File folder, @NonNull String name) throws IOException {
        return J2SEPlatformCreator.createJ2SEPlatform(FileUtil.toFileObject(folder), name);
    }

}
