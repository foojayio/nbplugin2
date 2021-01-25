package io.foojay.support;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.java.platform.JavaPlatform;
import org.netbeans.modules.java.j2seplatform.api.J2SEPlatformCreator;
import org.openide.WizardDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileUtil;

@SuppressWarnings("initialization")
public final class FoojayPlatformIt implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    public static final String PROP_DOWNLOAD = "download"; //NOI18N
    
    private int index;

    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;
    private WizardDescriptor wizard;
    private WizardState state = new WizardState();
    private String[] names;

    @SuppressWarnings("call.invalid.ui") //TODO: Remove this and fix the underlying warning
    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<>();
            panels.add(new SetupFoojayPlatform(state));
            panels.add(new DownloadPlatform(state));
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
    public Set<JavaPlatform> instantiate() throws IOException {
        //TODO: Download (in background?)
        String downloadedFolder = (String) wizard.getProperty(FoojayPlatformIt.PROP_DOWNLOAD);
        if (downloadedFolder != null) {
            String name = state.pkgInfo.getDistribution().getUiString() + " "
                    + state.pkgInfo.getJavaVersion().toString();
            return Collections.singleton(J2SEPlatformCreator.createJ2SEPlatform(FileUtil.toFileObject(new File(downloadedFolder)), name));
        } else {
            //TODO: notifcation?
            return Collections.EMPTY_SET;
        }
    }
    
    @Override
    public void uninitialize(WizardDescriptor wd) {
    }

}
