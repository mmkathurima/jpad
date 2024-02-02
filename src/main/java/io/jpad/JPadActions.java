package io.jpad;

import com.google.common.base.Preconditions;
import com.timestored.command.Command;
import com.timestored.command.CommandProvider;
import com.timestored.docs.Document;
import com.timestored.docs.OpenDocumentsModel;
import com.timestored.swingxx.SwingUtils;
import com.timestored.theme.ShortcutAction;
import com.timestored.theme.Theme;
import io.jpad.model.JEngine;
import io.jpad.model.JEngineAdapter;
import io.jpad.model.RunConfig;
import io.jpad.model.RunResult;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class JPadActions
        implements CommandProvider {
    private final JEngine jEngine;
    private final JPPersistance jpPersistance;
    private final Frame frame;
    private final OpenDocumentsModel openDocumentsModel;
    private final Action runAction;
    private final Action preferencesAction;
    private final UploadSnipAction uploadSnipAction;

    public JPadActions(final JEngine jEngine, final DocumentTabbedPane documentTabbedPane, final JPPersistance jpPersistance, final Frame frame, OpenDocumentsModel openDocumentsModel, final RunConfig runConfig) {
        this.jEngine = Preconditions.checkNotNull(jEngine);
        this.jpPersistance = Preconditions.checkNotNull(jpPersistance);
        this.frame = Preconditions.checkNotNull(frame);
        this.openDocumentsModel = Preconditions.checkNotNull(openDocumentsModel);

        this.uploadSnipAction = new UploadSnipAction();
        this.uploadSnipAction.putValue("AcceleratorKey", KeyStroke.getKeyStroke(117, 0));
        jEngine.addListener(new JEngineAdapter() {
            public void resultReturned(RunResult runResult) {
                boolean f = JPadActions.this.uploadSnipAction.calculateEnabled();
                JPadActions.this.uploadSnipAction.setEnabled(f);
            }
        });

        this.runAction = new AbstractAction("Run Java Code", Theme.CIcon.SERVER_GO.get16()) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                runConfig.setRawCode(documentTabbedPane.getText());
                if (runConfig.getRawCode().length() > 0) {
                    jEngine.run(runConfig);
                }
            }
        };
        this.runAction.putValue("ShortDescription", "Run Java Code.");
        this.runAction.putValue("AcceleratorKey", KeyStroke.getKeyStroke(116, 0));
        this.runAction.putValue("MnemonicKey", Integer.valueOf(82));

        String lTitle = "Configure Upload Login";
        String lMsg = "Set the username and password used for uploading to jpad.io";
        this
                .preferencesAction = new ShortcutAction(lTitle, null, lMsg, Integer.valueOf(67), 0) {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                final LoginConfigureDialog d = new LoginConfigureDialog();
                d.setUsername(jpPersistance.get(JPPersistance.Key.USERNAME, ""));
                d.setPassword(jpPersistance.get(JPPersistance.Key.PASSWORD, ""));
                d.setListener(new LoginConfigureDialog.Listener() {
                    public void onSave() {
                        jpPersistance.put(JPPersistance.Key.USERNAME, d.getUsername());
                        jpPersistance.put(JPPersistance.Key.PASSWORD, new String(d.getPassword()));
                    }
                });
                d.setLocationRelativeTo(frame);
                d.setVisible(true);
            }
        }
        ;
    }

    private static String getVal(Map<String, String> map, String key) {
        String s = map.get(key);
        if (s == null) {
            throw new IllegalArgumentException("Key: " + key + " not found in map");
        }
        return s;
    }

    private static Snip getSnip(Map<String, String> renderings) {
        Snip snip = new Snip();

        snip.setJpadCode(getVal(renderings, "jpadCode"));
        snip.setHtmlOut(getVal(renderings, "htmlOut"));
        snip.setConsoleOut(getVal(renderings, "consoleOut"));
        snip.setBytecodeOut(getVal(renderings, "bytecodeOut"));
        snip.setJpadVersion("1.07");


        JpadHeader jpde = JpadHeader.extract(snip.getJpadCode());
        snip.setTitle(jpde.getTitle());
        snip.setTags(jpde.getTags());
        snip.setDescription(jpde.getDescription());
        jpde.getSnipID().ifPresent(snip::setSnipId);

        return snip;
    }

    public Action getRunAction() {
        return this.runAction;
    }

    public Action getPreferencesAction() {
        return this.preferencesAction;
    }

    public UploadSnipAction getUploadSnipAction() {
        return this.uploadSnipAction;
    }

    public Collection<Command> getCommands() {
        return Collections.emptyList();
    }

    private class UploadSnipAction
            extends ShortcutAction {
        private static final long serialVersionUID = 1L;

        public UploadSnipAction() {
            super("Upload Snip", Theme.CIcon.UP_CLOUD, "Upload to jpad.io for sharing", 85, 117);
        }

        public void actionPerformed(ActionEvent e) {
            Snip snip = JPadActions.getSnip(JPadActions.this.jEngine.getLatestRenderings());


            if (snip.getTitle().isEmpty()) {
                String t = JPadActions.this.openDocumentsModel.getSelectedDocument().getTitle();
                if (t.endsWith(".jpad")) {
                    t = t.substring(0, t.length() - 5);
                }
                snip.setTitle(t);
            }

            if (snip.getJpadCode().isEmpty()) {
                String message = "You must first run some jpad code before attempting to upload.";
                SwingUtils.showMessageDialog(JPadActions.this.frame, message, "Must Run Code First", 2);

                return;
            }
            if (!JPadActions.this.jpPersistance.isLoginSet()) {
                JPadActions.this.preferencesAction.actionPerformed(null);
            }

            if (JPadActions.this.jpPersistance.isLoginSet()) {
                String u = JPadActions.this.jpPersistance.get(JPPersistance.Key.USERNAME, "");
                String p = JPadActions.this.jpPersistance.get(JPPersistance.Key.PASSWORD, "");
                SnipUploadDialog sed = new SnipUploadDialog(JPadActions.this.frame, snip, u, p);
                sed.setVisible(true);


                UploadResult uploadResult = sed.getUploadResult();
                if (uploadResult != null && uploadResult.isSuccessful()) {
                    JpadHeader jpde = JpadHeader.build(snip, uploadResult.getUrl());
                    Document doc = JPadActions.this.openDocumentsModel.getSelectedDocument();
                    String s = jpde.replaceComment(doc.getContent());
                    doc.setContent(s);
                }
            }
        }

        public boolean calculateEnabled() {
            Snip snip = JPadActions.getSnip(JPadActions.this.jEngine.getLatestRenderings());
            Document doc = JPadActions.this.openDocumentsModel.getSelectedDocument();
            String content = doc.getContent();
            return (!snip.getJpadCode().isEmpty() && content.equals(snip.getJpadCode()));
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\JPadActions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */