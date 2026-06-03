package net.nightfall.mixin;

import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.nightfall.NickStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes the player-list (tab list / "scoreboard") name resolve to the player's
 * nickname. Vanilla builds the {@code PlayerListS2CPacket} display-name entry
 * from {@code getPlayerListName()}, so overriding it renames the player on the
 * tab list. A broadcast of {@code UPDATE_DISPLAY_NAME} (see the command) pushes
 * the change to every client immediately.
 */
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerListNameMixin {

    @Inject(method = "getPlayerListName", at = @At("HEAD"), cancellable = true)
    private void nightfall$applyListName(CallbackInfoReturnable<Text> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        String nick = NickStorage.getNickname(player.getUuid());
        if (nick != null && !nick.isEmpty()) {
            // Keep the team color/prefix/suffix on the tab-list name.
            cir.setReturnValue(Team.decorateName(player.getScoreboardTeam(), Text.literal(nick)));
        }
    }
}
