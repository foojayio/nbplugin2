package io.foojay.support;

import org.openide.WizardDescriptor;

public class BrowseWizardPanel extends AbstractWizardPanel<BrowsePanel> {

    private final WizardState state;

    BrowseWizardPanel(WizardState state) {
        this.state = state;
    }

    @Override
    protected BrowsePanel createComponent() {
        return BrowsePanel.create(state);
    }

    @Override
    public boolean isValid() {
        return getComponent().isOK();
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty(FoojayPlatformIt.PROP_DOWNLOAD_FOLDER, getComponent().getUserDownloadFolder());
    }
}
