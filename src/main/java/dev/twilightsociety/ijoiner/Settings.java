package dev.twilightsociety.ijoiner;

import dev.twilightsociety.ijoiner.commons.YamlConfig;

public class Settings extends YamlConfig {
    @Ignore
    public static final Settings IMP = new Settings();

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
}
