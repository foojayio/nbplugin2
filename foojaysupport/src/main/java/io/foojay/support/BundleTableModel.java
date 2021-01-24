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

import io.foojay.api.discoclient.pkg.ArchiveType;
import io.foojay.api.discoclient.pkg.Distribution;
import io.foojay.api.discoclient.pkg.Pkg;
import io.foojay.api.discoclient.pkg.PackageType;
import io.foojay.api.discoclient.pkg.ReleaseStatus;
import io.foojay.api.discoclient.pkg.VersionNumber;

import javax.swing.table.AbstractTableModel;
import java.util.List;


public class BundleTableModel extends AbstractTableModel {
    private String[]     columnNames = { "Version", "Distribution", "Vendor", "Bundle Type", "Release Status", "Extension" };
    private List<Pkg> bundles;


    public BundleTableModel(final List<Pkg> bundles) {
        this.bundles = bundles;
    }


    public List<Pkg> getBundles() { return bundles; }
    public void setBundles(final List<Pkg> bundles) {
        this.bundles = bundles;
        this.fireTableDataChanged();
    }

    public String getColumnName(final int col) {
        switch(col) {
            case 0 :
            case 1 :
            case 2 :
            case 3 :
            case 4 :
            case 5 : return columnNames[col];
            default: return null;
        }
    }

    public Class getColumnClass(final int col) {
        switch(col) {
            case 0 : return VersionNumber.class;
            case 1 : return Distribution.class;
            case 2 : return String.class;
            case 3 : return PackageType.class;
            case 4 : return ReleaseStatus.class;
            case 5 : return ArchiveType.class;
            default: return null;
        }
    }

    @Override public int getRowCount() {
        if (null == bundles) { return 0; }
        return bundles.size();
    }

    @Override public int getColumnCount() {
        return columnNames.length;
    }

    @Override public Object getValueAt(final int row, final int col) {
        if (row < 0 || row > bundles.size()) { return null; }
        final Pkg bundle = bundles.get(row);
        switch(col) {
            case 0 : return bundle.getDistributionVersion();
            case 1 : return bundle.getDistribution().getUiString();
            //TODO: EMI: Remove Vendor column
            case 2 : return bundle.getDistribution().getUiString();
            case 3 : return bundle.getPackageType().getUiString();
            case 4 : return bundle.getReleaseStatus().name();
            case 5 : return bundle.getArchiveType().getUiString();
            default: return null;
        }
    }
}
