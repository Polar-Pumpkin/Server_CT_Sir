package org.serverct.sir.citylifecore.api;

import org.serverct.sir.citylifecore.hooks.CareerHook;
import org.serverct.sir.citylifecore.hooks.VaultHook;
import org.serverct.sir.citylifecore.manager.AreaManager;
import org.serverct.sir.citylifecore.manager.ChatRequestManager;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.manager.SelectionManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;

public class CityLifeCoreApi {

    private AreaManager areaManager;
    private ChatRequestManager chatRequestManager;
    private SelectionManager selectionManager;
    private InventoryManager inventoryManager;
    private InventoryUtil inventoryUtil;
    private VaultHook vaultUtil;
    private CareerHook careerUtil;

    public AreaManager getAreaAPI() {
        if(areaManager == null) {
            areaManager = new AreaManager();
        }
        return areaManager;
    }

    public ChatRequestManager getChatRequestAPI() {
        if(chatRequestManager == null) {
            chatRequestManager = new ChatRequestManager();
        }
        return chatRequestManager;
    }

    public SelectionManager getSelectionAPI() {
        if(selectionManager == null) {
            selectionManager = new SelectionManager();
        }
        return selectionManager;
    }

    public InventoryManager getInventoryAPI() {
        if(inventoryManager == null) {
            inventoryManager = new InventoryManager();
        }
        return inventoryManager;
    }

    public VaultHook getVaultUtil() {
        if(vaultUtil == null) {
            vaultUtil = new VaultHook();
        }
        return vaultUtil;
    }

    public CareerHook getCareerUtil() {
        if(careerUtil == null) {
            careerUtil = new CareerHook();
        }
        return careerUtil;
    }

    public InventoryUtil getInventoryUtil() {
        if(inventoryUtil == null) {
            inventoryUtil = new InventoryUtil();
        }
        return inventoryUtil;
    }

}
