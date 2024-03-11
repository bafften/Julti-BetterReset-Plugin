package xyz.bafften.resound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.duncanruns.julti.JultiOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public  class ReSoundOptions{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ReSoundOptions instance;
    public static final Path SAVE_PATH = Paths.get(System.getProperty("user.home")).resolve(".PaceMan").resolve("options.json").toAbsolutePath();
    public String ResetSound = JultiOptions.getJultiDir().resolve("sounds").resolve("click.wav").toAbsolutePath().toString();
    public float ResetVolume = 0.7f;

    public static void save() throws IOException {
        FileWriter writer = new FileWriter(SAVE_PATH.toFile());
        GSON.toJson(instance, writer);
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