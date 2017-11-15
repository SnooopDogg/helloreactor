package com.hello;

import java.time.Duration;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class CalendarSortServiceImpl implements CalendarSortService {

    private static final Duration MONTH_SORT_DELAY = Duration.ofMillis(1000);
    private static final Duration WEEK_DAY_SORT_DELAY = Duration.ofMillis(500);

    @Override
    public CalendarDto sort(final CalendarDto calendarDto) {

        Flux<String> month = Flux.fromIterable(calendarDto.getMonths());
        Flux<String> weekDay = Flux.fromIterable(calendarDto.getWeekDay());

        calendarDto.setMonths(month.delayElements(MONTH_SORT_DELAY).sort().toStream().collect(Collectors.toList()));
        calendarDto.setWeekDay(weekDay.delayElements(WEEK_DAY_SORT_DELAY).sort().toStream().collect(Collectors.toList()));
        return calendarDto;
    }
}
