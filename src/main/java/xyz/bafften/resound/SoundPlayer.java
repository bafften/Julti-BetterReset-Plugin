package xyz.bafften.resound;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Random;

import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.util.*;

public class SoundPlayer{
    private SoundPlayer(){}

    public static void playSound(String soundPath, float volume, int instances){
        soundPath = soundPath.trim();
        if (soundPath.isEmpty() || volume <= 0.0f) {
            return;
        }

        int ms = 128;
        if (instances < 12 || 4 < instances){
            ms = 256;
        } else if (instances < 18) {
            ms = 312;
        } else {
            ms = 380;
        }

        Player p = new Player(new File(soundPath), volume, ms);
        p.start();
    }
}

class Player extends Thread {

    private File soundFile;
    private float volume;
    private int ms;

    Player(File soundFile, float volume, int ms){
        this.soundFile = soundFile;
        this.volume = volume;
        this.ms = ms;
    }

    @Override
    public void run(){
        try {
            this.volume = Math.min(1.0f, Math.max(0.0f, this.volume));
            final AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
            final Clip clip = AudioSystem.getClip();
            clip.open(stream);

            FloatControl gainControl = ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN));
            gainControl.setValue(((gainControl.getMaximum() - gainControl.getMinimum()) * this.volume) + gainControl.getMinimum());

            Random rnd = new Random();
            int rndValue = rnd.nextInt(ms);

            Thread.sleep(rndValue);

            clip.loop(0);
            clip.addLineListener(e -> {
                if (e.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();
        } catch (Exception e) {
            Julti.log(Level.ERROR, "Failed to play sound:\n" + ExceptionUtil.toDetailedString(e));
        }
    }
}