package com.hello;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class CalendarSortServiceImpl implements CalendarSortService {

    private static final int SLEEP_DELAY = 50;

    private static final Duration MONTH_SORT_DELAY = Duration.ofMillis(1000);
    private static final Duration WEEK_DAY_SORT_DELAY = Duration.ofMillis(500);
    private final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public CalendarDto sort(final CalendarDto calendarDto) {
        try {
            final Flux<String> month = Flux.fromIterable(calendarDto.getMonths());
            final Flux<String> weekDay = Flux.fromIterable(calendarDto.getWeekDay());
            final int totalMonth = calendarDto.getMonths().size();
            final int totalWeekDays = calendarDto.getWeekDay().size();

            final CalendarDto result = new CalendarDto();
            result.setMonths(Collections.synchronizedList(new ArrayList<>(totalMonth)));
            result.setWeekDay(Collections.synchronizedList(new ArrayList<>(totalWeekDays)));

            month.sort().delayElements(MONTH_SORT_DELAY).subscribe(result.getMonths()::add);
            weekDay.sort().delayElements(WEEK_DAY_SORT_DELAY).subscribe(result.getWeekDay()::add);

            while (totalMonth > result.getMonths().size() || totalWeekDays > result.getWeekDay().size()) {
                try {
                    Thread.sleep(SLEEP_DELAY);
                } catch (InterruptedException e) {
                    logger.error("Main thread interrupted", e);
                }
            }
            return result;
        } catch (NullPointerException e) {
            throw new SortException(e);
        }
    }
}
