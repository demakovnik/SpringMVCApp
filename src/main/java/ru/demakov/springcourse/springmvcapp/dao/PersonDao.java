package ru.demakov.springcourse.springmvcapp.dao;

import org.springframework.stereotype.Component;
import ru.demakov.springcourse.springmvcapp.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDao {

    private static int PEOPLE_COUNT = 0;
    private List<Person> people;

    {
        people = new ArrayList<Person>() {{
            add(new Person(++PEOPLE_COUNT, "Nikita", "Demakov", 36, "demakoffnik@gmail.com"));
            add(new Person(++PEOPLE_COUNT, "Ivan", "Frolov", 44, "frolov@mail.ru"));
        }};
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int i) {
        return people.stream().filter(person -> person.getId() == i)
                .findAny()
                .orElse(null);
    }

    public void create(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    public void update(int id, Person person) {
        delete(id);
        person.setId(id);
        people.add(person);
    }

    public void delete(int id) {
        people.removeIf(person -> person.getId() == id);
        if (people.size() == 0) {
            PEOPLE_COUNT = 0;
        }
    }
}
