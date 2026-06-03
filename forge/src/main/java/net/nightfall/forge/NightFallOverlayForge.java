package net.nightfall.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.nightfall.NickStorage;
import net.nightfall.NicknameVoiceAddon;

/**
 * Forge entry point. Mirrors the Fabric {@code NightFallOverlay} initializer:
 * loads stored nicknames, registers the {@code /nick} command and hooks the
 * Plasmo Voice addon.
 */
@Mod(NightFallOverlayForge.MOD_ID)
public class NightFallOverlayForge {
    public static final String MOD_ID = "nightfalloverlay";

    public NightFallOverlayForge() {
        NickStorage.load();

        // Plasmo Voice is optional. The addon is instantiated AND registered only
        // inside this guard: NicknameVoiceAddon implements a Plasmo Voice
        // interface, so even constructing it throws NoClassDefFoundError when
        // Plasmo Voice is absent. Keeping it here lets the tab-list/chat rename
        // features load regardless.
        try {
            su.plo.voice.api.server.PlasmoVoiceServer.getAddonsLoader().load(new NicknameVoiceAddon());
        } catch (Throwable t) {
            System.out.println("[NightFallOverlay] Plasmo Voice not available, overlay addon disabled: " + t);
        }

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new NickEvents());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        NickCommandForge.register(event.getDispatcher());
    }
}
