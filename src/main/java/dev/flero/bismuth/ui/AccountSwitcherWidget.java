package dev.flero.bismuth.ui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.entity.player.BismuthOtherClientPlayerEntity;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class AccountSwitcherWidget extends DrawableHelper {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Logger logger = LogManager.getLogger("Bismuth/PlayerModalWidget");

    private GameProfile profile;

    public AccountSwitcherWidget() {
        profile = client.getSession().getProfile();
        if (profile.getId() == null) {
            profile = new GameProfile(UUID.randomUUID(), client.getSession().getUsername());
        }
    }

    public LivingEntity getDummy() {
        if (client.player != null) return client.player;
        return new BismuthOtherClientPlayerEntity(profile);
    }

    public void render(int mouseX, int mouseY, int x, int y, int width, int height) {
        fill(x, y, x + width, y + height - 16, 0x80000000);

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50);
        GlStateManager.scale(1, 1, -1);

        GlStateManager.translate((float) width / 2, height - 20, 0);
        float scale = 40.0f;
        GlStateManager.scale(scale, scale, scale);

        GlStateManager.rotate(180.0f, 2.0f, 0.0f, 0.0f);
        DiffuseLighting.enableNormally();
        EntityRenderDispatcher dispatcher = client.getEntityRenderManager();
        dispatcher.setYaw(0);
        dispatcher.pitch = 0;
        dispatcher.setRenderShadows(false);

        // Render dummy
        dispatcher.method_6913(getDummy(), 0.0d, 0.0d, 0.0d, 0.0f, 0.0f, true);

        dispatcher.setRenderShadows(true);
        GlStateManager.popMatrix();

        DiffuseLighting.disable();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.lightmapTextureUnit);
        GlStateManager.disableDepthTest();
        GlStateManager.activeTexture(GLX.textureUnit);

        // Draw Button
        ButtonWidget button = new ButtonWidget(client.getSession().getUsername(), () -> logger.info("Clicked!"));
        button.render(mouseX, mouseY, x, y + 89, width, 15);
    }
}
