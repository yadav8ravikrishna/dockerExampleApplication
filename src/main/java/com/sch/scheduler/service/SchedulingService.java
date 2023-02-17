package com.sch.scheduler.service;

import com.sch.scheduler.entity.ReportSubscriber;
import com.sch.scheduler.repo.ReportSubscriberRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SchedulingService {

    private final ReportSubscriberRepo repo;

    public SchedulingService(ReportSubscriberRepo repo) {
        this.repo = repo;
    }

    @Scheduled(cron = "0/1 * * * * *")
    public void toSentScheduler(){
        System.out.println(LocalDateTime.now().plusMonths(1));

        List<ReportSubscriber> toSendList = new ArrayList<>();

        for(ReportSubscriber rs :repo.findAll() ) {
            if (rs.getLastSent() == null) {
                long minDay = LocalDateTime.of(2022, 1, 1, 0, 0, 0).toEpochSecond(OffsetDateTime.now().getOffset());
                long maxDay = LocalDateTime.of(2023, 2, 28, 23, 59, 59).toEpochSecond(OffsetDateTime.now().getOffset());
                long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(randomDay), ZoneId.of("UTC"));

                rs.setLastSent(localDateTime);
                rs.setDayOfWeek(localDateTime.getDayOfWeek().getValue());
                rs.setMonthOfYear(localDateTime.getMonthValue());
                rs.setDayOfMonth(localDateTime.getDayOfMonth());
                repo.save(rs);
            }
        }


        for(ReportSubscriber rs :repo.findAll() ){
            if (rs.getSetInterval().equals("daily")){
                if(rs.getLastSent().plusDays(1).toEpochSecond(OffsetDateTime.now().getOffset())<=LocalDateTime.now().toEpochSecond(OffsetDateTime.now().getOffset())){
                    toSendList.add(rs);
                }
            }

            if (rs.getSetInterval().equals("weekly")
                    && rs.getLastSent().getDayOfWeek().plus(1)==LocalDateTime.now().getDayOfWeek() ){
                    toSendList.add(rs);
            }


            if (rs.getSetInterval().equals("monthly")
                    && rs.getLastSent().getMonth().plus(1)==LocalDateTime.now().getMonth()
                    && rs.getDayOfMonth()==LocalDateTime.now().getDayOfMonth()){
                    toSendList.add(rs);
            }


        }
        LocalDateTime ldt = LocalDateTime.now();
        for(ReportSubscriber rs : toSendList){
            if(rs.getSetInterval().equals("daily"))
                System.out.println("Daily Template Number : " + rs.getTemplateId() + " sent to : " + rs.getEmail());
            if(rs.getSetInterval().equals("weekly"))
                System.out.println("weekly Template Number : " + rs.getTemplateId() + " sent to : " + rs.getEmail());
            if(rs.getSetInterval().equals("monthly"))
                System.out.println("monthly Template Number : " + rs.getTemplateId() + " sent to : " + rs.getEmail());
            rs.setLastSent(ldt);
            rs.setDayOfWeek(ldt.getDayOfWeek().getValue());
            rs.setMonthOfYear(ldt.getMonthValue());
            rs.setDayOfMonth(ldt.getDayOfMonth());
            repo.save(rs);
        }
    }
}
