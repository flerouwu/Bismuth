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

public class PaperDollWidget extends DrawableHelper implements Widget {
    private static final Logger logger = LogManager.getLogger("Bismuth/PaperDollWidget");
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private GameProfile profile;
    private BismuthOtherClientPlayerEntity dummy;
    private final boolean followMouse;

    public PaperDollWidget(boolean followMouse) {
        this.followMouse = followMouse;

        profile = client.getSession().getProfile();
        if (profile == null) {
            logger.info("No profile found, creating dummy profile and requesting secure profile information.");
            profile = new GameProfile(UUID.fromString(client.getSession().getUuid()), client.getSession().getUsername());
            MinecraftClient.getInstance().getSessionService().fillProfileProperties(profile, true);
        }
    }

    public LivingEntity getDummy() {
        if (client.player != null) return client.player;
        if (dummy == null) dummy = new BismuthOtherClientPlayerEntity(profile);
        return dummy;
    }

    @Override
    public void render(int mouseX, int mouseY, int x, int y, int width, int height) {
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50);
        GlStateManager.scale(1, 1, -1);

        GlStateManager.translate((float) width / 2, height, 0);
        float scale = 40.0f;
        GlStateManager.scale(scale, scale, scale);

        GlStateManager.rotate(180.0f, 2.0f, 0.0f, 0.0f);
        DiffuseLighting.enableNormally();
        EntityRenderDispatcher dispatcher = client.getEntityRenderManager();
        GlStateManager.rotate(0.0f, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) Math.atan((mouseY - y - (double) height / 3) / 40.0F) * 10.0F, 1.0F, 0.0F, 0.0F);

        // Render dummy
        LivingEntity entity = getDummy();
        if (followMouse) {
            entity.bodyYaw = -((float) Math.atan((mouseX - x - (double) width / 2)) / 20.0f) * 120.0F;
            entity.yaw = -((float) Math.atan((mouseX - x - (double) width / 2) / 40.0f) * 40.0F);
            entity.pitch = (float) Math.atan((mouseY - y - (double) height / 3) / 40.0f) * 20.0F;
            entity.headYaw = entity.yaw;
            entity.prevHeadYaw = entity.yaw;
        }

        dispatcher.setRenderShadows(false);
        dispatcher.method_6913(entity, 0.0d, 0.0d, 0.0d, 0.0f, 1.0f, false);
        dispatcher.setRenderShadows(true);
        GlStateManager.popMatrix();

        DiffuseLighting.disable();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.lightmapTextureUnit);
        GlStateManager.disableDepthTest();
        GlStateManager.activeTexture(GLX.textureUnit);
    }

    @Override
    public Size getMinimumSize() {
        return new Size(50, 50);
    }
}
