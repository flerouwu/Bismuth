package net.minecraft.client.entity.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.BismuthPlayerListEntry;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.world.BismuthClientWorld;
import net.minecraft.entity.player.OtherClientPlayerEntity;

import java.util.Optional;

public class BismuthOtherClientPlayerEntity extends OtherClientPlayerEntity {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private BismuthPlayerListEntry listEntry;

    public BismuthOtherClientPlayerEntity(GameProfile profile) {
        super(new BismuthClientWorld(), profile);
    }

    @Override
    public String getModel() {
        return getPlayerListEntry().getModel();
    }

    @Override
    public boolean isPartVisible(PlayerModelPart part) {
        // Never show the cape, because it's buggy.
        if (part == PlayerModelPart.CAPE) return false;

        if (client.player != null) return client.player.isPartVisible(part);
        else {
            Optional<PlayerModelPart> optionPart = client.options.getEnabledPlayerModelParts().stream().filter(part::equals).findAny();
            return optionPart.isPresent();
        }
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public PlayerListEntry getPlayerListEntry() {
        if (listEntry == null) listEntry = new BismuthPlayerListEntry(this.getGameProfile());
        return listEntry;
    }
}
