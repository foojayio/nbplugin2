package io.foojay.support;

import java.io.File;
import org.openide.WizardDescriptor;

public class DownloadPlatform extends AbstractWizardPanel<DownloadPanel> {

    private final WizardState state;

    DownloadPlatform(WizardState state) {
        this.state = state;
    }

    @Override
    protected DownloadPanel createComponent() {
        DownloadPanel component = new DownloadPanel(state);
        component.addPropertyChangeListener(DownloadPanel.PROP_DOWNLOAD_FINISHED, (e) -> {
            if (component.getDownload().isFile())
                component.putClientProperty(WizardDescriptor.PROP_WARNING_MESSAGE, "Could not unarchive package, please install it manually");

            fireChangeListeners();
        });
        return component;
    }

    @Override
    public boolean isValid() {
        return getComponent().isDownloadFinished();
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        File file = getComponent().getDownload();
        if (file != null)
            wiz.putProperty(FoojayPlatformIt.PROP_DOWNLOAD, file.getAbsolutePath());
    }

}
