package net.nightfall.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nightfall.Nightfall;
import net.nightfall.block.NightfallBlock;

public class NightfallISYNXtemGroup {
    public static ItemGroup SYNX;

    public static void registerItemSYNXGroups() {
        SYNX = Registry.register(Registries.ITEM_GROUP,
                new Identifier(Nightfall.MOD_ID, "synx"),
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemgroup.synx"))
                        .icon(() -> new ItemStack(NightfallItem.ZENITH_MICROPHONE))
                        .entries((displayContext, entries) -> {
                            //Coin
                            entries.add(NightfallItem.NETHERITE_COIN);
                            entries.add(NightfallItem.DAIMOND_COIN);
                            //Mic-Headphone
                            entries.add(NightfallBlock.MIC);
                            entries.add(NightfallItem.MICROPHONE_SYNX);
                            entries.add(NightfallItem.HEADPHONE);
                            entries.add(NightfallItem.ZENITH_MICROPHONE);
                            entries.add(NightfallItem.ANGELY_MICROPHONE);
                            entries.add(NightfallItem.ANGELY_HEADPHONE);
                            //Normal Item
                            entries.add(NightfallItem.SCALE_FLASHLIGHT);
                            entries.add(NightfallItem.CAPSULE);
                            entries.add(NightfallItem.POCKY);
                            entries.add(NightfallItem.BURI);
                            //instruments
                            entries.add(NightfallItem.GUITAR);
                        })
                        .build()
        );

        Nightfall.LOGGER.info("Registered Item SYNX Group for " + Nightfall.MOD_ID);
    }
}
