package xyz.bafften.resound;

import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.Icon;
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

import org.apache.logging.log4j.Level;


public class ReSoundGUI extends JFrame{
    private static ReSoundGUI instance = null;
    private boolean closed = false;
    private JPanel mainPanel;
    private JButton fileButton;
    private JButton volumeButton;

    public ReSoundGUI() {
        this.$$$setupUI$$$();
        this.setContentPane(this.mainPanel);
        this.setTitle("ReSound Config");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ReSoundGUI.this.onClose();
            }
        });
        this.refreshfileButton();
        this.refreshvolumeButton();
        this.fileButton.addActionListener((ignored) -> {
            // out = JOptionPane.showInputDialog(this, "Enter sound file path.(.wav)", "ReSound: Choose file", 3, (Icon)null, (Object[])null, String.valueOf(ReSoundOptions.getInstance().ResetSound));
            JFileChooser jfc = new JFileChooser(JultiOptions.getJultiDir().resolve("sounds").toString());
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("wav", "wav"));
            int result = jfc.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION){
                try{
                    String outString = jfc.getSelectedFile().getAbsolutePath();
                    ReSoundOptions.getInstance().ResetSound = outString;
                    ReSoundOptions.save();
                    this.refreshfileButton();
                } catch (IOException e) {
                    Julti.log(Level.ERROR, "ReSound: Expection: " + e);
                }
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
            try{
                ReSoundOptions.getInstance().ResetVolume = outInt / 100f;
                ReSoundOptions.save();
                this.refreshvolumeButton();
            } catch(IOException e){
                Julti.log(Level.ERROR, "ReSound: Expection: " + e);
            }
        });
        this.setSize(312, 128);
        this.setVisible(true);
    }

    private void showInvalidInput() {
        JOptionPane.showMessageDialog(this, "Enter an integer between 0 and 100.", "ReSound: Invalid Input", 0);
    }

    private void refreshfileButton() {
        Path soundfile = Paths.get(ReSoundOptions.getInstance().ResetSound);
        this.fileButton.setText("Reset Sound File: " + soundfile.getFileName().toString());
    }

    private void refreshvolumeButton() {
        this.volumeButton.setText("Volume: " + (int) (ReSoundOptions.getInstance().ResetVolume * 100) + "%");
    }

    public static ReSoundGUI open() {
        if (instance == null || instance.isClosed()) {
            instance = new ReSoundGUI();
        } else {
            instance.requestFocus();
        }
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
        this.fileButton = new JButton();
        this.fileButton.setText("Reset Goal: ");
        this.mainPanel.add(this.fileButton);
        this.volumeButton = new JButton();
        this.volumeButton.setText("Volume: ");
        this.mainPanel.add(this.volumeButton);
    }

    public JComponent $$$getRootComponent$$$() {
        return this.mainPanel;
    }
}