package io.foojay.support;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.awt.StatusDisplayer;

public final class FoojayPlatformIt implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    public static final String PROP_FILENAME = "fileName"; //NOI18N
    public static final String PROP_FILEURL = "url"; //NOI18N
    
    private int index;

    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;
    private WizardDescriptor wizard;
    private String[] names;

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
            panels.add(new SetupFoojayPlatform());
            String[] steps = new String[panels.size()];
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
                // Default step name to component name of panel.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
        }
        return panels;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
        getPanels();
    }

    @Override
    public Set instantiate() throws IOException {
        String fileName = (String) wizard.getProperty(FoojayPlatformIt.PROP_FILENAME); 
        String fileURL = (String) wizard.getProperty(FoojayPlatformIt.PROP_FILEURL);
        StatusDisplayer.getDefault().setStatusText(fileName + " / " + fileURL);
        return Collections.EMPTY_SET;
    }
    
    @Override
    public void uninitialize(WizardDescriptor wd) {
    }

}
