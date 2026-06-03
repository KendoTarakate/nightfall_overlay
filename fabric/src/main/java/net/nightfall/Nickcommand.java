package net.nightfall;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Nickcommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("nick")

                // /nick set <name>
                .then(CommandManager.literal("set")
                        // greedyString captures the rest of the line (spaces allowed, no quotes needed):
                        //   /nick set Name Space More_Space
                        .then(CommandManager.argument("name", StringArgumentType.greedyString())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();
                                    if (player == null) {
                                        source.sendError(Text.literal("Only players can set a nickname."));
                                        return 0;
                                    }

                                    String name = StringArgumentType.getString(context, "name");
                                    NickStorage.setNicknames(player.getUuid(), name);
                                    refreshDisplayName(player);

                                    source.sendFeedback(() -> Text.literal("Nickname set to: " + name), false);
                                    return 1;
                                })
                        )
                )

                // /nick reload
                .then(CommandManager.literal("reload")
                        .requires(source -> source.hasPermissionLevel(2)) // Only OPs
                        .executes(context -> {
                            NickStorage.load();
                            // Push the freshly loaded names to every online player's tab entry.
                            for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
                                refreshDisplayName(player);
                            }
                            context.getSource().sendFeedback(() -> Text.literal("Nicknames reloaded"), false);
                            return 1;
                        })
                )

                // /nick look  (op only) — open a GUI of online players' nicknames + real usernames
                .then(CommandManager.literal("look")
                        .requires(source -> source.hasPermissionLevel(2)) // Only OPs
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();
                            if (player == null) {
                                context.getSource().sendError(Text.literal("Only players can open the nickname GUI."));
                                return 0;
                            }
                            NickLookGui.open(player);
                            return 1;
                        })
                )
        );
    }

    /**
     * Broadcasts an {@code UPDATE_DISPLAY_NAME} player-list packet so every
     * client refreshes this player's tab-list name immediately. The actual name
     * shown comes from the {@code getPlayerListName} mixin.
     */
    private static void refreshDisplayName(ServerPlayerEntity player) {
        if (player.getServer() == null) {
            return;
        }
        player.getServer().getPlayerManager().sendToAll(
                new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, player));
    }
}
