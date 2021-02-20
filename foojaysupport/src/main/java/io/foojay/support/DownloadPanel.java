package io.foojay.support;

import io.foojay.api.discoclient.DiscoClient;
import io.foojay.api.discoclient.event.DownloadEvt;
import io.foojay.api.discoclient.event.Evt;
import io.foojay.api.discoclient.pkg.Pkg;
import static io.foojay.support.SwingWorker2.submit;
import io.foojay.support.archive.JDKCommonsUnzip;
import io.foojay.support.archive.UnarchiveUtils;
import io.foojay.support.ioprovider.IOContainerPanel;
import java.awt.CardLayout;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.checkerframework.checker.guieffect.qual.UIEffect;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOContainer;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

@SuppressWarnings("initialization")
public class DownloadPanel extends javax.swing.JPanel {

    public final static String PROP_DOWNLOAD_FINISHED = "downloadFinished";

    private boolean downloadFinished;
    private File download;
    private final DiscoClient discoClient;
    private final WizardState state;
    private IOContainerPanel executionPanel;

    @UIEffect
    public static DownloadPanel create(WizardState state) {
        DownloadPanel d = new DownloadPanel(state);
        d.init();
        return d;
    }

    @UIEffect
    @SuppressWarnings("initialization")
    private DownloadPanel(WizardState state) {
        this.state = state;
        discoClient = new DiscoClient();
    }

    @UIEffect
    private void init() {
        setName("Download");

        initComponents();

        this.executionPanel = new IOContainerPanel();

        discoClient.setOnEvt(DownloadEvt.DOWNLOAD_STARTED, this::handleDownloadStarted);
        discoClient.setOnEvt(DownloadEvt.DOWNLOAD_FINISHED, this::handleDownloadFinished);
        discoClient.setOnEvt(DownloadEvt.DOWNLOAD_FAILED, this::handleDownloadFailed);
        discoClient.setOnEvt(DownloadEvt.DOWNLOAD_PROGRESS, this::handleDownloadProgress);
    }

    @Override
    @UIEffect
    public void addNotify() {
        super.addNotify();
        jdkDescription.setText(state.selection.getFileName());
    }

    @UIEffect
    private void downloadBundle(File destinationFolder) {
        statusLabel.setText("Preparing...");
        submit(() -> {
            Pkg bundle = state.selection.get(discoClient);
            return discoClient.getPkgInfo(bundle.getEphemeralId(), bundle.getJavaVersion());
        }).then(pkgInfo -> {
            download = new File(destinationFolder, pkgInfo.getFileName());

            //        if (download.exists())
            //            handleDCEvent(this, new DCEvent(DCEventType.DOWNLOAD_FINISHED, 1));
            Future<?> future = discoClient.downloadPkg(pkgInfo, download.getAbsolutePath());
            //        try {
            //            assert null == future.get();
            //        } catch (InterruptedException | ExecutionException e) {
            //
            //        }
        }).handle(Exceptions::printStackTrace)
        .execute();
    }

    private void handleDownloadStarted(Evt e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Downloading...");
                    downloadButton.setEnabled(false);
                });
    }

    private void handleDownloadFinished(Evt e) {
                downloadFinished = true;
                final FileObject downloadFO = FileUtil.toFileObject(download);
                //TODO: check only for zip
                if (FileUtil.isArchiveFile(downloadFO)) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Unarchiving...");
                        bottomPanel.add(executionPanel);
                        ((CardLayout) bottomPanel.getLayout()).last(bottomPanel);
                        InputOutput io = IOProvider.getDefault().getIO("Unarchive output", new Action[0], IOContainer.create(executionPanel));

                        RequestProcessor.getDefault().post(() -> unarchive(io));
                    });
                } else {
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Download finished."));
                    SwingUtilities.invokeLater(() -> firePropertyChange(PROP_DOWNLOAD_FINISHED, false, true));
                }
    }

    private void handleDownloadProgress(Evt e) {
        DownloadEvt event = (DownloadEvt) e;
                SwingUtilities.invokeLater(() -> progressBar.setValue((int) ((double) event.getFraction() / (double) event.getFileSize() * 100)));
    }

    private void handleDownloadFailed(Evt e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(DownloadPanel.this, "Download failed", "Attention", JOptionPane.WARNING_MESSAGE));
    }
    
    
    private void unarchive(InputOutput io) {
        try {
            File outputFile = UnarchiveUtils.unarchive(download, io);
            SwingUtilities.invokeLater(() -> statusLabel.setText("Unarchived."));
            //find bin folder and return the parent of that as the download path
            File binFolder = JDKCommonsUnzip.findBin(outputFile);
            if (binFolder != null) {
                File parent = binFolder.getParentFile();
                if (parent != null) //but, really, could the parent ever be null?
                    download = parent;
            }
        } catch (IOException | InterruptedException ex) {
            //TODO: What now? Reveal file?
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedOperationException uoe) {
            Exceptions.printStackTrace(uoe);
        }
        SwingUtilities.invokeLater(() -> firePropertyChange(PROP_DOWNLOAD_FINISHED, false, true));
    }

    public boolean isDownloadFinished() {
        return downloadFinished;
    }

    //@Nullable
    public File getDownload() {
        return download;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @UIEffect
    @SuppressWarnings({"unchecked", "nullness"})
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jdkDescription = new javax.swing.JLabel();
        downloadButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        downloadPathText = new javax.swing.JTextField();
        chooseButton = new javax.swing.JToggleButton();
        statusLabel = new javax.swing.JLabel();
        bottomPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jdkDescription, org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.jdkDescription.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(downloadButton, org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.downloadButton.text")); // NOI18N
        downloadButton.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.jLabel2.text")); // NOI18N

        downloadPathText.setEditable(false);
        downloadPathText.setText(org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.downloadPathText.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(chooseButton, org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.chooseButton.text")); // NOI18N
        chooseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseButtonActionPerformed(evt);
            }
        });

        statusLabel.setFont(statusLabel.getFont().deriveFont((statusLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        org.openide.awt.Mnemonics.setLocalizedText(statusLabel, org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.statusLabel.text")); // NOI18N

        bottomPanel.setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        progressBar.setStringPainted(true);
        jPanel1.add(progressBar, java.awt.BorderLayout.NORTH);

        bottomPanel.add(jPanel1, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downloadPathText)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chooseButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(downloadButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jdkDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jdkDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(downloadPathText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(downloadButton)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    @UIEffect
    private void chooseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setDialogTitle("Select destination folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File folder = fileChooser.getSelectedFile();
            if (folder != null) {
                String destinationFolder = folder.getAbsolutePath();
                downloadPathText.setText(destinationFolder);
                downloadButton.setEnabled(true);

                downloadBundle(folder);
            }
        }
    }//GEN-LAST:event_chooseButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JToggleButton chooseButton;
    private javax.swing.JButton downloadButton;
    private javax.swing.JTextField downloadPathText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jdkDescription;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables

}
