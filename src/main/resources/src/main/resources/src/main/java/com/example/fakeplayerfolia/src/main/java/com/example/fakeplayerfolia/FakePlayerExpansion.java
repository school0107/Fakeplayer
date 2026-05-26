package com.example.fakeplayerfolia;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FakePlayerExpansion extends PlaceholderExpansion {

    private final FakePlayerFolia plugin;

    public FakePlayerExpansion(FakePlayerFolia plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getAuthor() {
        return "AI_Developer";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "fakeplayer";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; 
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        // %fakeplayer_online% -> Trả về số người chơi thật + số fake player
        if (params.equalsIgnoreCase("online")) {
            int realPlayers = Bukkit.getOnlinePlayers().size();
            int fakePlayers = plugin.getFakePlayers().size();
            return String.valueOf(realPlayers + fakePlayers);
        }

        // %fakeplayer_count% -> Chỉ trả về số fake player
        if (params.equalsIgnoreCase("count")) {
            return String.valueOf(plugin.getFakePlayers().size());
        }

        return null;
    }
}