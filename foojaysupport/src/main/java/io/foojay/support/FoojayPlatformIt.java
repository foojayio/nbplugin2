package io.foojay.support;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.java.platform.JavaPlatform;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.Parameters;

public class FoojayPlatformIt implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private final ChangeSupport changeSupport = new ChangeSupport(this);

    private WizardDescriptor.Panel<WizardDescriptor>[] panels;
    private WizardDescriptor wizard;
    private String[] names;
    private int index;
    
    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return panels[index];
    }

    @Override
    public String name() {
        return names[index];
    }

    @Override
    public boolean hasNext() {
        return index < panels.length - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        index++;
    }

    @Override
    public void previousPanel() {
        index--;
    }

    @Override
    public void addChangeListener(@NonNull ChangeListener listener) {
        Parameters.notNull("listener", listener); //NOI18N
        changeSupport.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(@NonNull final ChangeListener listener) {
        Parameters.notNull("listener", listener); //NOI18N
        changeSupport.removeChangeListener(listener);
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        panels = (WizardDescriptor.Panel<WizardDescriptor>[]) new WizardDescriptor.Panel<?>[]{
            new SetupFoojayPlatform.Panel()
        };
        names = new String[]{
            "Connect to Universal OpenJDK Service"
        };
        index = 0;
        ((JComponent) panels[0].getComponent()).putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, names);
    }

    @Override
    public Set<JavaPlatform> instantiate() throws IOException {
        return Collections.EMPTY_SET;
    }

    @Override
    public void uninitialize(WizardDescriptor wd) {
    }

}
