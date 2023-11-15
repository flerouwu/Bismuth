package dev.flero.bismuth.modules;

import dev.flero.bismuth.BismuthMod;
import dev.flero.bismuth.chat.Component;
import dev.flero.bismuth.config.ConfigHolder;
import dev.flero.bismuth.config.ConfigValue;
import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.RichPresence;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

@ConfigHolder(name = "modules/discordrpc")
public class DiscordRPC {
    private static final Logger logger = LogManager.getLogger("Bismuth/DiscordRPC");
    @ConfigValue
    public static boolean isEnabled = true;
    @ConfigValue
    public static long clientId = 1173864387870740490L;

    /**
     * This should always be called whenever the `clientId` property is changed.
     * Also called when the mod is initialized.
     */
    public static void updateClient() {
        DiscordIPC.start(clientId, () -> logger.info("Connected to Discord as " + DiscordIPC.getUser().username));
        setInfo("Loading...", null);
    }

    /**
     * Updates the Discord RPC with the provided state and details
     *
     * @param details translation key for details
     * @param state   translation key for state
     */
    public static void setInfo(String details, String state) {
        setInfo(Component.text(details), Component.text(state));
    }

    /**
     * Updates the Discord RPC with the provided state and details
     */
    public static void setInfo(@Nullable Component details, @Nullable Component state) {
        if (!isEnabled || !DiscordIPC.isConnected()) return;

        RichPresence presence = new RichPresence();
        if (details != null) presence.setDetails(details.getUnformatted());
        if (state != null) presence.setState(state.getUnformatted());
        presence.setStart(BismuthMod.startTime.getEpochSecond());

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getSession() != null) {
            Session session = client.getSession();
            presence.setSmallImage(String.format("https://cravatar.eu/helmavatar/%s/128.png", session.getUuid()), session.getUsername());
        }

        DiscordIPC.setActivity(presence);
    }

    /**
     * Shutdown the Discord RPC client.
     * This should be called when the mod is unloaded.
     */
    public static void shutdown() {
        DiscordIPC.stop();
    }
}
