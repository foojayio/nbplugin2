package io.foojay.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class DownloadPlatform implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private final List<ChangeListener> listeners = new ArrayList<>();
    private DownloadPanel component;
    private final WizardState state;

    DownloadPlatform(WizardState state) {
        this.state = state;
    }

    @Override
    public DownloadPanel getComponent() {
        if (component == null) {
            component = new DownloadPanel(state);
            component.addPropertyChangeListener(DownloadPanel.PROP_DOWNLOAD_FINISHED, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    if (component.getDownload().isFile()) {
                        component.putClientProperty(WizardDescriptor.PROP_WARNING_MESSAGE, "Could not unarchive package, please install it manually");
                    }

                    ChangeEvent ce = new ChangeEvent(DownloadPlatform.this);
                    listeners.forEach(l -> l.stateChanged(ce));
                }
            });
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
        listeners.add(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        File file = getComponent().getDownload();
        if (file != null) {
            wiz.putProperty(FoojayPlatformIt.PROP_DOWNLOAD, file.getAbsolutePath());
        }
    }

    @Override
    public void prepareValidation() {
    }

    @Override
    public void validate() throws WizardValidationException {
    }

}
