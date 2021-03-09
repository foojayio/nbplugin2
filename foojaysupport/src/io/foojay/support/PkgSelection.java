package io.foojay.support;

import io.foojay.api.discoclient.pkg.Pkg;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PkgSelection {

    public static @NonNull PkgSelection of(@NonNull Pkg pkg) {
        return new PkgSelection() {
            @Override
            public Pkg get(@Nullable Client d) {
                return pkg;
            }

            @Override
            public String getJavaPlatformDisplayName() {
                return pkg.getDistribution().getUiString() + " " + pkg.getJavaVersion().toString();
            }

            @Override
            public String getFileName() {
                return pkg.getFileName();
            }
        };
    }

    public @Nullable Pkg get(@Nullable Client d);

    public @NonNull String getFileName();

    public @NonNull String getJavaPlatformDisplayName();

}
