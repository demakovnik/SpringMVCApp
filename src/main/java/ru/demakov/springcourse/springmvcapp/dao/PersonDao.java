package ru.demakov.springcourse.springmvcapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.demakov.springcourse.springmvcapp.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int i) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{i},
                new BeanPropertyRowMapper<>(Person.class)).stream().findFirst().orElse(null);
    }

    public void create(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name,surname,age,email) VALUES(?,?,?,?)",
                person.getName(), person.getSurname(), person.getAge(), person.getEmail());
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE Person SET name=?,surname=?,age=?,email=? WHERE id=?",
                person.getName(),person.getSurname(),person.getAge(),person.getEmail(),id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);
    }

    public void testWithBatch() {
        List<Person> people = get1000Persons();
        long before = System.currentTimeMillis();
        jdbcTemplate.batchUpdate("INSERT INTO Person(name,surname,age,email) VALUES(?,?,?,?) ",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, people.get(i).getName());
                        ps.setString(2, people.get(i).getSurname());
                        ps.setInt(3, people.get(i).getAge());
                        ps.setString(4,people.get(i).getEmail());
                    }
                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });
        long after = System.currentTimeMillis();
        System.out.println("Time is (with batch):" + (after - before));
    }

    public void testWithoutBatch() {
        List<Person> people = get1000Persons();
        long before = System.currentTimeMillis();
        for (Person person : people) {
            jdbcTemplate.update("INSERT INTO Person(name,surname,age,email) VALUES(?,?,?,?)"
            ,person.getName(),person.getSurname(),person.getAge(),person.getEmail());
        }
        long after = System.currentTimeMillis();
        System.out.println("Time is (without batch): " + (after - before));
    }

    private List<Person> get1000Persons() {
        List<Person> personList = new ArrayList<>();
        for(int i = 0;i<1000;i++) {
            Person person = new Person();
            person.setName("Name " + i);
            person.setSurname("Surname " + i);
            person.setAge(36);
            person.setEmail("asd@asd.com");
            personList.add(person);
        }
        return personList;
    }
}
