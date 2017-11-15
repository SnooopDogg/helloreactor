package com.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalendarSortController {

    @Autowired
    private CalendarSortService calendarSortService;

    @PostMapping("/sort")
    public CalendarDto sort(@RequestBody CalendarDto input) {
        return calendarSortService.sort(input);
    }
}
