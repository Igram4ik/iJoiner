package dev.twilightsociety.ijoiner;

import dev.twilightsociety.ijoiner.commons.config.YamlConfig;

public class Settings extends YamlConfig {
    @Ignore
    public static final Settings IMP = new Settings();

    @Comment("""
            
            
            
            """)
    public boolean ENABLED = true;

    @Comment("Вывод debug сообщений, необходимо если что-то идет не так.")
    public boolean DEBUG = false;

    @Create
    public STORAGE STORAGE;
    public static class STORAGE {
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

    @Create
    public MESSAGES MESSAGES;
    @Comment("Сообщения и логи в консоле")
    public static class MESSAGES {
        public String RELOAD = "&c&l[!] &aiJoiner успешно перезапущен.";
        public String RELOAD_FAILED = "&c&l[!] &cНе удалось перезапуститься. &7[Смотрите в консоле]";
        public String UNKNOWN_COMMAND = "&c&l[!] &cНеизвестная подкоманда.";
        public String NOPERM = "&c&l[!] &cНедостаточно прав.";
        public String NO_ARGS = "&c&l[!] &cНедостаточно аргументов.";
        public String CLEARED = "&c&l[!] &aПользовательский текст при заходе на сервер успешно удалён. &7(Теперь писатся будет случайный из предложенных.)";
        public String CLEAR_FAILED = "&c&l[!] &cНе удалось удалить пользовательский текст. &fОбратитесь к Администрацией";
        public String SETTED = "&c&l[!] &aПользовательский текст успешно установлен. &7(Он будет синхронизирован на всех режимах.)";
        public String SET_FAILED = "&c&l[!] &cНе удалось установить пользовательский текст. &fОбратитесь к Администрацией.";
    }
}
