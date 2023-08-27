package dev.twilightsociety.ijoiner.events;
import dev.twilightsociety.ijoiner.Settings;
import dev.twilightsociety.ijoiner.Storage;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent PJE) {
        PJE.joinMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(getText(PJE.getPlayer())));
    }

    public String getText(Player player) {
        String text = "";
        var random = new Random();

        if (Storage.hasPlayer(player)) {
            var message = Storage.getText(player);
            if (message.equalsIgnoreCase("default"))
                text = Settings.IMP.DEFAULTS.get(random.nextInt(Settings.IMP.DEFAULTS.size()));

            text = text.replace("%online%", getOnline()).replace("%player%", player.getName())
                    .replace("%prefix%", getPrefix(player))
                    .replace("%suffix%", getSuffix(player))
                    .replace("%rprefix%", getRPrefix(player))
                    .replace("%rsuffix%", getRSuffix(player));
            text = text.replace("%message%", message);
        } else {
            text = Settings.IMP.DEFAULTS.get(random.nextInt(Settings.IMP.DEFAULTS.size()));
            text = text.replace("%online%", getOnline()).replace("%player%", player.getName())
                    .replace("%prefix%", getPrefix(player))
                    .replace("%suffix%", getSuffix(player))
                    .replace("%rprefix%", getRPrefix(player))
                    .replace("%rsuffix%", getRSuffix(player));
        }
        return text;
    }

    public String getOnline() {
        return String.valueOf(Bukkit.getOnlinePlayers().size());
    }
    public String getPrefix(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%vault_prefix%");
    }
    public String getSuffix(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%vault_suffix%");
    }
    public String getRPrefix(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%vault_rankprefix%");
    }
    public String getRSuffix(Player player) {
        return PlaceholderAPI.setPlaceholders(player, "%vault_ranksuffix%");
    }
}
