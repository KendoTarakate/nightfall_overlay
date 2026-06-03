package net.nightfall;

import su.plo.voice.api.addon.AddonInitializer;
import su.plo.voice.api.addon.InjectPlasmoVoice;
import su.plo.voice.api.addon.annotation.Addon;
import su.plo.voice.api.event.EventSubscribe;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.event.player.PlayerInfoCreateEvent;
import su.plo.voice.proto.data.player.VoicePlayerInfo;

import java.util.UUID;

/**
 * Plasmo Voice addon that swaps the name shown in the voice-chat overlay for the
 * player's configured nickname. Uses only the Plasmo Voice API (no Minecraft or
 * loader classes), so it is shared between the Fabric and Forge builds.
 */
@Addon(
        // An addon id must start with a lowercase letter and may contain only lowercase letters, digits, hyphens, and underscores.
        // It should be between 4 and 32 characters long.
        id = "pv-addon-overlay",
        name = "Overlay Addon",
        version = "1.0.0",
        authors = {"Kendo"}
)
public final class NicknameVoiceAddon implements AddonInitializer {

    @InjectPlasmoVoice
    private PlasmoVoiceServer voiceServer;

    @Override
    public void onAddonInitialize() {
        NickStorage.load();
        // Refresh the voice overlay live whenever a nickname changes.
        NickStorage.setChangeListener(this::refreshOverlay);
        System.out.println("[NightFallOverlay] Plasmo Voice addon initialized");
    }

    @Override
    public void onAddonShutdown() {
        NickStorage.setChangeListener(null);
        System.out.println("[NightFallOverlay] Plasmo Voice addon shut down");
    }

    /**
     * Re-broadcasts a player's Plasmo Voice info so the overlay nickname updates
     * immediately, instead of only after the player reconnects. Plasmo Voice
     * normally builds the overlay name once (in {@link #onPlayerInfoCreate}) when
     * the player joins the voice session; broadcasting an info update re-fires
     * that event with the new nickname.
     */
    private void refreshOverlay(UUID uuid) {
        PlasmoVoiceServer server = this.voiceServer;
        if (server == null) {
            return;
        }
        server.getPlayerManager().getPlayerById(uuid).ifPresent(player ->
                server.getTcpPacketManager().broadcastPlayerInfoUpdate(player));
    }

    @EventSubscribe
    public void onPlayerInfoCreate(PlayerInfoCreateEvent event) {
        VoicePlayerInfo original = event.getVoicePlayerInfo();
        UUID uuid = original.getPlayerId();

        // Get nickname from shared storage
        String nickname = NickStorage.getNickname(uuid);
        if (nickname == null || nickname.isEmpty()) {
            return; // no nickname set: leave the original overlay name untouched
        }

        VoicePlayerInfo modified = new VoicePlayerInfo(
                uuid,
                nickname, // show nickname in overlay
                original.isMuted(),
                original.isVoiceDisabled(),
                original.isMicrophoneMuted()
        );

        event.setVoicePlayerInfo(modified);
    }
}
