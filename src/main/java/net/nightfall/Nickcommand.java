package net.nightfall;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Nickcommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("nick")

                // /nick set <name>
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("name", StringArgumentType.string())
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayer();

                                    String name = StringArgumentType.getString(context, "name");
                                    NickStorage.setNicknames(player.getUuid(), name);
                                    player.setCustomName(Text.of(name));
                                    player.setCustomNameVisible(false);

                                    source.sendFeedback(() -> Text.literal("Nickname set to: " + name), false);
                                    System.out.println("[NickCommand] Saved nickname for " + player.getName().getString() + " -> " + name);
                                    return 1;
                                })
                        )
                )

                // /nick reload
                .then(CommandManager.literal("reload")
                        .requires(source -> source.hasPermissionLevel(2)) // Only OPs
                        .executes(context -> {
                            NickStorage.load();
                            context.getSource().sendFeedback(() -> Text.literal("Nicknames reloaded"), false);
                            System.out.println("[NickCommand] Nicknames reloaded from disk.");
                            return 1;
                        })
                )
        );
    }
}
