package net.nightfall;

import net.fabricmc.api.ModInitializer;

import net.nightfall.block.NightfallBlock;
import net.nightfall.item.NightfallISYNXtemGroup;
import net.nightfall.item.NightfallItem;
import net.nightfall.item.NightfallItemGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nightfall implements ModInitializer {
	public static final String MOD_ID = "nightfall";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		NightfallItem.registerModItems();
		NightfallItemGroup.registerItemGroups();
		NightfallISYNXtemGroup.registerItemSYNXGroups();
		NightfallBlock.registerModBlocks();

		LOGGER.info("Hello Fabric world!");
	}
}