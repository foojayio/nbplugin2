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

import io.foojay.api.discoclient.bundle.Architecture;
import io.foojay.api.discoclient.bundle.Bitness;
import io.foojay.api.discoclient.bundle.Bundle;
import io.foojay.api.discoclient.bundle.BundleType;
import io.foojay.api.discoclient.bundle.Distribution;
import io.foojay.api.discoclient.bundle.Extension;
import io.foojay.api.discoclient.bundle.ReleaseStatus;
import io.foojay.api.discoclient.bundle.SupportTerm;
import io.foojay.api.discoclient.bundle.VersionNumber;

import javax.swing.table.AbstractTableModel;
import java.util.List;


public class BundleTableModel extends AbstractTableModel {
    private String[]     columnNames = { "Version", "Distribution", "Vendor", "Bundle Type", "Support Term", "Release Status", "Extension", "Filename" };
    private List<Bundle> bundles;


    public BundleTableModel(final List<Bundle> bundles) {
        this.bundles = bundles;
    }


    public List<Bundle> getBundles() { return bundles; }
    public void setBundles(final List<Bundle> bundles) {
        this.bundles = bundles;
    }

    public String getColumnName(final int col) {
        switch(col) {
            case 0 :
            case 1 :
            case 2 :
            case 3 :
            case 4 :
            case 5 :
            case 6 :
            case 7 :
            case 8 : return columnNames[col];
            default: return null;
        }
    }

    public Class getColumnClass(final int col) {
        switch(col) {
            case 0 : return VersionNumber.class;
            case 1 : return Distribution.class;
            case 2 : return String.class;
            case 3 : return BundleType.class;
            case 4 : return SupportTerm.class;
            case 5 : return ReleaseStatus.class;
            case 6 : return Extension.class;
            case 7 : return String.class;
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
        final Bundle bundle = bundles.get(row);
        switch(col) {
            case 0 : return bundle.getVersionNumber();
            case 1 : return bundle.getDistribution().getUiString();
            case 2 : return bundle.getDistribution().getVendor();
            case 3 : return bundle.getBundleType().getUiString();
            case 4 : return bundle.getSupportTerm().name();
            case 5 : return bundle.getReleaseStatus().name();
            case 6 : return bundle.getExtension().getUiString();
            case 7 : return bundle.getFileName();
            default: return null;
        }
    }
}
