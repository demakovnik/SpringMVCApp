package ru.demakov.springcourse.springmvcapp.dao;

import org.springframework.stereotype.Component;
import ru.demakov.springcourse.springmvcapp.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDao {

    private static final String URL = "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() {
        List<Person> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            final String URL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(URL);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                Person person = new Person();
                person.setId(id);
                person.setName(name);
                person.setSurname(surname);
                person.setAge(age);
                person.setEmail(email);
                result.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Person show(int i) {

        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM Person WHERE id=?");
            preparedStatement.setInt(1, i);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            int age = resultSet.getInt("age");
            String email = resultSet.getString("email");
            person = new Person(id,name,surname,age,email);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
       return person;
    }

    public void create(Person person) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Person(name,surname,age,email) VALUES(?,?,?,?)");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2,person.getSurname());
            preparedStatement.setInt(3,person.getAge());
            preparedStatement.setString(4, person.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(int id, Person person) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Person SET name=?,surname=?,age=?,email=? WHERE id=?");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setString(4, person.getEmail());
            preparedStatement.setInt(5,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM Person WHERE id=?");
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
