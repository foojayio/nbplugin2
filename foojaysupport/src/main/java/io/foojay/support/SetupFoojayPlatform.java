package io.foojay.support;

import io.foojay.api.discoclient.util.PkgInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class SetupFoojayPlatform implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private FoojayPanel component;
    private final List<ChangeListener> listeners = new ArrayList<>();

    private final WizardState state;
    
    SetupFoojayPlatform(WizardState state) {
        this.state = state;
    }

    @Override
    public FoojayPanel getComponent() {
        if (component == null) {
            component = new FoojayPanel();
            component.addPropertyChangeListener(FoojayPanel.PROP_DOWNLOAD_SELECTION, new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    ChangeEvent ce = new ChangeEvent(SetupFoojayPlatform.this);
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
        return getComponent().getBundleInfo() != null;
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
        PkgInfo bi = getComponent().getBundleInfo();
        wiz.putProperty(FoojayPlatformIt.PROP_FILENAME, bi.getFileName());
        wiz.putProperty(FoojayPlatformIt.PROP_FILEURL, bi.getDirectDownloadUri());
        state.pkgInfo = bi;
    }

    @Override
    public void prepareValidation() {
    }

    @Override
    public void validate() throws WizardValidationException {
    }

}
