package com.thefluyter.dateplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.thefluyter.dateplanner.model.Friend;
import com.thefluyter.dateplanner.model.Friends;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DatePlannerService {

    private final ObjectMapper objectMapper;

    public DatePlannerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Friend planDate(String pathToFriendsJson) throws IOException {
        String json = Files.readString(Path.of(pathToFriendsJson));
        Friends friends = objectMapper.readValue(json, Friends.class);
        return friends.selectFriendToPlanWith();
    }

    public void recordPlannedDate(String pathToFriendsJson, String name) throws IOException {
        String json = Files.readString(Path.of(pathToFriendsJson));
        Friends friends = objectMapper.readValue(json, Friends.class);

        for (Friend friend : friends.getFriendList()) {
            if (name.equals(friend.getName())) {
                friend.updateDateCounter();
                friend.updateDateToToday();
                break;
            }
        }

        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        String updatedJson = writer.writeValueAsString(friends);
        Files.write(Path.of(pathToFriendsJson), updatedJson.getBytes());
    }

}
