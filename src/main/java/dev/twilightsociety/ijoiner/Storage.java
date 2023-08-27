package dev.twilightsociety.ijoiner;

import dev.twilightsociety.ijoiner.sql.iDatabase;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import static dev.twilightsociety.ijoiner.iJoiner.log;

public class Storage {
    public Storage(iJoiner pl) {
        var ST = Settings.IMP.STORAGE;
        if (ST.TYPE == Settings.STORAGES.MARIADB || ST.TYPE == Settings.STORAGES.MYSQL) {
            type = Settings.STORAGES.MYSQL;
            db = pl.database;
        } else if (ST.TYPE == Settings.STORAGES.LOCAL) {
            type = Settings.STORAGES.LOCAL;
            local = pl.localStorage;
            playersFile = iJoiner.getInstance().getDataFolder().toPath().resolve("players.yml").toFile();
        }
    }
    private static Settings.STORAGES type;
    private static Map<String, LocalConfigStorage.PLAYER> local;
    private static iDatabase db;
    private static File playersFile;

    public static boolean clear(String player, UUID uuid) {
        if (type == Settings.STORAGES.MYSQL) {
            if (db.update("UPDATE `%s` SET `text`='default' WHERE `player`='%s' OR `uuid`='%s';", db.getTName(), player, uuid)) {
                return true;
            } else {
                log("&7[&6&l\\&7] &cНе удалось очистить пользователский текст.");
                return false;
            }
        } else {
            local.remove(player);
            LocalConfigStorage.LOCAL.reload(playersFile);
            return true;
        }
    }
    public static boolean clear(Player player) {
        return clear(player.getName(), player.getUniqueId());
    }

    public static boolean hasPlayer(String player, UUID uuid) {
        if (type == Settings.STORAGES.MYSQL) {
            try {
                var result = db.query("SELECT * FROM `%s` WHERE `player`='%s' OR `uuid`='%s';", db.getTName(), player, uuid);
                return result.next();
            } catch (SQLException SQLE) {
                log("&7[&6&l\\&7] &cОшибка при получении возможности редактирования польз-ого текста: &f" + SQLE.getMessage());
                return false;
            }
        } else {
            return local.containsKey(player);
        }
    }
    public static boolean hasPlayer(Player player) {
        return hasPlayer(player.getName(), player.getUniqueId());
    }

    public static boolean addPlayer(String text, String player, UUID uuid) {
        if (type == Settings.STORAGES.MYSQL) {
            if (db.update("INSERT INTO `%s`(`text`, `player`, `uuid`) VALUES ('%s', '%s', '%s');", db.getTName(), text, player, uuid))
                return true;
            else {
                log("&7[&6&l\\&7] &cНе удалось добавить пользовательский текст.");
                return false;
            }
        } else {
            LocalConfigStorage.Manager.addPlayer(player, uuid.toString(), text);
            LocalConfigStorage.LOCAL.reload(playersFile);
            return true;
        }
    }
    public static boolean addPlayer(String text, Player player) {
        return addPlayer(text, player.getName(), player.getUniqueId());
    }

    public static boolean setText(String text, String player, UUID uuid) {
        if (type == Settings.STORAGES.MYSQL) {
            if (db.update("UPDATE `%s` SET `text`='%s' WHERE `player`='%s' OR `uuid`='%s';", db.getTName(), text, player, uuid))
                return true;
            else {
                log("&7[&6&l\\&7] &cНе удалось установить пользовательский текст.");
                return false;
            }
        } else {
            local.get(player).TEXT = text;
            return true;
        }
    }
    public static boolean setText(String text, Player player) {
        return setText(text, player.getName(), player.getUniqueId());
    }

    public static String getText(String player, UUID uuid) {
        if (type == Settings.STORAGES.MYSQL) {
            try {
                ResultSet set = db.query("SELECT `text` FROM `%s` WHERE `player`='%s' OR `uuid`='%s';", db.getTName(), player, uuid);
                if (set.next()) {
                    return set.getString("text");
                } else return null;
            } catch (SQLException SQLE) {
                log("&7[&6&l\\&7] &cНе удалось получить польз. текст: &f" + SQLE.getMessage());
                return null;
            }
        } else {
            if (local.containsKey(player)) {
                var p = local.get(player);
                if (p.UUID.equalsIgnoreCase(uuid.toString()))
                    return p.TEXT;
                else return null;
            } else return null;
        }
    }
    public static String getText(Player player) {
        return getText(player.getName(), player.getUniqueId());
    }
}
