package dev.twilightsociety.ijoiner.events;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent PJE) {
        PJE.joinMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(getMessage(PJE.getPlayer())));
    }

    public static String getMessage(Player player) {
        return "";
    }
}
