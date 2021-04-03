/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.foojay.support;

import io.foojay.api.discoclient.pkg.Distribution;
import io.foojay.api.discoclient.pkg.Latest;
import io.foojay.api.discoclient.pkg.PackageType;
import io.foojay.api.discoclient.pkg.Pkg;
import io.foojay.api.discoclient.pkg.TermOfSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableModel;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AdvancedPanel extends javax.swing.JPanel {

    protected BundleTableModel tableModel;

    public AdvancedPanel() {
        initComponents();

        distributionComboBox.setRenderer(new DistributionListCellRenderer());
        versionComboBox.setRenderer(new VersionListCellRenderer());
    }

    @UIEffect
    public @Nullable
    Pkg getSelectedPackage() {
        int index = table.getSelectedRow();
        if (index < 0)
            return null;
        int modelIndex = table.convertRowIndexToModel(index);
        Pkg bundle = tableModel.getBundles().get(modelIndex);
        return bundle;
    }

    private TableModel createTableModel() {
        if (tableModel == null) {
            tableModel = new BundleTableModel(new ArrayList<>());
        }

        return tableModel;
    }

    @UIEffect
    protected abstract void updateData(Distribution distribution, Integer featureVersion, Latest latest, PackageType bundleType);
    
    protected void setVersions(List<Integer> versions, Map<Integer, TermOfSupport> lts) {
        List<Integer> reversedVersions = new ArrayList<>(versions);
        Collections.sort(reversedVersions, Collections.reverseOrder());
        ((VersionListCellRenderer) versionComboBox.getRenderer()).setLTS(lts);
        DefaultComboBoxModel versionModel = (DefaultComboBoxModel<Integer>) versionComboBox.getModel();
        reversedVersions.forEach(v -> versionModel.addElement(v));
        versionModel.setSelectedItem(LTSes.latest(lts));
    }

    private ComboBoxModel<Integer> createVersionComboboxModel() {
        return new DefaultComboBoxModel<>();
    }

    private ComboBoxModel<Distribution> createDistributionComboboxModel() {
        Distribution[] distributions = {Distribution.NONE, Distribution.AOJ, Distribution.CORRETTO, Distribution.DRAGONWELL, Distribution.LIBERICA, Distribution.OJDK_BUILD, Distribution.SAP_MACHINE, Distribution.ZULU};

        return new DefaultComboBoxModel<>(distributions);
    }

    private ComboBoxModel<PackageType> createPackageTypeComboboxModel() {
        PackageType[] bundleTypes = Arrays.stream(PackageType.values()).filter(bundleType -> PackageType.NONE != bundleType).filter(bundleType -> PackageType.NOT_FOUND != bundleType).toArray(PackageType[]::new);
        return new DefaultComboBoxModel<>(bundleTypes);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        distributionComboBox = new javax.swing.JComboBox<>();
        javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        versionComboBox = new javax.swing.JComboBox<>();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        packageTypeComboBox = new javax.swing.JComboBox<>();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        latestCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.jLabel1.text")); // NOI18N
        jPanel4.add(jLabel1);

        distributionComboBox.setModel(createDistributionComboboxModel());
        distributionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distributionComboBoxActionPerformed(evt);
            }
        });
        jPanel4.add(distributionComboBox);

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.jLabel2.text")); // NOI18N
        jPanel5.add(jLabel2);

        versionComboBox.setModel(createVersionComboboxModel());
        versionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versionComboBoxActionPerformed(evt);
            }
        });
        jPanel5.add(versionComboBox);

        jPanel1.add(jPanel5);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.jLabel3.text")); // NOI18N
        jPanel3.add(jLabel3);

        packageTypeComboBox.setModel(createPackageTypeComboboxModel());
        packageTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                packageTypeComboBoxActionPerformed(evt);
            }
        });
        jPanel3.add(packageTypeComboBox);

        jPanel1.add(jPanel3);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.jLabel4.text")); // NOI18N
        jPanel2.add(jLabel4);

        latestCheckBox.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(latestCheckBox, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.latestCheckBox.text")); // NOI18N
        latestCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                latestCheckBoxActionPerformed(evt);
            }
        });
        jPanel2.add(latestCheckBox);

        jPanel1.add(jPanel2);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        table.setAutoCreateRowSorter(true);
        table.setModel(createTableModel());
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(table);

        add(jScrollPane1, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void latestCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_latestCheckBoxActionPerformed
        updateData((Distribution) distributionComboBox.getSelectedItem(),
                (Integer) versionComboBox.getSelectedItem(),
                latestCheckBox.isSelected() ? Latest.OVERALL : Latest.NONE,
                (PackageType) packageTypeComboBox.getSelectedItem());
    }//GEN-LAST:event_latestCheckBoxActionPerformed

    private void distributionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distributionComboBoxActionPerformed
        updateData((Distribution) distributionComboBox.getSelectedItem(),
                (Integer) versionComboBox.getSelectedItem(),
                latestCheckBox.isSelected() ? Latest.OVERALL : Latest.NONE,
                (PackageType) packageTypeComboBox.getSelectedItem());
    }//GEN-LAST:event_distributionComboBoxActionPerformed

    private void versionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_versionComboBoxActionPerformed
        updateData((Distribution) distributionComboBox.getSelectedItem(),
                (Integer) versionComboBox.getSelectedItem(),
                latestCheckBox.isSelected() ? Latest.OVERALL : Latest.NONE,
                (PackageType) packageTypeComboBox.getSelectedItem());
    }//GEN-LAST:event_versionComboBoxActionPerformed

    private void packageTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_packageTypeComboBoxActionPerformed
        updateData((Distribution) distributionComboBox.getSelectedItem(),
                (Integer) versionComboBox.getSelectedItem(),
                latestCheckBox.isSelected() ? Latest.OVERALL : Latest.NONE,
                (PackageType) packageTypeComboBox.getSelectedItem());
    }//GEN-LAST:event_packageTypeComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Distribution> distributionComboBox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox latestCheckBox;
    private javax.swing.JComboBox<PackageType> packageTypeComboBox;
    protected javax.swing.JTable table;
    private javax.swing.JComboBox<Integer> versionComboBox;
    // End of variables declaration//GEN-END:variables
}
