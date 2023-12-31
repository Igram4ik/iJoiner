package dev.twilightsociety.ijoiner.commands;

import dev.twilightsociety.ijoiner.Settings;
import dev.twilightsociety.ijoiner.Storage;
import dev.twilightsociety.ijoiner.iJoiner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
    public Commands() {

        var MSGS = Settings.IMP.MESSAGES;
        reload = pLC(MSGS.RELOAD);
        reloadFailed = pLC(MSGS.RELOAD_FAILED);
        unknown = pLC(MSGS.UNKNOWN_COMMAND);
        noPerm = pLC(MSGS.NOPERM);
        noArgs = pLC(MSGS.NO_ARGS);
        cleared = pLC(MSGS.CLEARED);
        clearFailed = pLC(MSGS.CLEAR_FAILED);
        setted = pLC(MSGS.SETTED);
        setFailed = pLC(MSGS.SET_FAILED);
        notplayer = pLC(MSGS.NOT_FOR_CONSOLE);
        tooMuch = pLC(MSGS.TOO_MUCH);
    }
    private final Component reload;
    private final Component reloadFailed;
    private final Component unknown;
    public final Component notplayer;
    private final Component noPerm;
    private final Component noArgs;
    private final Component cleared;
    private final Component clearFailed;
    private final Component setted;
    private final Component setFailed;
    private final Component tooMuch;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(unknown);
            return true;
        }

        //
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("ijoiner.reload") || sender.isOp()) {
                if (iJoiner.getInstance().reload())
                    sender.sendMessage(reload);
                else
                    sender.sendMessage(reloadFailed);
            } else {
                sender.sendMessage(noPerm);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("clear")) {
                if (sender instanceof Player player) {
                    if (player.hasPermission("ijoiner.clear") || player.isOp()) {
                        if (Storage.clear(player))
                            player.sendMessage(cleared);
                        else
                            player.sendMessage(clearFailed);
                    } else sender.sendMessage(noPerm);
                } else sender.sendMessage(notplayer);
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length == 1) {
                sender.sendMessage(noArgs);
                return true;
            }
            if (sender instanceof Player player) {
                if (player.hasPermission("ijoiner.set") || player.isOp()) {
                    StringBuilder text = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        text.append(args[i]);
                        text.append(" ");
                    }
                    if (text.length() >= 35) {
                        player.sendMessage(tooMuch);
                        return true;
                    }
                    if (Storage.hasPlayer(player)) {
                        if (Storage.setText(text.toString(), player))
                            sender.sendMessage(setted);
                        else
                            sender.sendMessage(setFailed);
                    } else {
                        if (Storage.addPlayer(text.toString(), player))
                            sender.sendMessage(setted);
                        else
                            sender.sendMessage(setFailed);
                    }
                } else sender.sendMessage(noPerm);
            } else sender.sendMessage(noPerm);
        } else sender.sendMessage(unknown);
        //
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            var tab = new ArrayList<String>();
            if (args.length == 1) {
                if (sender.hasPermission("ijoiner.reload") || sender.isOp())
                    tab.add("reload");
                if (sender instanceof Player) {
                    if (sender.hasPermission("ijoiner.set") || sender.isOp())
                        tab.add("set");
                    if (sender.hasPermission("ijoiner.clear") || sender.isOp())
                        tab.add("clear");
                }
                return tab;
            }
        }
        return new ArrayList<>();
    }

    public static Component pLC(String str) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(str);
    }
}
