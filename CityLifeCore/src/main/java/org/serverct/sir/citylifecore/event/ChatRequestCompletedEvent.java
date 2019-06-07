package org.serverct.sir.citylifecore.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.serverct.sir.citylifecore.data.ChatRequest;

public class ChatRequestCompletedEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Getter String pluginName;
    @Getter private Player player;
    @Getter private ChatRequest chatRequest;
    @Getter private String message;

    public ChatRequestCompletedEvent(Player player, ChatRequest chatRequest) {
        this.player = player;
        this.chatRequest = chatRequest;
        this.pluginName = chatRequest.getPluginName();
        this.message = chatRequest.getMessage();
    }

    public boolean checkConsistency() {
        return player.getName().equals(chatRequest.getPlayer().getName());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
