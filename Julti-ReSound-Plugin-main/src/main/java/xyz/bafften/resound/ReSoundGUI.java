package xyz.bafften.resound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;

import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.util.GUIUtil;

public class ReSoundGUI extends JFrame {
    private static ReSoundGUI instance = null;
    JPanel mainPanel;
    private boolean closed = false;

    public ReSoundGUI() {

        setUpWindow();
        this.setTitle("ReSound Config");
        this.setContentPane(this.mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ReSoundGUI.this.onClose();
            }
        });
        this.revalidate();
        this.setMinimumSize(new Dimension(256, 128));
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public static ReSoundGUI open(Point initialLocation) {
        if (instance == null || instance.isClosed()) {
            instance = new ReSoundGUI();
            if (initialLocation != null) {
                instance.setLocation(initialLocation);
            }
        } else {
            instance.requestFocus();
        }
        return instance;
    }

    private void save() {
        try {
            ReSoundOptions.save();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    private void onClose() {
        this.closed = true;
        save();
    }

    private void setUpWindow() {
        mainPanel = new JPanel();
        Path jultiDir = JultiOptions.getJultiDir().resolve("sounds");

        mainPanel.add(GUIUtil.createSpacer());
        mainPanel.add(ReSoundUtil.leftJustify(new JLabel("Reset Sound:")));
        mainPanel.add(GUIUtil.createSpacer());
        mainPanel.add(ReSoundUtil.leftJustify(ReSoundUtil.createFileSelectButton(mainPanel, "ResetSound", "wav", jultiDir)));
        mainPanel.add(GUIUtil.createSpacer());
        mainPanel.add(ReSoundUtil.leftJustify(ReSoundUtil.createVolumeSlider("ResetVolume")));
    }
}