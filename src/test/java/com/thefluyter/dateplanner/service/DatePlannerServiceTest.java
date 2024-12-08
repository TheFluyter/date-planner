package com.thefluyter.dateplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thefluyter.dateplanner.exception.FriendPlanningException;
import com.thefluyter.dateplanner.model.Friend;
import com.thefluyter.dateplanner.model.Friends;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testGetAllFriends_shouldReturnFriendList() throws IOException {
        Files.writeString(TMP_JSON_PATH, createTmpJson());
        List<Friend> friends = datePlannerService.getAllFriends(TMP_JSON);

        assertEquals(1, friends.size());
        assertEquals("Henk", friends.getFirst().getName());
    }

    @Test
    void invalidJsonPath_shouldThrowException() {
        assertThrows(FriendPlanningException.class, () -> datePlannerService.getAllFriends("invalid.json"));
        assertThrows(FriendPlanningException.class, () -> datePlannerService.planDate("invalid.json"));
        assertThrows(FriendPlanningException.class, () -> datePlannerService.recordPlannedDate(Path.of("invalid.json"), "N/A", LocalDate.now()));
    }

    @Test
    void testUniqueValues_shouldReturnFriendWithLowestCounter() {
        Friend friend = datePlannerService.planDate("src/test/resources/input/friends-with-unique-values.json");
        assertEquals("Karel", friend.getName());
    }

    @Test
    void testDuplicateLowestCounter_shouldReturnFriendWithOldestDate() {
        Friend friend = datePlannerService.planDate("src/test/resources/input/friends-with-duplicate-counters.json");
        assertEquals("Henk", friend.getName());
    }

    @Test
    void testRecordPlannedDate_shouldUpdateDateAndCounter() throws IOException {
        Files.writeString(TMP_JSON_PATH, createTmpJson());
        datePlannerService.recordPlannedDate(TMP_JSON_PATH, "Henk", LocalDate.now());

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