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
//import io.foojay.api.discoclient.pkg.Release;
import io.foojay.api.discoclient.pkg.ReleaseStatus;
import io.foojay.api.discoclient.pkg.VersionNumber;
import io.foojay.api.discoclient.pkg.ArchiveType;
import io.foojay.api.discoclient.pkg.Latest;
import io.foojay.api.discoclient.pkg.MajorVersion;
import io.foojay.api.discoclient.pkg.Scope;
import io.foojay.api.discoclient.pkg.TermOfSupport;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;

@SuppressWarnings("initialization")
public class FoojayPanel extends javax.swing.JPanel {
    public static final String PROP_DOWNLOAD_SELECTION = "downloadSelection";

    private final DiscoClient discoClient;
    private JComboBox<Integer> versionComboBox;
    private JComboBox<Distribution> distributionComboBox;
    private JComboBox<PackageType> bundleTypeComboBox;
    private JCheckBox latestCheckBox;
    private BundleTableModel tableModel;
    private JTable table;

    @UIEffect
    public static FoojayPanel create() {
        FoojayPanel f = new FoojayPanel();
        f.init();
        return f;
    }

    @SuppressWarnings("initialization")
    @UIEffect
    private FoojayPanel() {
        // Setup disco client
        discoClient = new DiscoClient();
    }

    @UIEffect
    private void init() {
        setName("Connect to OpenJDK Discovery Service");

        SwingWorker comboboxInit = new SwingWorker<Map.Entry<List<Integer>, Integer>, Object>() {

            @Override
            protected Map.Entry<List<Integer>, Integer> doInBackground() throws Exception {
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
            }

            @Override
            protected void done() {
                try {
                    Map.Entry<List<Integer>, Integer> c = get();
                    ((DefaultComboBoxModel) versionComboBox.getModel()).addAll(c.getKey());
                    versionComboBox.setSelectedItem(c.getValue());
                } catch (InterruptedException | ExecutionException ex) {
                    //TODO: bad, show something to user, auto-retry?
                    Exceptions.printStackTrace(ex);
                }
            }

        };

        // Versions
        JLabel versionLabel = new JLabel("Versions");
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        versionComboBox = new JComboBox<>();
        versionComboBox.addActionListener(e -> updateData());
        comboboxInit.execute();

        Box versionsVBox = Box.createVerticalBox();
        versionsVBox.add(versionLabel);
        versionsVBox.add(versionComboBox);
        versionsVBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Distributions
        JLabel distributionLabel = new JLabel("Distributions");
        distributionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Distribution[] distributions = {Distribution.NONE, Distribution.AOJ, Distribution.CORRETTO, Distribution.DRAGONWELL, Distribution.LIBERICA, Distribution.OJDK_BUILD, Distribution.SAP_MACHINE, Distribution.ZULU};
        distributionComboBox = new JComboBox<>(distributions);
        distributionComboBox.setRenderer(new DistributionListCellRenderer());
        distributionComboBox.addActionListener(e -> updateData());

        Box distributionVBox = Box.createVerticalBox();
        distributionVBox.add(distributionLabel);
        distributionVBox.add(distributionComboBox);
        distributionVBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Bundle Types
        JLabel bundleTypeLabel = new JLabel("Bundle Type");
        bundleTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        PackageType[] bundleTypes = Arrays.stream(PackageType.values()).filter(bundleType -> PackageType.NONE != bundleType).filter(bundleType -> PackageType.NOT_FOUND != bundleType).toArray(PackageType[]::new);
        bundleTypeComboBox = new JComboBox<>(bundleTypes);
        bundleTypeComboBox.addActionListener(e -> updateData());

        Box bundleTypeVBox = Box.createVerticalBox();
        bundleTypeVBox.add(bundleTypeLabel);
        bundleTypeVBox.add(bundleTypeComboBox);
        bundleTypeVBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Latest
        JLabel latestLabel = new JLabel("Latest");
        latestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        latestCheckBox = new JCheckBox();
        latestCheckBox.setSelected(true);
        latestCheckBox.addActionListener(e -> updateData());

        Box latestVBox = Box.createVerticalBox();
        latestVBox.add(latestLabel);
        latestVBox.add(latestCheckBox);
        latestVBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Header Box
        Box hBox = Box.createHorizontalBox();
        hBox.add(distributionVBox);
        hBox.add(versionsVBox);
        hBox.add(bundleTypeVBox);
        hBox.add(latestVBox);

        Box vBox = Box.createVerticalBox();
        vBox.add(hBox);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.PAGE_AXIS));
        headerPanel.add(hBox);

        // Table
        tableModel = new BundleTableModel(List.of());
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            boolean selectedSomething = table.getSelectedRow() >= 0;
            if (selectedSomething)
                firePropertyChange(PROP_DOWNLOAD_SELECTION, false, true);
        });
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(400, 300));

        // Setup main layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }

    @UIEffect
    private void updateData() {
        Distribution distribution = (Distribution) distributionComboBox.getSelectedItem();
        if (distribution == null)
            return;
        Integer featureVersion = (Integer) versionComboBox.getSelectedItem();
        if (featureVersion == null)
            return;
        Latest latest = latestCheckBox.isSelected() ? Latest.OVERALL : Latest.NONE;
        OperatingSystem operatingSystem = getOperatingSystem();
        Architecture architecture = Architecture.NONE;
        Bitness bitness = Bitness.NONE;
        ArchiveType extension = ArchiveType.NONE;
        PackageType bundleType = (PackageType) bundleTypeComboBox.getSelectedItem();
        Boolean fx = false;
        ReleaseStatus releaseStatus = ReleaseStatus.NONE;
        TermOfSupport supportTerm = TermOfSupport.NONE;
        this.setEnabled(false);
        SwingWorker2.submit(() -> {
            synchronized (discoClient) {
                List<Pkg> bundles = discoClient.getPkgs(distribution, new VersionNumber(featureVersion), latest, operatingSystem, architecture, bitness, extension, bundleType, fx, releaseStatus, supportTerm, Scope.PUBLIC);
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

    @UIEffect
    public @Nullable Pkg getBundleInfo() {
        int index = table.getSelectedRow();
        if (index < 0)
            return null;
        int modelIndex = table.convertRowIndexToModel(index);
        Pkg bundle = tableModel.getBundles().get(modelIndex);
        return bundle;
    }

    private OperatingSystem getOperatingSystem() {
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
