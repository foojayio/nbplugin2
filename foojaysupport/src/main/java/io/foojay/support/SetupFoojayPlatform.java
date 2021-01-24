package io.foojay.support;

import io.foojay.api.discoclient.util.PkgInfo;
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
        component.addPropertyChangeListener(FoojayPanel.PROP_DOWNLOAD_SELECTION, (e) -> fireChangeListeners());
        return component;
    }

    @Override
    public boolean isValid() {
        return getComponent().getBundleInfo() != null;
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        PkgInfo bi = getComponent().getBundleInfo();
        if (bi == null)
            throw new IllegalStateException("Null package"); //but really, if isValid is true this should not happen
        wiz.putProperty(FoojayPlatformIt.PROP_FILENAME, bi.getFileName());
        wiz.putProperty(FoojayPlatformIt.PROP_FILEURL, bi.getDirectDownloadUri());
        state.pkgInfo = bi;
    }

}
