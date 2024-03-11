package xyz.bafften.resound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.duncanruns.julti.JultiOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
public  class ReSoundOptions{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ReSoundOptions instance;
    private static final Path SAVE_PATH = JultiOptions.getJultiDir().resolve("resoundoptions.json");

    public String ResetSound = JultiOptions.getJultiDir().resolve("sounds").resolve("click.wav").toAbsolutePath().toString();
    public float ResetVolume = 0.7f;

    public String username = "";
    public boolean enabled = true;

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

    public String getValueString(String optionName) {
        Object value = this.getValue(optionName);
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            List<Object> objectList = new ArrayList<>();
            for (int i = 0; i < Array.getLength(value); i++) {
                objectList.add(Array.get(value, i));
            }
            value = objectList;
        }
        return String.valueOf(value);
    }

    @Nullable
    public Object getValue(String optionName) {
        Field optionField = null;
        try {
            optionField = this.getClass().getField(optionName);
        } catch (NoSuchFieldException ignored) {
            // Handled by nullability
        }
        if (optionField == null || Modifier.isTransient(optionField.getModifiers())) {
            return null;
        }
        try {
            return optionField.get(this);
        } catch (IllegalAccessException ignored) {
            // Handled by nullability
        }
        return null;
    }
}