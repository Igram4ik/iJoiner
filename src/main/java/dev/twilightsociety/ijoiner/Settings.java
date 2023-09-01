package dev.twilightsociety.ijoiner;

import dev.igrammine.commons.YamlConfig;

import java.util.List;

public class Settings extends YamlConfig {
    @Ignore
    public static final Settings IMP = new Settings();

    @Comment("""
            iJoiner - Igram4ik
            # Пользовательские сообщения при заходе на сервер.
            
            # Права:
            #  - ijoiner.reload - Перезагрузить плагин (/ij reload)
            #  - ijoiner.clear - Очистить польз-ий текст (/ij clear)
            #  - ijoiner.set - Поставить польз-ий текст (/ij set)
            
            # Если true, то сообщения включены.""")
    public boolean ENABLED = true;

    @Comment("Вывод debug сообщений, необходимо если что-то идет не так.")
    public boolean DEBUG = false;

    @Create
    public STORAGE STORAGE;
    public static class STORAGE {
        @Comment("Доступные: LOCAL, MYSQL/MARIADB")
        public STORAGES TYPE = STORAGES.LOCAL;
        @Create
        public SQL SQL;
        @Comment("Если включен тип: MYSQL/MARIADB")
        public static class SQL {
            public String HOST = "127.0.0.1:3306";
            public String USER = "root";
            public String PASSWORD = "abdul";
            public String DATABASE = "ijoiner";
            public String TABLE = "iJoiner";
            public boolean USE_SSL = false;
        }
    }
    public enum STORAGES {
        MYSQL,
        MARIADB,
        LOCAL
    }

    @Comment("""
            Паттерн для пользовательских сообщений.
            # Он необходим для того, чтобы сделать рамки сообщений, которые игрок может редактировать.
            #
            # Например: "&c[!] %player% %message% (Зашел на сервер)"
            #   P.S. %message% - это текст, который задал игрок через команду
            # Итог: "[!] Igram4ik пернул с подливой (Зашел на сервер)"
            # 
            # Плейсхолдеры:
            #  - %player% - никнейм игрока
            #  - %prefix% - префикс игрока (%vault_prefix%)
            #  - %suffix% - суффикс игрока (%vault_suffix%)
            #  - %rprefix% - донатный префикс игрока (%vault_rankprefix%)
            #  - %rsuffix% - донатный суффикс игрока (%vault_ranksuffix%)
            #  - %online% - онлайн сервера
            """)
    public String PATTERN = "%player% - %message%";

    @Comment("""
            Стандартные случайные сообщения. (Если игрок себе ничего не ставил.)
            #
            #Плейсхолдеры:
            # - %player% - никнейм игрока
            # - %prefix% - префикс игрока (%vault_prefix%)
            # - %suffix% - суффикс игрока (%vault_suffix%)
            # - %rprefix% - донатный префикс игрока (%vault_rankprefix%)
            # - %rsuffix% - донатный суффикс игрока (%vault_ranksuffix%)
            # - %online% - онлайн сервера
            # 
            # Также поддерживается перенос строки через - "%nl%"
            """)
    public List<String> DEFAULTS = List.of(
            "&c&l[!] &f%player% &7зашёл на сервер, выбив дверь.",
            "&c&l[!] &f%prefix% %player% &7приземлился прямоком на сервер.",
            "&c&l[!] &7Кто-то шлепнулся на сервер, так%nl%&7что аж ачутился на следующей строке! &8(%player%)"
    );

    @Create
    public MESSAGES MESSAGES;
    @Comment("Сообщения и логи в консоле")
    public static class MESSAGES {
        public String RELOAD = "&c&l[!] &aiJoiner успешно перезапущен.";
        public String RELOAD_FAILED = "&c&l[!] &cНе удалось перезапуститься. &7[Смотрите в консоле]";
        public String UNKNOWN_COMMAND = "&c&l[!] &cНеизвестная подкоманда.";
        public String NOPERM = "&c&l[!] &cНедостаточно прав.";
        public String NOT_FOR_CONSOLE = "&c&l[!] &cДанная команда доступна исключительно для консоли.";
        public String NO_ARGS = "&c&l[!] &cНедостаточно аргументов.";
        public String CLEARED = "&c&l[!] &aПользовательский текст при заходе на сервер успешно удалён. &7(Теперь писатся будет случайный из предложенных.)";
        public String CLEAR_FAILED = "&c&l[!] &cНе удалось удалить пользовательский текст. &fОбратитесь к Администрацией";
        public String SETTED = "&c&l[!] &aПользовательский текст успешно установлен. &7(Он будет синхронизирован на всех режимах.)";
        public String SET_FAILED = "&c&l[!] &cНе удалось установить пользовательский текст. &fОбратитесь к Администрацией.";
        public String TOO_MUCH = "&c&l[!] &cВаш текст слишком длинный!";
    }
}
