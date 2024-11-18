package com.thefluyter.dateplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thefluyter.dateplanner.model.Friend;
import com.thefluyter.dateplanner.model.Friends;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatePlannerServiceTest {

    private static final String TMP_JSON = "src/test/resources/input/tmp-file-for-updating.json";
    private static final Path TMP_JSON_PATH = Path.of(TMP_JSON);

    private static DatePlannerService datePlannerService;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
        datePlannerService = new DatePlannerService(objectMapper);
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(TMP_JSON_PATH);
    }

    @Test
    void testUniqueValues_shouldReturnFriendWithLowestCounter() throws IOException {
        Friend friend = datePlannerService.planDate("src/test/resources/input/friends-with-unique-values.json");
        assertEquals("Karel", friend.getName());
    }

    @Test
    void testDuplicateLowestCounter_shouldReturnFriendWithOldestDate() throws IOException {
        Friend friend = datePlannerService.planDate("src/test/resources/input/friends-with-duplicate-counters.json");
        assertEquals("Henk", friend.getName());
    }

    @Test
    void testRecordPlannedDate_shouldUpdateDateAndCounter() throws IOException {
        Files.writeString(TMP_JSON_PATH, createTmpJson());
        datePlannerService.recordPlannedDate(TMP_JSON, "Henk");

        String json = Files.readString(TMP_JSON_PATH);
        Friends friends = objectMapper.readValue(json, Friends.class);
        Friend henk = friends.getFriend("Henk");

        assertEquals(3, henk.getDateCounter());
        assertEquals(LocalDate.now(), henk.getLastDate());
    }

    private String createTmpJson() {
        return """
            {
              "friends" : [
                {
                  "name" : "Henk",
                  "date-counter": 2,
                  "last-date": "2024-01-20"
                }
              ]
            }
            """;
    }

}