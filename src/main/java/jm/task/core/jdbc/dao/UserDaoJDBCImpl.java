package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getMySQLConnection(); Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT," +
                    "firstName VARCHAR(255),lastName VARCHAR(255), age INT, PRIMARY KEY (id))");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Ошибка при создании таблицы");
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getMySQLConnection(); Statement statement = connection.createStatement()) {

            String execute = "DROP TABLE users";
            statement.execute(execute);

        } catch (SQLException ignore) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String firstName, String lastName, byte age) {
        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement pS = connection.prepareStatement("INSERT INTO users(firstName,lastName,age) VALUES (?,?,?);")) {

            pS.setString(1, firstName);
            pS.setString(2, lastName);
            pS.setInt(3, age);
            pS.executeUpdate();
            System.out.println("User с именем - " + firstName + " добавлен в базу данных");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Ошибка при сохранении пользователя");
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement pS = connection.prepareStatement("DELETE FROM users WHERE id = ?;")) {

            pS.setLong(1, id);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Ошибка при удалении пользователя по id");
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = Util.getMySQLConnection(); Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM users;")) {

            for (int i = 0; rs.next(); i++) {
                long id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                byte age = (byte) rs.getInt(4);
                userList.add(new User(firstName, lastName, age));
                userList.get(i).setId(id);
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Ошибка при получении списка пользователей");
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getMySQLConnection(); Statement statement = connection.createStatement()) {

            statement.execute("DELETE FROM users;");

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Ошибка при очистке таблицы");
        }
    }
}