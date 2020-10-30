package io.foojay.support;

import java.awt.Component;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

public class SetupFoojayPlatform extends javax.swing.JPanel {

    static class Panel implements WizardDescriptor.AsynchronousValidatingPanel<WizardDescriptor>, ChangeListener {

        // https://download.jetbrains.com/jdk/feed/v1/jdks.json
        private static final String HELP_ID = "setup-foojay-platform";    //NOI18N

        private final ChangeSupport changeSupport;
        private FoojayPanel ui;
//        private boolean valid = false;
//        private volatile WizardDescriptor wizardDescriptor;

        public Panel() {
            changeSupport = new ChangeSupport(this);
        }

        @Override
        public Component getComponent() {
            if (ui == null) {
                ui = new FoojayPanel();
//                ui.addChangeListener(this);
            }
//            checkPanelValidity();
            return ui;
        }

        @Override
        public void prepareValidation() {
        }

        @Override
        public void validate() throws WizardValidationException {
        }

        @Override
        public HelpCtx getHelp() {
            return new HelpCtx(HELP_ID);
        }

        @Override
        public void readSettings(WizardDescriptor data) {
        }

        @Override
        public void storeSettings(WizardDescriptor data) {
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public void addChangeListener(ChangeListener cl) {
        }

        @Override
        public void removeChangeListener(ChangeListener cl) {
        }

        @Override
        public void stateChanged(ChangeEvent e) {
        }

    }

    public SetupFoojayPlatform() {
        initComponents();
        setName("Connect to Universal OpenJDK Service");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(SetupFoojayPlatform.class, "SetupFoojayPlatform.jTabbedPane2.TabConstraints.tabTitle"), jTabbedPane2); // NOI18N
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(SetupFoojayPlatform.class, "SetupFoojayPlatform.jTabbedPane3.TabConstraints.tabTitle"), jTabbedPane3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    // End of variables declaration//GEN-END:variables
}