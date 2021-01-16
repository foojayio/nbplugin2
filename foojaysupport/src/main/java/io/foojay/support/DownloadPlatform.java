package io.foojay.support;

import java.io.File;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class DownloadPlatform implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private DownloadPanel component;
    private final WizardState state;
    
    DownloadPlatform(WizardState state) {
        this.state = state;
    }

    @Override
    public DownloadPanel getComponent() {
        if (component == null) {
            component = new DownloadPanel(state.pkgInfo);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return getComponent().isDownloadFinished();
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        File file = getComponent().getDownload();
        wiz.putProperty(FoojayPlatformIt.PROP_DOWNLOAD, file.getAbsolutePath());
    }

    @Override
    public void prepareValidation() {
    }

    @Override
    public void validate() throws WizardValidationException {
    }


}
