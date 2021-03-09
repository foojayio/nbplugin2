package io.foojay.support;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public abstract class AbstractWizardPanel<C extends Component> implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor> {

    private final List<ChangeListener> listeners = new ArrayList<>();
    private @MonotonicNonNull C component;

    @UIEffect
    @NonNull
    protected abstract C createComponent();

    @Override
    @UIEffect
    @SuppressWarnings("override.effect.invalid")
    public C getComponent() {
        if (component == null)
            component = createComponent();
        return component;
    }

    @UIEffect
    protected void fireChangeListeners() {
        ChangeEvent ce = new ChangeEvent(this);
        for(ChangeListener l : listeners) {
            l.stateChanged(ce);
        }
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
    }

    @Override
    public void prepareValidation() {
    }

    @Override
    public void validate() throws WizardValidationException {
    }

    @Override
    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

}
