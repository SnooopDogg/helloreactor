package com.hello;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class CalendarSortServiceImpl implements CalendarSortService {

    private static final Duration MONTH_SORT_DELAY = Duration.ofMillis(1000);
    private static final Duration WEEK_DAY_SORT_DELAY = Duration.ofMillis(500);

    @Override
    public CalendarDto sort(final CalendarDto calendarDto) {
        try {
            Flux<String> month = Flux.fromIterable(calendarDto.getMonths());
            Flux<String> weekDay = Flux.fromIterable(calendarDto.getWeekDay());
            final CalendarDto result = new CalendarDto();
            result.setMonths(sortAsync(month, MONTH_SORT_DELAY));
            result.setWeekDay(sortAsync(weekDay, WEEK_DAY_SORT_DELAY));
            return result;
        } catch (NullPointerException e) {
            throw new SortException(e);
        }
    }

    private List<String> sortAsync(final Flux<String> month, Duration delay) {
        return month.delayElements(delay).sort().toStream().collect(Collectors.toList());
    }
}
