package io.foojay.support;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.netbeans.api.java.platform.JavaPlatform;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

public class J2SEPlatformUtils {

    @NonNull
    public static JavaPlatform register(@NonNull File folder, @NonNull String name) throws IOException {
        // When the API becomes public we can just do
        // return J2SEPlatformCreator.createJ2SEPlatform(FileUtil.toFileObject(folder), name);
        // util then, using reflection since an impl dependency is only pain.
        FileObject fo = FileUtil.toFileObject(folder);

        ClassLoader classLoader = Lookup.getDefault().lookup(ClassLoader.class);
        try {
            Class<?> j2SEPlatformCreatorClass = Class.forName("org.netbeans.modules.java.j2seplatform.api.J2SEPlatformCreator", true, classLoader); //NOI18N
            Method createJ2SEPlatformMethod = j2SEPlatformCreatorClass.getDeclaredMethod("createJ2SEPlatform", FileObject.class, String.class); //NOI18N
            Object platform = createJ2SEPlatformMethod.invoke(null, fo, name);
            if (platform instanceof JavaPlatform)
                return (JavaPlatform) platform;

            throw new IllegalStateException("Could not register J2SE platform");
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new IOException("Could not register J2SE platform", ex);
        }
    }

}
