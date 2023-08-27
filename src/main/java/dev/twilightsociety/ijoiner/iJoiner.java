package dev.twilightsociety.ijoiner;

import dev.twilightsociety.ijoiner.commands.Commands;
import dev.twilightsociety.ijoiner.events.Listener;
import dev.twilightsociety.ijoiner.sql.iDatabase;
import org.apache.commons.lang.time.StopWatch;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class iJoiner extends JavaPlugin {
    public iDatabase database;
    public Map<String, LocalConfigStorage.PLAYER> localStorage;

    public static iJoiner getInstance() {
        return instance;
    }
    private static iJoiner instance;

    private final StopWatch stopWatch = new StopWatch();
    @Override
    public void onEnable() {
        instance = this;
        log("&7[&6&l\\&7] &6iJoiner запущен." + " &7[Работа его будет асинхронна и тут же окончена для ядра.]");
        CompletableFuture.runAsync(this::reload)
                .exceptionallyAsync((t) -> {
                    log("&7[&6&l\\&7] &cПроизошла ошибка при загрузке iJoiner: &f" + t.getMessage());
                    return null;
                });
    }

    public synchronized boolean reload() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        Settings.IMP.reload(getDataFolder().toPath().resolve("config.yml"));
        Settings.IMP.setLogger(Bukkit.getLogger());

        if (Settings.IMP.STORAGE.TYPE == Settings.STORAGES.MYSQL || Settings.IMP.STORAGE.TYPE == Settings.STORAGES.MARIADB) {
            var sql = Settings.IMP.STORAGE.SQL;
            database = new iDatabase(
                    "iJoiner",
                    sql.HOST,
                    sql.USER,
                    sql.PASSWORD,
                    sql.DATABASE,
                    sql.TABLE,
                    "CREATE TABLE IF NOT EXISTS `%table%`" +
                            "(`id` INT(5) PRIMARY KEY AUTO_INCREMENT, " +
                            "`uuid` VARCHAR(36) NOT NULL, " +
                            "`player` VARCHAR(16) NOT NULL," +
                            "`text` VARCHAR(35) NOT NULL) DEFAULT CHARSET='utf8b';",
                    sql.USE_SSL
            );
            if (!database.setup())
                return false;
        } else if (Settings.IMP.STORAGE.TYPE == Settings.STORAGES.LOCAL) {
            LocalConfigStorage.LOCAL.reload(getDataFolder().toPath().resolve("players.yml"));
            this.localStorage = LocalConfigStorage.LOCAL.PLAYERS;
        } else {
            log("&7[&6&l\\&7] &cКажется что-то пошло не так с загрузкой. &fПравильно ли стоит тип БД в конфигурационном файле?");
            return false;
        }
        new Storage(this);

        //JavaPlugin papi = null;
        //try {
        //    papi = getPlugin();
        //} catch (IllegalStateException | IllegalArgumentException IE) {
        //    log("&7[&6&l\\&7] &eКажется PAPI не запущен или не установлен. &7Плагину это не грозит, но не будут доступны некоторые плейсхолдеры.");
        //}
        //if (papi != null && papi.isEnabled()) {
        //    papiEnabled = true;
        //} else log("&7[&6&l\\&7] &eКажется PAPI не запущен или не установлен. &7Плагину это не грозит, но не будут доступны некоторые плейсхолдеры.");

        Bukkit.getPluginManager().registerEvents(new Listener(), this);
        try {
            var commands = new Commands();
            PluginCommand command = Objects.requireNonNull(getCommand("ijoiner"));
            command.setExecutor(commands);
            command.setTabCompleter(commands);
        } catch (NullPointerException ignored) {
            log("&7[&6&l\\&7] &cОшибка при регистраци команд. &7Кажется они убраны из plugin.yml.");
            return false;
        }

        return true;
    }
    public static boolean papiEnabled = false;

    @Override
    public void onDisable() {
        if (Settings.IMP.STORAGE.TYPE == Settings.STORAGES.MYSQL || Settings.IMP.STORAGE.TYPE == Settings.STORAGES.MARIADB)
            database.close();
        log("&7[&6&l\\&7] &6iJoiner успешно выключен.");
    }


    public static void log(String str, Object... args) {
        Bukkit.getLogger().info(String.format(str, args).replace("&", "§"));
    }
    public static void log(String str) {
        Bukkit.getLogger().info(str.replace("&", "§"));
    }
}
