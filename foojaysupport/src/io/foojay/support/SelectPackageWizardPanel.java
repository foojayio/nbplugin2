package io.foojay.support;

import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.openide.WizardDescriptor;

public class SelectPackageWizardPanel extends AbstractWizardPanel<SelectPackagePanel> {

    private final WizardState state;

    SelectPackageWizardPanel(WizardState state) {
        this.state = state;
    }

    @Override
    @UIEffect
    public SelectPackagePanel createComponent() {
        SelectPackagePanel component = SelectPackagePanel.create();
        component.addPropertyChangeListener(SelectPackagePanel.PROP_VALIDITY_CHANGED, (e) -> fireChangeListeners());
        return component;
    }

    @Override
    public boolean isValid() {
        return getComponent().getSelectedPackage() != null;
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        if (!isValid())
            return;

        PkgSelection bi = getComponent().getSelectedPackage();
        if (bi == null)
            throw new IllegalStateException("Null package"); //but really, if isValid is true this should not happen
        state.selection = bi;
    }

}
