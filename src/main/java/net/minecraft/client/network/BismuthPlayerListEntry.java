package net.minecraft.client.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Identifier;

public class BismuthPlayerListEntry extends PlayerListEntry {
    private boolean texturesLoaded;
    // Why is this called elytra texture? I don't know, but it's the cape texture.
    private Identifier elytraTexture;
    private Identifier skinTexture;

    public BismuthPlayerListEntry(GameProfile gameProfile) {
        super(gameProfile);
        this.loadTextures();
    }

    @Override
    public Team getScoreboardTeam() {
        return null;
    }

    @Override
    public Identifier getSkinTexture() {
        if (this.skinTexture == null) this.loadTextures();
        return this.skinTexture != null ? this.skinTexture : DefaultSkinHelper.getTexture(this.getProfile().getId());
    }

    @Override
    public Identifier getElytraTexture() {
        if (this.elytraTexture == null) this.loadTextures();
        return this.elytraTexture;
    }

    @Override
    protected void loadTextures() {
        synchronized (this) {
            if (!this.texturesLoaded) {
                this.texturesLoaded = true;
                MinecraftClient.getInstance().getSkinProvider().loadProfileSkin(this.getProfile(), (type, identifier, minecraftProfileTexture) -> {
                    switch (type) {
                        case SKIN:
                            BismuthPlayerListEntry.this.skinTexture = identifier;
                            BismuthPlayerListEntry.this.model = minecraftProfileTexture.getMetadata("model");
                            if (BismuthPlayerListEntry.this.model == null) {
                                BismuthPlayerListEntry.this.model = "default";
                            }
                            break;
                        case CAPE:
                            BismuthPlayerListEntry.this.elytraTexture = identifier;
                    }

                }, false);
            }

        }
    }
}
