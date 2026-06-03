package net.nightfall;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NightFallOverlay implements ModInitializer {
	public static final String MOD_ID = "nightfalloverlay";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		NickStorage.load();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				Nickcommand.register(dispatcher));

		// Plasmo Voice is optional. The addon is instantiated AND registered only
		// inside this guard: NicknameVoiceAddon implements a Plasmo Voice
		// interface, so even constructing it throws NoClassDefFoundError when
		// Plasmo Voice is absent. Keeping it here lets the tab-list/chat rename
		// features load regardless.
		try {
			su.plo.voice.api.server.PlasmoVoiceServer.getAddonsLoader().load(new NicknameVoiceAddon());
		} catch (Throwable t) {
			LOGGER.warn("Plasmo Voice not available, overlay addon disabled: {}", t.toString());
		}
	}
}
