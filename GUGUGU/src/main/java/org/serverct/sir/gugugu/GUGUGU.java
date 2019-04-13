package org.serverct.sir.gugugu;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class GUGUGU extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        while(true) {
            try {
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
