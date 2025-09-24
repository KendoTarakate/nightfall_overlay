package net.nightfall;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.plo.voice.api.addon.annotation.Addon;
import su.plo.voice.api.server.PlasmoVoiceServer;

public class NightFallOverlay implements ModInitializer {
	public static final String MOD_ID = "nightfalloverlay";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private final NicknameVoiceAddon addon = new NicknameVoiceAddon();
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
			Nickcommand.register(commandDispatcher);
		}));
		NickStorage.load();
		PlasmoVoiceServer.getAddonsLoader().load(addon);
	}
}