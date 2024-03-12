package xyz.bafften.resound;

import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import xyz.duncanruns.julti.Julti;
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
        this.setTitle("Julti Benchmark Options");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ReSoundGUI.this.onClose();
            }
        });
        this.refreshfileButton();
        this.refreshvolumeButton();
        this.fileButton.addActionListener((ignored) -> {
            Object out;
            do{
                out = JOptionPane.showInputDialog(this, "Enter sound file path.(.wav)", "ReSound: Choose file", 3, (Icon)null, (Object[])null, String.valueOf(ReSoundOptions.getInstance().ResetSound));
            } while(out == null);
            try{
                String outString = out.toString();
                ReSoundOptions.getInstance().ResetSound = outString;
                ReSoundOptions.save();
                this.refreshfileButton();
            } catch (IOException e) {
                Julti.log(Level.ERROR, "ReSound: Expection: " + e);
            }
        });
        this.volumeButton.addActionListener((ignored) -> {
            Object out = JOptionPane.showInputDialog(this, "Enter sound volume.(%)", "ReSound: Volume", 3, (Icon)null, (Object[])null, String.valueOf(ReSoundOptions.getInstance().ResetVolume));
            if (out != null) {
                try {
                    int outInt = Integer.parseInt(out.toString());
                    if (outInt < 1) {
                        this.showInvalidInput();
                    } else {
                        try{
                            ReSoundOptions.getInstance().ResetVolume = outInt / 100f;
                            ReSoundOptions.save();
                            this.refreshvolumeButton();
                        } catch(IOException e){
                            Julti.log(Level.ERROR, "ReSound: Expection: " + e);
                        }
                    }
                } catch (NumberFormatException var4) {
                    this.showInvalidInput();
                }
            }
        });
        this.setSize(312, 128);
        this.setVisible(true);
    }

    private void showInvalidInput() {
        JOptionPane.showMessageDialog(this, "You did not input a positive number.", "ReSound: Invalid Input", 0);
    }

    private void refreshfileButton() {
        this.fileButton.setText("Reset Sound File: " + ReSoundOptions.getInstance().ResetSound);
    }

    private void refreshvolumeButton() {
        this.volumeButton.setText("Volume: " + (ReSoundOptions.getInstance().ResetVolume * 100) + "%");
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
        // this.mainPanel.setLayout(new GridLayout(2, 1));
        this.mainPanel.setLayout(new FlowLayout(1, 1, 2));
        this.fileButton = new JButton();
        this.fileButton.setText("Reset Goal: ");
        this.mainPanel.add(this.fileButton);
        this.volumeButton = new JButton();
        this.volumeButton.setText("Start Benchmark");
        this.mainPanel.add(this.volumeButton);
    }

    public JComponent $$$getRootComponent$$$() {
        return this.mainPanel;
    }
}