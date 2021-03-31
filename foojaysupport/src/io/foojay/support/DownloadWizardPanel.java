package io.foojay.support;

import java.io.File;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.openide.WizardDescriptor;

public class DownloadWizardPanel extends AbstractWizardPanel<DownloadPanel> {

    private final WizardState state;

    DownloadWizardPanel(WizardState state) {
        this.state = state;
    }

    @UIEffect
    @Override
    protected DownloadPanel createComponent() {
        DownloadPanel component = DownloadPanel.create(state);
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

    @Override
    public void readSettings(WizardDescriptor wiz) {
        String folder = (String) wiz.getProperty(FoojayPlatformIt.PROP_DOWNLOAD_FOLDER);
        getComponent().setDownloadFolder(folder);
    }
}
