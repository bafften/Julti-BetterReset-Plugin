package xyz.bafften.resound;

import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.util.GUIUtil;

import org.apache.logging.log4j.Level;


public class ReSoundGUI extends JFrame{
    private static ReSoundGUI instance = null;
    private static ReSoundOptions options = ReSoundOptions.getInstance();
    private static int coundOfCheckBoxChange = 0;
    private boolean closed = false;
    private JPanel mainPanel;
    private JCheckBox enabledCheckBox;
    private JButton fileButton;
    private JButton volumeButton;
    private JButton saveButton;

    public ReSoundGUI() {
        // setup
        this.$$$setupUI$$$();
        this.setContentPane(this.mainPanel);
        this.setTitle("ReSound Config");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ReSoundGUI.this.onClose();
            }
        });

        // refresh
        this.refreshEnabledCheckBox();
        this.refreshFileButton();
        this.refreshVolumeButton();

        // actions
        this.enabledCheckBox.addActionListener((ignored) -> {
            coundOfCheckBoxChange++;
            if(this.isEnabled()){
                options.enabled = true;
            } else {
                options.enabled = false;
            }
        });

        this.fileButton.addActionListener((ignored) -> {
            // out = JOptionPane.showInputDialog(this, "Enter sound file path.(.wav)", "ReSound: Choose file", 3, (Icon)null, (Object[])null, String.valueOf(ReSoundOptions.getInstance().ResetSound));
            JFileChooser jfc = new JFileChooser(JultiOptions.getJultiDir().resolve("sounds").toString());
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("wav", "wav"));
            int result = jfc.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION){
                // try{
                //     String outString = jfc.getSelectedFile().getAbsolutePath();
                //     ReSoundOptions.getInstance().ResetSound = outString;
                //     ReSoundOptions.save();
                //     this.refreshfileButton();
                // } catch (IOException e) {
                //     Julti.log(Level.ERROR, "ReSound: Expection: " + e);
                // }
                String outString = jfc.getSelectedFile().getAbsolutePath();
                options.ResetSound = outString;
                this.refreshFileButton();
            }
        });

        this.volumeButton.addActionListener((ignored) -> {
            String current = String.valueOf(ReSoundOptions.getInstance().ResetVolume);
            int current_int = (int) (Float.parseFloat(current) * 100f);
            Object out;
            int outInt = 0;
            boolean ok = false;
            do{
                out = JOptionPane.showInputDialog(this, "Enter sound volume.(%)", "ReSound: Volume", 3, (Icon)null, (Object[])null, current_int);
                try{
                    outInt = Integer.parseInt(out.toString());
                    if ((outInt < 0) || (outInt > 100)) {
                        this.showInvalidInput();
                    } else {
                        ok = true;
                    }
                } catch (Exception e){
                    this.showInvalidInput();
                }
            } while (ok == false);
            // try{
            //     ReSoundOptions.getInstance().ResetVolume = outInt / 100f;
            //     ReSoundOptions.save();
            //     this.refreshvolumeButton();
            // } catch(IOException e){
            //     Julti.log(Level.ERROR, "ReSound: Expection: " + e);
            // }
            options.ResetVolume =  outInt / 100f;
            this.refreshVolumeButton();
        });

        this.saveButton.addActionListener((ignored) -> {
            try{
                boolean hasChangedCheckBox = false;
                if(coundOfCheckBoxChange % 2 == 1){
                    hasChangedCheckBox = true;
                }
                ReSoundOptions.save(options, hasChangedCheckBox);
            } catch (IOException e) {
                Julti.log(Level.ERROR, "ReSound: Expection: " + e);
            }
        });

        this.setSize(312, 128);
        this.setVisible(true);
    }

    private void showInvalidInput() {
        JOptionPane.showMessageDialog(this, "Enter an integer between 0 and 100.", "ReSound: Invalid Input", 0);
    }

    private void refreshEnabledCheckBox() {
        this.enabledCheckBox.setEnabled(options.enabled);
    }

    private void refreshFileButton() {
        // Path soundfile = Paths.get(ReSoundOptions.getInstance().ResetSound);
        Path soundfile = Paths.get(options.ResetSound);
        this.fileButton.setText("Reset Sound File: " + soundfile.getFileName().toString());
    }

    private void refreshVolumeButton() {
        // this.volumeButton.setText("Volume: " + (int) (ReSoundOptions.getInstance().ResetVolume * 100) + "%");
        this.volumeButton.setText("Volume: " + (int) (options.ResetVolume * 100) + "%");
    }

    public static ReSoundGUI open() {
        if (instance == null || instance.isClosed()) {
            instance = new ReSoundGUI();
        } else {
            instance.requestFocus();
        }

        options = ReSoundOptions.getInstance();
        coundOfCheckBoxChange = 0;

        return instance;
    }
    
    private void onClose() {
        this.closed = true;
    }
  
     public boolean isClosed() {
        return this.closed;
    }

    private void $$$setupUI$$$() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new FlowLayout(1, 1, 4));
        // enabled check box
        this.enabledCheckBox = new JCheckBox("enabled");
        this.mainPanel.add(this.enabledCheckBox);
        // file button
        this.fileButton = new JButton();
        this.fileButton.setText("");
        this.mainPanel.add(this.fileButton);
        // volume button
        this.volumeButton = new JButton();
        this.volumeButton.setText("Volume: ");
        this.mainPanel.add(this.volumeButton);
        // create space
        this.mainPanel.add(GUIUtil.createSpacer());
        // save button
        this.saveButton = new JButton();
        this.saveButton.setText("Save");
        this.mainPanel.add(this.saveButton);
    }

    public JComponent $$$getRootComponent$$$() {
        return this.mainPanel;
    }
}