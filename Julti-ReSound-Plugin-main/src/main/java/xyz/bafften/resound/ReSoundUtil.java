package xyz.bafften.resound;

import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.messages.OptionChangeQMessage;
import xyz.duncanruns.julti.util.GUIUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ReSoundUtil{
    private ReSoundUtil() {
    }

    private static boolean queueOptionChangeAndWait(String optionName, Object val) {
        return Julti.getJulti().queueMessageAndWait(new OptionChangeQMessage(optionName, val));
    }

    public static Component leftJustify(Component component) {
        Box b = Box.createHorizontalBox();
        b.add(component);
        b.add(Box.createHorizontalGlue());
        return b;
    }

    public static JComponent createFileSelectButton(final Component parent, final String optionName, final String fileType, Path startingLocation) {
        final ReSoundOptions options = ReSoundOptions.getInstance();

        String currentValue = options.getValueString(optionName);
        JButton button = new JButton(currentValue.isEmpty() ? "No File Selected" : currentValue);

        Path fStartingLocation = startingLocation == null ? Paths.get(currentValue) : startingLocation;

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if ((!options.getValueString(optionName).isEmpty()) && e.getButton() == 3) {
                    int ans = JOptionPane.showConfirmDialog(parent, "Clear file selection?", "Julti: Clear file selection", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (ans == 0) {
                        queueOptionChangeAndWait(optionName, "");
                        button.setText("No File Selected");
                    }
                }
            }
        });
        button.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setDialogTitle("Julti: Choose Files");
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.addChoosableFileFilter(new FileNameExtensionFilter(fileType, fileType));
            jfc.setCurrentDirectory(fStartingLocation.toFile());

            int val = jfc.showOpenDialog(parent);
            if (val == JFileChooser.APPROVE_OPTION) {
                String chosen = jfc.getSelectedFile().toPath().toString();
                queueOptionChangeAndWait(optionName, chosen);
                button.setText(chosen);
            }
        });
        return button;
    }

    public static JComponent createVolumeSlider(String optionName) {
        ReSoundOptions options = ReSoundOptions.getInstance();
        int current = (int) (((Float) options.getValue(optionName)) * 100);
        current = Math.max(0, Math.min(100, current));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel label = new JLabel();
        label.setText("Volume (" + current + "%)");

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, current);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.addChangeListener(e -> {
            int newCurrent = slider.getValue();
            queueOptionChangeAndWait(optionName, newCurrent / 100f);
            label.setText("Volume (" + newCurrent + "%)");
        });

        GUIUtil.setActualSize(slider, 200, 23);
        panel.add(slider);
        panel.add(label);

        return panel;
    }
}