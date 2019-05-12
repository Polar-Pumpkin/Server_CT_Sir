package org.serverct.sir.anohanamarry.runnable;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.configuration.PlayerData.StatusType;

import java.util.List;

public class ExpiredSocializePropose extends BukkitRunnable {

    private Player player;
    private Player targetPlayer;
    private PlayerData playerData;

    private List<String> queue;

    public ExpiredSocializePropose(Player sender, Player receiver) {
        this.player = receiver;
        this.targetPlayer = sender;
    }

    @Override
    public void run() {
        playerData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(player.getName());

        if(!playerData.getStatus().equals(StatusType.Married)) {
            queue = playerData.getQueue();
            if(queue.contains(targetPlayer.getName())) {
                queue.remove(targetPlayer.getName());
                playerData.setQueue(queue);
                PlayerDataManager.getInstance().saveData(playerData);

                Language.getInstance().sendSubtitle(targetPlayer.getName(), Language.getInstance().getMessage("Common.Propose.Socialize.Result.Expired.Subtitle").replace("%receiver%", player.getName()));
                targetPlayer.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Common.Propose.Socialize.Result.Expired.Message").replace("%receiver%", player.getName()));

                player.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands.Propose.Socialize.Expired").replace("%sender%", targetPlayer.getName()));
            }
        }
    }
}
