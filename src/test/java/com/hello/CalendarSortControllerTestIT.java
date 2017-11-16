package com.hello;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalendarSortControllerTestIT {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Test
    public void shouldSort() throws JSONException {
        CalendarDto dto = new CalendarDto();
        dto.setMonths(Arrays.asList("C", "A", "B"));
        dto.setWeekDay(Arrays.asList("3", "1", "2"));

        String expected = "{\"months\":[\"A\",\"B\",\"C\"],\"weekDay\":[\"1\",\"2\",\"3\"]}";

        HttpEntity<CalendarDto> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<String> response = restTemplate.exchange(
            createURLWithPort("/sort"),
            HttpMethod.POST, entity, String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), true);
    }

    @Test
    public void badRequest() {
        CalendarDto dto = new CalendarDto();

        HttpEntity<CalendarDto> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            createURLWithPort("/sort"),
            HttpMethod.POST, entity, String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid input data"));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
