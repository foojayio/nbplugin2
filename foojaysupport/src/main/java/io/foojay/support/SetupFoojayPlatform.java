package io.foojay.support;

import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.openide.WizardDescriptor;

public class SetupFoojayPlatform extends AbstractWizardPanel<FoojayPanel> {

    private final WizardState state;

    SetupFoojayPlatform(WizardState state) {
        this.state = state;
    }

    @Override
    @UIEffect
    public FoojayPanel createComponent() {
        FoojayPanel component = FoojayPanel.create();
        component.addPropertyChangeListener(FoojayPanel.PROP_VALIDITY_CHANGED, (e) -> fireChangeListeners());
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
