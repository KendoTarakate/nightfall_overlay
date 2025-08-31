package net.nightfall.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nightfall.Nightfall;

public class NightfallItemGroup {
    public static final ItemGroup Nightfall = Registry.register(Registries.ITEM_GROUP,
            new Identifier(net.nightfall.Nightfall.MOD_ID, "nightfall"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.nightfall"))
                    .icon(() -> new ItemStack(NightfallItem.LOLIPOP_YUKI)).entries((displayContext, entries) -> {
                        entries.add(NightfallItem.LOLIPOP_YUKI);
                        entries.add(NightfallItem.LOLIPOP_PHING);
                        entries.add(NightfallItem.WINGLASS);
                    }).build());

    public static void registerItemGroups() {
        net.nightfall.Nightfall.LOGGER.info("Registering Item Groups for " + net.nightfall.Nightfall.MOD_ID);
    }
}
