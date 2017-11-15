package com.hello;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDto {
    List <String> months;
    List <String> weekDay;
}
