package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final static String USERS_TABLE = Util.getDbName() + ".`users`";
    private final static Connection connection;

    static {
        try {
            connection = Util.getInstance().getConnection();
        } catch (ClassNotFoundException|SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public UserDaoJDBCImpl() {}

    private static void exctUpd(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void createUsersTable() {
        exctUpd( "CREATE TABLE if not exists " + USERS_TABLE + " (\n" +
                    "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NOT NULL,\n" +
                    "  `lastName` VARCHAR(45) NOT NULL,\n" +
                    "  `age` INT(3) NULL,\n" +
                    "  PRIMARY KEY (`id`));");
    }

    public void dropUsersTable() {
        exctUpd("drop table if exists " + USERS_TABLE);
    }

    public void saveUser(String name, String lastName, byte age) {
        exctUpd("INSERT INTO " + USERS_TABLE +"VALUES (DEFAULT,"
                    + " \"" + name + "\",  \"" + lastName + "\", " + age + ")");
    }

    public void removeUserById(long id) {
        exctUpd( "DELETE FROM " + USERS_TABLE + "WHERE id = " + id);
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        String sql = "select * from `pp113`.`users`";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery(sql);
            User user;
            while (rs.next()) {
                user = new User(
                rs.getString(2),
                rs.getString(3),
                rs.getByte(4));
                user.setId(rs.getLong(1));

                result.add(user);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void cleanUsersTable() {
        exctUpd("TRUNCATE " + USERS_TABLE);
    }
}
