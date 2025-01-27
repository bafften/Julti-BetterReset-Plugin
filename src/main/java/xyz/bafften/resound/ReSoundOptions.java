package xyz.bafften.resound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.duncanruns.julti.JultiOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public  class ReSoundOptions{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ReSoundOptions instance;
    private static final Path SAVE_PATH = JultiOptions.getJultiDir().resolve("resoundoptions.json");

    // 設定項目
    public boolean enabled = true;
    public String ResetSound = JultiOptions.getJultiDir().resolve("sounds").resolve("click.wav").toAbsolutePath().toString();
    public float ResetVolume = 0.7f;

    // Julti側の設定の保存
    public float singleResetVolume;
    public float multiResetVolume;

    public static void save() throws IOException{
        FileWriter writer = new FileWriter(SAVE_PATH.toFile());
        GSON.toJson(instance, writer);
        writer.close();
    }

    public static void save(ReSoundOptions options, boolean hasChangedCheckBox) throws IOException{
        FileWriter writer = new FileWriter(SAVE_PATH.toFile());

        if (hasChangedCheckBox){
            if (options.enabled){
                ReSoundUtil.jultiVolumeOff();
            } else {
                ReSoundUtil.jultiVolumeOn(instance);
            }
        }
        GSON.toJson(options, writer);
        writer.close();
    }

    public static ReSoundOptions load() throws IOException {
        if (Files.exists(SAVE_PATH)) {
            instance = GSON.fromJson(new String(Files.readAllBytes(SAVE_PATH)), ReSoundOptions.class);
        } else {
            instance = new ReSoundOptions();
        }
        return instance;
    }

    public static ReSoundOptions getInstance() {
        return instance;
    }
}