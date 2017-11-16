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
            final Flux<String> month = Flux.fromIterable(calendarDto.getMonths());
            final Flux<String> weekDay = Flux.fromIterable(calendarDto.getWeekDay());

            final CalendarDto result = new CalendarDto();
            result.setMonths(fluxToString(sortAsync(month, MONTH_SORT_DELAY)));
            result.setWeekDay(fluxToString(sortAsync(weekDay, WEEK_DAY_SORT_DELAY)));
            return result;
        } catch (NullPointerException e) {
            throw new SortException(e);
        }
    }

    private Flux<String> sortAsync(final Flux<String> stringFlux, final Duration delay) {
        return stringFlux.delayElements(delay).sort();
    }

    private List<String> fluxToString(final Flux<String> stringFlux) {
        return stringFlux.toStream().collect(Collectors.toList());
    }

}
