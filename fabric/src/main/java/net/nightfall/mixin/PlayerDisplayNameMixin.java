package net.nightfall.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.nightfall.NickStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes a server player's display name resolve to their configured nickname.
 * {@code getDisplayName()} is what vanilla uses to render the chat sender name,
 * death messages, {@code /tell} feedback, etc., so overriding it renames the
 * player in chat in real time (the value is read fresh for every message).
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerDisplayNameMixin {

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    private void nightfall$applyNickname(CallbackInfoReturnable<Text> cir) {
        if (!((Object) this instanceof ServerPlayerEntity player)) {
            return; // only rename server-side player entities
        }
        String nick = NickStorage.getNickname(player.getUuid());
        if (nick != null && !nick.isEmpty()) {
            // Decorate with the player's team (color/prefix/suffix), matching how
            // vanilla formats the real name, so a colored team keeps its color.
            cir.setReturnValue(Team.decorateName(player.getScoreboardTeam(), Text.literal(nick)));
        }
    }
}
