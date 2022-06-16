package ru.demakov.springcourse.springmvcapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.demakov.springcourse.springmvcapp.dao.PersonDao;
import ru.demakov.springcourse.springmvcapp.models.Person;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/batch")
public class BatchController {

    private final PersonDao personDao;

    @Autowired
    public BatchController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping("/test-batch")
    public String getTestBatchForm() {
        return "batch/index";
    }

    @GetMapping("/test-batch/with")
    public String testWithBatch() {
        personDao.testWithBatch();
        return "redirect:/batch/test-batch";
    }

    @GetMapping("/test-batch/without")
    public String testWithoutBatch() {
        personDao.testWithoutBatch();
        return "redirect:/batch/test-batch";
    }
}
