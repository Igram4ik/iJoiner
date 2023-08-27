package dev.twilightsociety.ijoiner.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.twilightsociety.ijoiner.Settings;
import dev.twilightsociety.ijoiner.iJoiner;
import org.bukkit.Bukkit;
import org.checkerframework.checker.units.qual.N;
import org.intellij.lang.annotations.Language;

import javax.annotation.WillNotClose;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static dev.twilightsociety.ijoiner.iJoiner.log;

public class iDatabase {
    public iDatabase(String Name, String host, String user, String password, String db, String TableName, @Language("SQL") String Table, boolean useSSL) {
        this.Name = Name;
        this.Host = host;
        this.Database = db;
        this.User = user;
        this.Password = password;
        this.Table = Table;
        this.TableName = TableName;
        this.useSSL = useSSL;
        setHConfig();
    }

    private final String Name;
    private final String Host;
    private final String Database;
    private final String User;
    private final String Password;
    private final String TableName;
    private final String Table;
    private final boolean useSSL;
    public HikariDataSource dataSource;
    private final HikariConfig HConfig = new HikariConfig();
    public Connection connection;
    public Statement statement;

    public void startKeepAlive() {
        Bukkit.getScheduler()
                .runTaskTimerAsynchronously(iJoiner.getInstance(),
                        () -> {
                            try {
                                if (connection.isClosed()) {
                                    connection = dataSource.getConnection();
                                    statement = connection.createStatement();
                                }
                            } catch (SQLException e) {
                                log("&7[&6&l\\&7] &cСтранная ошибка SQL: &f" + e.getMessage());
                            }
                        }, 10L, 300L);
    }
    public boolean setup() {
        try { dataSource = new HikariDataSource(HConfig); }
        catch (RuntimeException RE) {
            log("&7[&6&l\\&7] &cОшибка Hikari &7["+Name+"]&c: &f" + RE.getMessage());
            return false;
        }
        try { connection = dataSource.getConnection(); }
        catch (SQLException SQLE) {
            log("&7[&6&l\\&7] &cОшибка SQL &7["+Name+"]&c: &f" + SQLE.getMessage());
            return false;
        }
        try {
            statement = connection.createStatement();
            if (createTable()) {
                if (Settings.IMP.DEBUG)
                    log("&7[&6&l\\&7] &aБаза данных &b" + Name + " &aуспешно запущена.");
            }
            else
                log("&7[&6&l\\&7] &cПроизошла ошибка при загрузки таблицы.");
            startKeepAlive();
            return true;
        }
        catch (SQLException SQLE) {
            log("&7[&6&l\\&7] &cОшибка SQL &7["+Name+"]&c: &f" + SQLE.getMessage());
            return false;
        }
    }

    public void setHConfig() {
        HConfig.setPoolName(Name);
        HConfig.setUsername(User);
        HConfig.setPassword(Password);
        HConfig.setKeepaliveTime(1000);
        HConfig.setJdbcUrl("jdbc:mariadb://" + Host + "/" + Database + ((useSSL ? "?useSSL=true" : "")));
        HConfig.setDriverClassName("org.mariadb.jdbc.Driver");
    }

    static {
        Objects.requireNonNull(org.mariadb.jdbc.Driver.class);
    }

    public Statement getStatement() {
        if (statement == null) return null;
        else return statement;
    }
    public String getTName() {
        return TableName;
    }

    public boolean update(@Language("sql") String SQL) {
        try {
            if (Settings.IMP.DEBUG)
                log("&7[&6&l\\&7] &bSQL &fDEBUG: &6" + SQL);
            if (connection.isClosed()) {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
            }
            statement.executeUpdate(SQL);
            return true;
        } catch (SQLException SQLE) {
            log("&7[&6&l\\&7] &cОшибка SQL &7["+Name+"]&c: &f" + SQLE.getMessage());
            return false;
        }
    }
    public boolean update(@Language("sql") String SQL, Object... args) {
        return update(String.format(SQL, args));
    }
    public boolean update(@Language("sql") String SQL, String... args) {
        return update(String.format(SQL, args));
    }

    public boolean createTable() {
        try {
            statement.executeUpdate(Table.replace("%table%", TableName));
            return true;
        }
        catch (SQLException SQLE) {
            log("&7[&6&l\\&7] &cОшибка SQL &7["+Name+"]&c: &f" + SQLE.getMessage());
            return false;
        }
    }

    public ResultSet query(@Language("sql") String SQL) {
        try {
            if (Settings.IMP.DEBUG)
                log("&7[&6&l\\&7] &bSQL &fQUERY: &6" + SQL);
            if (connection.isClosed()) {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
            }
            return statement.executeQuery(SQL);
        }
        catch (SQLException SQLE) {
            log("&7[&6&l\\&7] &cОшибка SQL &7["+Name+"]&c: &f" + SQLE.getMessage());
            return null;
        }
    }
    @WillNotClose
    public ResultSet query(@Language("sql") String SQL, Object... objects) {
        return query(String.format(SQL, objects));
    }
    public boolean close() {
        try {
            dataSource.close();
            return true;
        }
        catch (RuntimeException ignored) {
            return false;
        }
    }
}
