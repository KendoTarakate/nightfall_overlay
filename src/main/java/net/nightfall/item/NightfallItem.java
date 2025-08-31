package net.nightfall.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nightfall.Nightfall;

public class NightfallItem {
    //Eternal Line and Re Lesson
    public static final Item LOLIPOP_YUKI = registerItem("lolipop_yuki", new Item(new FabricItemSettings().maxCount(1).maxDamage(-1).equipmentSlot(itemStack -> EquipmentSlot.HEAD)));
    public static final Item WINGLASS = registerItem("win_glass", new Item(new FabricItemSettings().maxCount(1).maxDamage(-1).equipmentSlot(itemStack -> EquipmentSlot.HEAD)));
    public static final Item LOLIPOP_PHING = registerItem("lolipop_phing", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));


    //SYNX PROJECT MODEL
    public static final Item SCALE_FLASHLIGHT = registerItem("scaleflashlight", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    public static final Item NETHERITE_COIN = registerItem("netherite_coin", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD)));
    public static final Item DAIMOND_COIN = registerItem("diamond_coin", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD)));
    public static final Item HEADPHONE = registerItem("headphone", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    public static final Item MICROPHONE_SYNX = registerItem("microphone_synx", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    public static final Item CAPSULE = registerItem("capsule", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));



    public static final Item TEST = registerItem("test", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    //SYNX PROJECT PLAYER MODEL
    public static final Item ZENITH_MICROPHONE = registerItem("zenith_microphone", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    public static final Item ANGELY_MICROPHONE = registerItem("angely_microphone", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    public static final Item ANGELY_HEADPHONE = registerItem("angely_headphone", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    public static final Item BURI = registerItem("buri", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));
    public static final Item POCKY = registerItem("pocky", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));

    //SYNX instrument
    public static final Item GUITAR = registerItem("guitar", new Item(new FabricItemSettings().equipmentSlot(itemStack -> EquipmentSlot.HEAD).maxCount(1).maxDamage(-1)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Nightfall.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Nightfall.LOGGER.info("Registering Mod Items for " + Nightfall.MOD_ID);

    }
}


