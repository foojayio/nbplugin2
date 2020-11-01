package io.foojay.support;

import io.foojay.api.discoclient.util.BundleFileInfo;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class SetupFoojayPlatform implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private FoojayPanel component;

    @Override
    public FoojayPanel getComponent() {
        if (component == null) {
            component = new FoojayPanel();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return true;
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
        BundleFileInfo bi = getComponent().getBundleInfo();
        wiz.putProperty(FoojayPlatformIt.PROP_FILENAME, bi.getFileName());
        wiz.putProperty(FoojayPlatformIt.PROP_FILEURL, bi.getFileDownloadUrl());
    }

    @Override
    public void prepareValidation() {
    }

    @Override
    public void validate() throws WizardValidationException {
    }

}
