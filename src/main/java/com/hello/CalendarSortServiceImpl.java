package com.hello;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import reactor.core.Disposable;
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

            final CalendarDto result = new CalendarDto();
            result.setMonths(Collections.synchronizedList(new ArrayList<>(calendarDto.getMonths().size())));
            result.setWeekDay(Collections.synchronizedList(new ArrayList<>(calendarDto.getWeekDay().size())));

            Disposable sorterMonth = sortAndSubscribe(month, result.getMonths(), MONTH_SORT_DELAY);
            Disposable sorterWeek = sortAndSubscribe(weekDay, result.getWeekDay(), WEEK_DAY_SORT_DELAY);

            while (!(sorterMonth.isDisposed() && sorterWeek.isDisposed())) {
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

    private Disposable sortAndSubscribe(final Flux<String> fluxString, final List result, final Duration sortDelay) {
        return fluxString.sort().delayElements(sortDelay).doOnSubscribe(System.out::println).subscribe(result::add);
    }
}
