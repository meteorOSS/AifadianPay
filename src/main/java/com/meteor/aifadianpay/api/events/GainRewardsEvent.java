package com.meteor.aifadianpay.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class GainRewardsEvent extends Event {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private String shopitem;
    private String player;

    public GainRewardsEvent(String shopitem,String player) {
        this.shopitem = shopitem;
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public String getShopitem() {
        return shopitem;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
