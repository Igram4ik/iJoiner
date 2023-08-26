package dev.twilightsociety.ijoiner.commands;

import dev.twilightsociety.ijoiner.Settings;
import dev.twilightsociety.ijoiner.iJoiner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
    public Commands(iJoiner plugin) {
        this.plugin = plugin;

        reload = pLC(Settings.IMP.MESSAGES.RELOAD);
        reloadFailed = pLC(Settings.IMP.MESSAGES.RELOAD_FAILED);
        unknown = pLC(Settings.IMP.MESSAGES.UNKNOWN_COMMAND);
        noPerm = pLC(Settings.IMP.MESSAGES.NOPERM);
        noArgs = pLC(Settings.IMP.STORAGE);
    }
    private final iJoiner plugin;
    private final Component reload;
    private final Component reloadFailed;
    private final Component unknown;
    private final Component noPerm;
    private final Component noArgs;
    private final Component cleared;
    private final Component clearFailed;
    private final Component setted;
    private final Component setFailed;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(unknown);
            return true;
        }
        if (!sender.hasPermission("ijoiner")) {
            sender.sendMessage(noPerm);
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            var tab = new ArrayList<String>();
            if (args.length == 1) {
                if (sender.hasPermission("ijoiner.custom")) {

                }
            }
        }
        return new ArrayList<>();
    }

    public static Component pLC(String str) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(str);
    }
}
