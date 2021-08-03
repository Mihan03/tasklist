package ru.project.tasklist.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.tasklist.business.entity.Priority;
import ru.project.tasklist.business.entity.Stat;
import ru.project.tasklist.business.search.PrioritySearchValues;
import ru.project.tasklist.business.service.PriorityService;
import ru.project.tasklist.business.service.StatService;
import ru.project.tasklist.business.util.MyLogger;

import java.util.List;

@RestController
public class StatController {

    private StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody String email) {

        MyLogger.debugMethodName("StatController: findAll(email) --------------------------------------");

        return ResponseEntity.ok(statService.findStat(email));
    }

}
