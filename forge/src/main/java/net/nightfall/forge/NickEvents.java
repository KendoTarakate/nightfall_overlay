package net.nightfall.forge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.nightfall.NickStorage;

/**
 * Forge name-rendering hooks. {@link PlayerEvent.NameFormat} drives the display
 * name used in chat (and elsewhere); {@link PlayerEvent.TabListNameFormat}
 * drives the tab-list / "scoreboard" name. Both results are cached by Forge and
 * invalidated via {@code refreshDisplayName()} / {@code refreshTabListName()}
 * when a nickname changes (see {@link NickCommandForge}), giving real-time
 * updates.
 */
public class NickEvents {

    @SubscribeEvent
    public void onNameFormat(PlayerEvent.NameFormat event) {
        Component nick = nickname(event.getEntity());
        if (nick != null) {
            event.setDisplayname(nick);
        }
    }

    @SubscribeEvent
    public void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        Component nick = nickname(event.getEntity());
        if (nick != null) {
            event.setDisplayName(nick);
        }
    }

    /** Returns the team-decorated nickname for the player, or null if none set. */
    private static Component nickname(Player player) {
        String nick = NickStorage.getNickname(player.getUUID());
        if (nick == null || nick.isEmpty()) {
            return null;
        }
        Component name = Component.literal(nick);
        // Decorate with the player's team (color/prefix/suffix) like vanilla does
        // for the real name, so a colored team keeps its color.
        return player.getTeam() != null ? PlayerTeam.formatNameForTeam(player.getTeam(), name) : name;
    }
}
