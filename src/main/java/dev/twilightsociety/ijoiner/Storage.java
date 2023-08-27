package dev.twilightsociety.ijoiner;

import dev.twilightsociety.ijoiner.sql.iDatabase;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static dev.twilightsociety.ijoiner.iJoiner.log;

public class Storage {
    private final iJoiner plugin;
    public Storage(iJoiner pl) {
        plugin = pl;
        var ST = Settings.IMP.STORAGE;
        if (ST.TYPE == Settings.STORAGES.MARIADB || ST.TYPE == Settings.STORAGES.MYSQL) {
            type = Settings.STORAGES.MYSQL;
            db = pl.database;
        } else if (ST.TYPE == Settings.STORAGES.LOCAL) {
            type = Settings.STORAGES.LOCAL;
            local = LocalConfigStorage.LOCAL;
        }
    }
    private static Settings.STORAGES type;
    private static LocalConfigStorage local;
    private static iDatabase db;

    public static boolean clear(String player, UUID uuid) {
        log(String.valueOf(uuid));
        if (db.update("UPDATE `%s` SET `text`='default' WHERE `uuid`, `player` LIKE `%s`, `%s`;", db.getTName(), player, uuid)) {
            return true;
        } else {
            log("&7[&6&l\\&7] &cНе удалось очистить пользователский текст.");
            return false;
        }
    }
    public static boolean clear(Player player) {
        return clear(player.getName(), player.getUniqueId());
    }

    public static boolean hasPlayer(String player, UUID uuid) {
        try {
            var result = db.query("SELECT * FROM `%s` WHERE `player`, `uuid` SET '%s', '%s';", db.getTName(), player, uuid);
            return result.next();
        } catch (SQLException SQLE) {
            log("&7[&6&l\\&7] &cОшибка при получении возможности редактирования польз-ого текста: &f" + SQLE.getMessage());
            return false;
        }
    }
    public static boolean hasPlayer(Player player) {
        return hasPlayer(player.getName(), player.getUniqueId());
    }

    public static boolean addPlayer(String text, String player, UUID uuid) {
        if (db.update("INSERT INTO `%s`(`text`, `player`, `uuid`) VALUES ('%s', '%s', '%s');", db.getTName(), text, player, uuid))
            return true;
        else {
            log("&7[&6&l\\&7] &cНе удалось добавить пользовательский текст.");
            return false;
        }
    }
    public static boolean addPlayer(String text, Player player) {
        return addPlayer(text, player.getName(), player.getUniqueId());
    }

    public static boolean setText(String text, String player, UUID uuid) {
        if (db.update("UPDATE `%s` SET `text`='%s' WHERE `player`, `uuid` LIKE '%s', '%s';", db.getTName(), text, player, uuid))
            return true;
        else {
            log("&7[&6&l\\&7] &cНе удалось установить пользовательский текст.");
            return false;
        }
    }
    public static boolean setText(String text, Player player) {
        return setText(text, player.getName(), player.getUniqueId());
    }

    public static String getText(String player, UUID uuid) {
        try {
            ResultSet set = db.query("SELECT `text` FROM `%s` WHERE `player`, `uuid` LIKE '%s', '%s';", db.getTName(), player, String.valueOf(uuid));
            if (set.next()) {
                return set.getString("text");
            } else return null;
        } catch (SQLException SQLE) {
            log("&7[&6&l\\&7] &cНе удалось получить польз. текст: &f" + SQLE.getMessage());
            return null;
        }
    }
    public static String getText(Player player) {
        return getText(player.getName(), player.getUniqueId());
    }
}
