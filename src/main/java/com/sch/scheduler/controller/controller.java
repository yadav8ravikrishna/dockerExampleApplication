package com.sch.scheduler.controller;

import com.sch.scheduler.entity.ReportSubscriber;
import com.sch.scheduler.repo.ReportSubscriberRepo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
    private final ReportSubscriberRepo repo;

    public controller(ReportSubscriberRepo repo) {
        this.repo = repo;
    }

    @PostMapping("/addData")
    public ReportSubscriber saveData(@RequestBody ReportSubscriber reportSubscriber){
        return repo.save(reportSubscriber);
    }
}