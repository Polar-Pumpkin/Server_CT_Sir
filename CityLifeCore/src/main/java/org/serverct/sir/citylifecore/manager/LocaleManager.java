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
    private boolean targetDebugMode;

    public LocaleUtil registerLocaleUtil(Plugin plugin) {
        targetLocaleUtil = new LocaleUtil(plugin);
        loadedLocales.put(plugin, targetLocaleUtil);
        return targetLocaleUtil;
    }

    public boolean registerDebugMode(Plugin plugin) {
        if (plugin.getConfig() != null) {
            targetDebugMode = plugin.getConfig().getBoolean("Debug");
            debugMode.put(plugin, targetDebugMode);
            return targetDebugMode;
        }
        return false;
    }

    public LocaleUtil getTargetLocaleUtil(Plugin plugin) {
        if(loadedLocales.containsKey(plugin)) {
            return loadedLocales.get(plugin);
        }
        return null;
    }

    public boolean checkDebugMode(Plugin plugin) {
        if(debugMode.containsKey(plugin)) {
            return debugMode.get(plugin);
        }
        return false;
    }

}
