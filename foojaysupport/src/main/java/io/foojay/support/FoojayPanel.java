/*
 * Copyright (c) 2020 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.foojay.support;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.pkg.Architecture;
import io.foojay.api.discoclient.pkg.Bitness;
import io.foojay.api.discoclient.pkg.Pkg;
import io.foojay.api.discoclient.pkg.PackageType;
import io.foojay.api.discoclient.pkg.Distribution;
import io.foojay.api.discoclient.pkg.OperatingSystem;
import io.foojay.api.discoclient.pkg.ReleaseStatus;
import io.foojay.api.discoclient.pkg.VersionNumber;
import io.foojay.api.discoclient.pkg.ArchiveType;
import io.foojay.api.discoclient.pkg.Latest;
import io.foojay.api.discoclient.pkg.LibCType;
import io.foojay.api.discoclient.pkg.MajorVersion;
import io.foojay.api.discoclient.pkg.Scope;
import io.foojay.api.discoclient.pkg.TermOfSupport;
import static io.foojay.support.SwingWorker2.submit;
import java.awt.CardLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

@SuppressWarnings("initialization")
public class FoojayPanel extends FirstPanel {
    public static final String PROP_DOWNLOAD_SELECTION = "downloadSelection";

    private final DiscoClient discoClient;

    @UIEffect
    public static FoojayPanel create() {
        FoojayPanel f = new FoojayPanel();
        f.init();
        return f;
    }
    private final QuickPanel quickPanel;
    private final FooAdvancedPanel advancedPanel;

    @SuppressWarnings("initialization")
    @UIEffect
    private FoojayPanel() {
        // Setup disco client
        discoClient = new DiscoClient();
        quickPanel = new QuickPanel();
        advancedPanel = new FooAdvancedPanel();

        //please wait message
        ((CardLayout) getLayout()).first(this);
        tabs.add("Quick", quickPanel);
        tabs.add("Advanced", advancedPanel);
        tabs.addChangeListener((ChangeEvent e) -> {
            //TODO: tweak validity based on active tab
        });
    }

    @UIEffect
    private void init() {
        setName("Connect to OpenJDK Discovery Service");
        
        submit(() -> {
                synchronized (discoClient) {
                    // Get release infos
                    MajorVersion lastLtsRelease = discoClient.getLatestLts(false);
                    Integer lastLtsFeatureRelease = lastLtsRelease.getAsInt();

                    MajorVersion nextRelease = discoClient.getLatestSts(false);
                    Integer nextFeatureRelease = nextRelease.getAsInt();

                    List<Integer> versionNumbers = new ArrayList<>();
                    for (Integer i = 6; i <= nextFeatureRelease; i++) {
                        versionNumbers.add(i);
                    }
                    return Map.entry(versionNumbers, lastLtsFeatureRelease);
                }
        }).then((c) -> {
            //hide 'please wait' message, show tabs
            ((CardLayout) getLayout()).next(FoojayPanel.this);

            advancedPanel.setVersions(c.getKey(), c.getValue());
            quickPanel.setVersions(c.getKey(), c.getValue());
        }).handle(ex -> {
                    //TODO: bad, show something to user, auto-retry?
                    Exceptions.printStackTrace(ex);
        }).execute();
    }

    class FooAdvancedPanel extends AdvancedPanel {

        FooAdvancedPanel() {
            ListSelectionModel selectionModel = table.getSelectionModel();
            selectionModel.addListSelectionListener(e -> {
                boolean selectedSomething = table.getSelectedRow() >= 0;
                if (selectedSomething)
                    firePropertyChange(PROP_DOWNLOAD_SELECTION, false, true);
            });
        }

    @UIEffect
    @Override
    protected void updateData(Distribution distribution, Integer featureVersion, Latest latest, PackageType bundleType) {
        if (distribution == null)
            return;
        if (featureVersion == null)
            return;
        OperatingSystem operatingSystem = getOperatingSystem();
        Architecture architecture = Architecture.NONE;
        Bitness bitness = Bitness.NONE;
        ArchiveType extension = ArchiveType.NONE;
        Boolean fx = false;
        ReleaseStatus releaseStatus = ReleaseStatus.NONE;
        TermOfSupport supportTerm = TermOfSupport.NONE;
        this.setEnabled(false);
        submit(() -> {
            synchronized (discoClient) {
                List<Pkg> bundles = discoClient.getPkgs(distribution, new VersionNumber(featureVersion), latest, operatingSystem, LibCType.NONE, architecture, bitness, extension, bundleType, fx, true, releaseStatus, supportTerm, Scope.PUBLIC);
                return bundles;
            }
        }).then(this::setPackages)
                //TODO: Show something to user, offer reload, auto-reload in N seconds?
                .handle(Exceptions::printStackTrace)
                .execute();
    }

    @UIEffect
    private void setPackages(List<Pkg> bundles) {
        FoojayPanel.this.setEnabled(true);
        tableModel.setBundles(bundles);
    }
    }

    @UIEffect
    public @Nullable Pkg getBundleInfo() {
        return advancedPanel.getSelectedPackage();
    }

    private static OperatingSystem getOperatingSystem() {
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

}
