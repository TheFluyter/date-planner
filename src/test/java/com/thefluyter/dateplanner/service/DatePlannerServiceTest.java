package com.thefluyter.dateplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thefluyter.dateplanner.model.Friend;
import com.thefluyter.dateplanner.model.Friends;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatePlannerServiceTest {

    private static DatePlannerService datePlannerService;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        datePlannerService = new DatePlannerService();
        objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    }

    @Test
    void testOneLowestCounterFriendList() throws IOException {
        String input = Files.readString(Path.of("src/test/resources/input/friends-with-different-values.json"));
        Friends friends = objectMapper.readValue(input, Friends.class);

        Friend friend = datePlannerService.planDate(friends);

        assertEquals("Karel", friend.getName());
    }

    @Test
    void testMultipleLowestCounterFriends() throws IOException {
        String input = Files.readString(Path.of("src/test/resources/input/friends-with-duplicate-counters.json"));
        Friends friends = objectMapper.readValue(input, Friends.class);

        Friend friend = datePlannerService.planDate(friends);

        assertEquals("Henk", friend.getName());
    }

}