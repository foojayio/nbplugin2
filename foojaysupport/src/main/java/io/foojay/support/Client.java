package io.foojay.support;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.event.Evt;
import io.foojay.api.discoclient.event.EvtObserver;
import io.foojay.api.discoclient.event.EvtType;
import io.foojay.api.discoclient.pkg.Architecture;
import io.foojay.api.discoclient.pkg.ArchiveType;
import io.foojay.api.discoclient.pkg.Bitness;
import io.foojay.api.discoclient.pkg.Distribution;
import io.foojay.api.discoclient.pkg.Latest;
import io.foojay.api.discoclient.pkg.LibCType;
import io.foojay.api.discoclient.pkg.MajorVersion;
import io.foojay.api.discoclient.pkg.OperatingSystem;
import io.foojay.api.discoclient.pkg.PackageType;
import io.foojay.api.discoclient.pkg.Pkg;
import io.foojay.api.discoclient.pkg.ReleaseStatus;
import io.foojay.api.discoclient.pkg.Scope;
import io.foojay.api.discoclient.pkg.SemVer;
import io.foojay.api.discoclient.pkg.TermOfSupport;
import io.foojay.api.discoclient.pkg.VersionNumber;
import io.foojay.api.discoclient.util.PkgInfo;
import java.util.List;
import java.util.concurrent.Future;

public class Client {

    private final static Client INSTANCE = new Client();

    private DiscoClient client = null;

    public static Client getInstance() {
        return INSTANCE;
    }

    private synchronized DiscoClient getDisco() {
        if (client == null)
            client = new DiscoClient();
        return client;
    }

    private Client() {
    }

    public synchronized MajorVersion getLatestLts(boolean b) {
        return getDisco().getLatestLts(b);
    }

    public synchronized MajorVersion getLatestSts(boolean b) {
        return getDisco().getLatestSts(b);
    }

    public synchronized List<Pkg> getPkgs(final Distribution distribution, final VersionNumber versionNumber, final Latest latest, final OperatingSystem operatingSystem,
            final Architecture architecture, final ArchiveType archiveType, final PackageType packageType,
            final Boolean javafxBundled) {
        return getDisco().getPkgs(distribution, versionNumber, latest, operatingSystem, LibCType.NONE, architecture, Bitness.NONE, archiveType, packageType, javafxBundled, /*directlyDownloadable*/ true, ReleaseStatus.NONE, TermOfSupport.NONE, Scope.PUBLIC);
    }

    public synchronized PkgInfo getPkgInfo(String ephemeralId, SemVer javaVersion) {
        return getDisco().getPkgInfo(ephemeralId, javaVersion);
    }

    public synchronized Future<?> downloadPkg(PkgInfo pkgInfo, String absolutePath) {
        return getDisco().downloadPkg(pkgInfo, absolutePath);
    }

    public synchronized void setOnEvt(final EvtType<? extends Evt> type, final EvtObserver observer) {
        //XXX: in theory this could be delayed until disco is instantiated...
        getDisco().setOnEvt(type, observer);
    }

}
