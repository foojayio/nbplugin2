package io.foojay.support;

import io.foojay.api.discoclient.pkg.TermOfSupport;
import java.awt.Component;
import java.util.Collections;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.checkerframework.checker.guieffect.qual.UIType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@UIType
public class VersionListCellRenderer extends DefaultListCellRenderer {

    private Map<Integer, TermOfSupport> lts = Collections.EMPTY_MAP;

    @Override
    public Component getListCellRendererComponent(JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (lts.containsKey((Integer) value))
            value = LTSes.text((Integer) value, lts.get((Integer) value));
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }

    public void setLTS(@NonNull Map<Integer, TermOfSupport> lts) {
        this.lts = lts;
    }
}
