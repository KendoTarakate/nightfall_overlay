package net.nightfall;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Server-side "/nick look" GUI for operators. Opens a vanilla 6-row chest
 * container (works on any vanilla client, no client mod needed) showing one
 * player head per online player: the item name is the player's nickname and the
 * lore shows their real Minecraft username. The container is view-only.
 */
public final class NickLookGui {
    private static final int SLOTS = 54; // 6 rows

    private NickLookGui() {}

    public static void open(ServerPlayerEntity viewer) {
        MinecraftServer server = viewer.getServer();
        if (server == null) {
            return;
        }
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();

        SimpleInventory inventory = new SimpleInventory(SLOTS);
        for (int i = 0; i < players.size() && i < SLOTS; i++) {
            inventory.setStack(i, createHead(players.get(i)));
        }

        viewer.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, playerInventory, p) -> new ViewOnlyHandler(syncId, playerInventory, inventory),
                Text.literal("Player Nicknames")
        ));
    }

    private static ItemStack createHead(ServerPlayerEntity player) {
        String username = player.getGameProfile().getName();
        String nick = NickStorage.getNickname(player.getUuid());
        boolean hasNick = nick != null && !nick.isEmpty();

        ItemStack head = new ItemStack(Items.PLAYER_HEAD);

        // Embed the player's profile so the head renders their skin.
        NbtCompound nbt = head.getOrCreateNbt();
        nbt.put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), player.getGameProfile()));

        Text name = hasNick
                ? Text.literal(nick).formatted(Formatting.YELLOW).styled(s -> s.withItalic(false))
                : Text.literal(username + " (no nickname)").formatted(Formatting.GRAY).styled(s -> s.withItalic(false));
        head.setCustomName(name);

        NbtList lore = new NbtList();
        lore.add(NbtString.of(Text.Serializer.toJson(line("Username: ", username))));
        lore.add(NbtString.of(Text.Serializer.toJson(line("Nickname: ", hasNick ? nick : "none"))));
        head.getOrCreateSubNbt("display").put("Lore", lore);

        return head;
    }

    private static Text line(String label, String value) {
        return Text.literal(label).formatted(Formatting.GRAY)
                .append(Text.literal(value).formatted(Formatting.WHITE))
                .styled(s -> s.withItalic(false));
    }

    /** A 6-row chest handler that ignores all clicks (view-only). */
    private static final class ViewOnlyHandler extends GenericContainerScreenHandler {
        ViewOnlyHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
            super(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, inventory, 6);
        }

        @Override
        public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
            // view-only: ignore every interaction so nothing can be taken
        }
    }
}
