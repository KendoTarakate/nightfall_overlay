package net.nightfall.forge;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.nightfall.NickStorage;

/**
 * Forge ({@code /nick}) command, written against Mojang mappings. Functional
 * twin of the Fabric {@code Nickcommand}.
 */
public class NickCommandForge {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nick")

                // /nick set <name>
                .then(Commands.literal("set")
                        // greedyString captures the rest of the line (spaces allowed, no quotes needed):
                        //   /nick set Name Space More_Space
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerPlayer player = source.getPlayerOrException();

                                    String name = StringArgumentType.getString(context, "name");
                                    NickStorage.setNicknames(player.getUUID(), name);
                                    refreshDisplayName(player);

                                    source.sendSuccess(() -> Component.literal("Nickname set to: " + name), false);
                                    return 1;
                                })
                        )
                )

                // /nick reload
                .then(Commands.literal("reload")
                        .requires(source -> source.hasPermission(2)) // Only OPs
                        .executes(context -> {
                            NickStorage.load();
                            MinecraftServer server = context.getSource().getServer();
                            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                                refreshDisplayName(player);
                            }
                            context.getSource().sendSuccess(() -> Component.literal("Nicknames reloaded"), false);
                            return 1;
                        })
                )

                // /nick look  (op only) — open a GUI of online players' nicknames + real usernames
                .then(Commands.literal("look")
                        .requires(source -> source.hasPermission(2)) // Only OPs
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            NickLookGuiForge.open(player);
                            return 1;
                        })
                )
        );
    }

    /**
     * Invalidates Forge's cached display/tab names so the {@link NickEvents}
     * hooks re-run, then broadcasts an {@code UPDATE_DISPLAY_NAME} packet so
     * every client refreshes this player's tab-list name immediately.
     */
    private static void refreshDisplayName(ServerPlayer player) {
        player.refreshDisplayName();
        player.refreshTabListName();

        MinecraftServer server = player.getServer();
        if (server != null) {
            server.getPlayerList().broadcastAll(
                    new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, player));
        }
    }
}
