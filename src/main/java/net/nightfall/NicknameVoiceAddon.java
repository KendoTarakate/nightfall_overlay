
package net.nightfall;


import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import su.plo.voice.api.addon.AddonInitializer;
import su.plo.voice.api.addon.InjectPlasmoVoice;
import su.plo.voice.api.addon.annotation.Addon;
import su.plo.voice.api.client.connection.ServerInfo;
import su.plo.voice.api.event.EventSubscribe;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.audio.source.AudioSource;
import su.plo.voice.api.server.audio.source.ServerPlayerSource;
import su.plo.voice.api.server.event.audio.source.ServerSourceCreatedEvent;
import su.plo.voice.api.server.event.player.PlayerInfoCreateEvent;
import su.plo.voice.proto.data.player.VoicePlayerInfo;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

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

        UUID fake = UUID.fromString("00000000-0000-0000-0000-000000000000");
        NickStorage.setNicknames(fake, "TestNick");

        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            Nickcommand.register(commandDispatcher);
        }));



        System.out.println("Addon initialized");
    }

    @Override
    public void onAddonShutdown() {
        System.out.println("Addon shut down");
    }
    @EventSubscribe

    public void onPlayerInfoCreate(PlayerInfoCreateEvent event) {
        VoicePlayerInfo original = event.getVoicePlayerInfo();
        UUID uuid = original.getPlayerId();

        // Get nickname from client-synced storage
        String nickname = NickStorage.getNickname(uuid);
        if (nickname == null || nickname.isEmpty()) {
            nickname = ""; // fallback: hide name
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


    public void onSourceCreated(ServerSourceCreatedEvent event) {
        AudioSource source = event.getSource();

        if (source instanceof ServerPlayerSource playerSource) {
            UUID uuid = playerSource.getPlayer().getInstance().getUuid();
            String nickname = NickStorage.getNickname(uuid);

            if (nickname != null && !nickname.isEmpty()) {
                System.out.println("Setting overlay name to:" + nickname);
                playerSource.setName(nickname);
            }
        }
    }
}
