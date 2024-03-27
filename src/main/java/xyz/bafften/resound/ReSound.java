package xyz.bafften.resound;

import com.google.common.io.Resources;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.plugin.PluginEvents;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;


public class ReSound implements PluginInitializer {
    public static void main(String[] args) throws IOException {
        // This is only used to test the plugin in the dev environment
        // ExamplePlugin.main itself is never used when users run Julti

        JultiAppLaunch.launchWithDevPlugin(args, PluginManager.JultiPluginData.fromString(
                Resources.toString(Resources.getResource(ReSound.class, "/julti.plugin.json"), Charset.defaultCharset())
        ), new ReSound());
    }

    @Override
    public void initialize() {
        ReSoundOptions options;
        AtomicLong timeTracker = new AtomicLong(System.currentTimeMillis());
        try {
            options = ReSoundOptions.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Julti.log(Level.INFO, "ReSound Plugin Initialized");

        PluginEvents.RunnableEventType.RELOAD.register(() -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.INFO, "ReSound Plugin Reloaded!");
        });

        PluginEvents.RunnableEventType.END_TICK.register(() -> {
            // This gets run every tick (1 ms)
            long currentTime = System.currentTimeMillis();
            if (currentTime - timeTracker.get() > 3000) {
                // This gets ran every 3 seconds
                // Julti.log(Level.INFO, "Example Plugin ran for another 3 seconds.");
                timeTracker.set(currentTime);
            }
        });

        PluginEvents.RunnableEventType.STOP.register(() -> {
            // This gets run when Julti is shutting down
            try{
                ReSoundOptions.save();
            } catch (IOException e){
                Julti.log(Level.ERROR, "ReSound: Failed to saving options. " + e);
            }
            Julti.log(Level.INFO, "ReSound Plugin shutting down...");
        });

        PluginEvents.InstanceEventType.RESET.register(instance -> {
            ResetSoundUtil.playSound(options.ResetSound, options.ResetVolume);
        });
    }

    @Override
    public String getMenuButtonName() {
        return "Open Config";
    }

    @Override
    public void onMenuButtonPress() {
       ReSoundGUI.open();
    }
}
