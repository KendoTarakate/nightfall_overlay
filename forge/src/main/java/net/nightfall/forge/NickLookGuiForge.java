package net.nightfall.forge;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nightfall.NickStorage;

import java.util.List;

/**
 * Server-side "/nick look" GUI for operators. Opens a vanilla 6-row chest
 * container (works on any vanilla client, no client mod needed) showing one
 * player head per online player: the item name is the player's nickname and the
 * lore shows their real Minecraft username. The container is view-only.
 */
public final class NickLookGuiForge {
    private static final int SLOTS = 54; // 6 rows

    private NickLookGuiForge() {}

    public static void open(ServerPlayer viewer) {
        MinecraftServer server = viewer.getServer();
        if (server == null) {
            return;
        }
        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        SimpleContainer container = new SimpleContainer(SLOTS);
        for (int i = 0; i < players.size() && i < SLOTS; i++) {
            container.setItem(i, createHead(players.get(i)));
        }

        viewer.openMenu(new SimpleMenuProvider(
                (syncId, playerInventory, p) -> new ViewOnlyMenu(syncId, playerInventory, container),
                Component.literal("Player Nicknames")
        ));
    }

    private static ItemStack createHead(ServerPlayer player) {
        String username = player.getGameProfile().getName();
        String nick = NickStorage.getNickname(player.getUUID());
        boolean hasNick = nick != null && !nick.isEmpty();

        ItemStack head = new ItemStack(Items.PLAYER_HEAD);

        // Embed the player's profile so the head renders their skin.
        CompoundTag tag = head.getOrCreateTag();
        tag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), player.getGameProfile()));

        Component name = hasNick
                ? Component.literal(nick).withStyle(s -> s.withColor(ChatFormatting.YELLOW).withItalic(false))
                : Component.literal(username + " (no nickname)").withStyle(s -> s.withColor(ChatFormatting.GRAY).withItalic(false));
        head.setHoverName(name);

        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf(Component.Serializer.toJson(line("Username: ", username))));
        lore.add(StringTag.valueOf(Component.Serializer.toJson(line("Nickname: ", hasNick ? nick : "none"))));
        head.getOrCreateTagElement("display").put("Lore", lore);

        return head;
    }

    private static Component line(String label, String value) {
        return Component.literal(label + value).withStyle(s -> s.withColor(ChatFormatting.GRAY).withItalic(false));
    }

    /** A 6-row chest menu that ignores all clicks (view-only). */
    private static final class ViewOnlyMenu extends ChestMenu {
        ViewOnlyMenu(int syncId, Inventory playerInventory, Container container) {
            super(MenuType.GENERIC_9x6, syncId, playerInventory, container, 6);
        }

        @Override
        public void clicked(int slotId, int button, ClickType clickType, Player player) {
            // view-only: ignore every interaction so nothing can be taken
        }
    }
}
