package xyz.bafften.julti.betterresetplugin;

import com.google.common.io.Resources;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.gui.JultiGUI;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class BetterResetPlugin implements PluginInitializer {
    public static void main(String[] args) throws IOException {
        // This is only used to test the plugin in the dev environment
        // ExamplePlugin.main itself is never used when users run Julti

        JultiAppLaunch.launchWithDevPlugin(args, PluginManager.JultiPluginData.fromString(
                Resources.toString(Resources.getResource(BetterResetPlugin.class, "/julti.plugin.json"), Charset.defaultCharset())
        ), new BetterResetPlugin());
    }

    @Override
    public void initialize() {
        // This gets run once when Julti launches
        InitStuff.init();
        Julti.log(Level.INFO, "Better Reset Plugin Initialized");
    }

    @Override
    public String getMenuButtonName() {
        return "I'm a button!";
    }

    @Override
    public void onMenuButtonPress() {
        JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Holy moly! You pressed the example plugin button!!!", "Jojulti Multi Instance Macro Example Plugin Button.", JOptionPane.INFORMATION_MESSAGE);
    }
}
