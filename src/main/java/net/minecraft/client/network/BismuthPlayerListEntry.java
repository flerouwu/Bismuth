package net.minecraft.client.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.scoreboard.Team;

public class BismuthPlayerListEntry extends PlayerListEntry {
    public BismuthPlayerListEntry(GameProfile gameProfile) {
        super(gameProfile);
    }

    public Team getScoreboardTeam() {
        return null;
    }
}
