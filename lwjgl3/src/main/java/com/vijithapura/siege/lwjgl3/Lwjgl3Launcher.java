package com.vijithapura.siege.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.vijithapura.siege.VijithapuraGame;
import com.vijithapura.siege.utils.Constants;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        // Wrap the application start in a try-catch block to capture crash errors
        try {
            createApplication();
        } catch (Exception e) {
            System.err.println("CRASH DURING STARTUP:");
            e.printStackTrace();
        }
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new VijithapuraGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        // Window settings
        configuration.setTitle("Vijithapura Siege - The Battle of King Dutthagamani");

        // Ensure Constants.SCREEN_WIDTH/HEIGHT exist. If not, replace with 1280, 720
        configuration.setWindowedMode(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        // Prevent resizing initially to avoid layout issues during testing
        configuration.setResizable(false);

        // Performance
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);

        // --- ICONS ---
        // I have commented this out. If these files do not exist in your assets folder,
        // the game WILL CRASH instantly with "Exit value 1".
        // Uncomment this only after you verify these files exist in your 'assets' folder.
        // configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        return configuration;
    }
}
