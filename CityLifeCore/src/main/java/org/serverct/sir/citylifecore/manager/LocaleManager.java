package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.utils.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

public class LocaleManager {

    @Getter private Map<Plugin, LocaleUtil> loadedLocales = new HashMap<>();
    @Getter private Map<Plugin, Boolean> debugMode = new HashMap<>();

    private LocaleUtil targetLocaleUtil;

    public LocaleUtil registerLocaleUtil(Plugin plugin) {
        targetLocaleUtil = new LocaleUtil(plugin);
        loadedLocales.put(plugin, targetLocaleUtil);
        return targetLocaleUtil;
    }

    public void registerDebugMode(Plugin plugin) {
        if (plugin.getConfig() != null) {
            debugMode.put(plugin, plugin.getConfig().getBoolean("Debug"));
        }
    }

    public boolean checkDebugMode(Plugin plugin) {
        if(debugMode.containsKey(plugin)) {
            return debugMode.get(plugin);
        }
        return false;
    }

}
