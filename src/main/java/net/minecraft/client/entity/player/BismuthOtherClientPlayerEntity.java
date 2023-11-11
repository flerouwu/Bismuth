package net.minecraft.client.entity.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.BismuthPlayerListEntry;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.world.BismuthClientWorld;
import net.minecraft.entity.player.OtherClientPlayerEntity;

public class BismuthOtherClientPlayerEntity extends OtherClientPlayerEntity {
    public BismuthOtherClientPlayerEntity(GameProfile profile) {
        super(new BismuthClientWorld(), profile);
    }

    public String getModel() {
        return DefaultSkinHelper.getModel(this.getUuid());
    }

    public boolean isSpectator() {
        return false;
    }

    public PlayerListEntry getPlayerListEntry() {
        return new BismuthPlayerListEntry(this.getGameProfile());
    }
}
