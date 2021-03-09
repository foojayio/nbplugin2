package io.foojay.support;

import java.awt.Component;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.checkerframework.checker.guieffect.qual.UIType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@UIType
public class VersionListCellRenderer extends DefaultListCellRenderer {

    private List<Integer> lts = Collections.EMPTY_LIST;

    public Component getListCellRendererComponent(JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (lts.contains(value))
            value = String.valueOf(value) + " (LTS)";
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }

    public void setLTS(@NonNull List<Integer> lts) {
        this.lts = lts;
    }
}
