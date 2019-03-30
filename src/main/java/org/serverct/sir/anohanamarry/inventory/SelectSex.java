package org.serverct.sir.anohanamarry.inventory;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.serverct.sir.anohanamarry.configuration.PlayerData;

import java.util.Random;

public class SelectSex implements InventoryProvider {

    private final Random random = new Random();

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));

        Dye pdye = new Dye(DyeColor.PINK);
        Dye lbdye = new Dye(DyeColor.LIGHT_BLUE);

        contents.set(3, 4, ClickableItem.of(new ItemStack(pdye.getItemType()),
                e -> {
                    PlayerData.getPlayerDataManager().setSex(player.getName(), true);
                    player.closeInventory();
                }));

        contents.set(3, 6, ClickableItem.of(new ItemStack(lbdye.getItemType()),
                e -> {
                    PlayerData.getPlayerDataManager().setSex(player.getName(), false);
                    player.closeInventory();
                }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        if(state % 5 != 0)
            return;

        short durability = (short) random.nextInt(15);

        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, durability);
        contents.fillBorders(ClickableItem.empty(glass));
    }
}
