package com.thefluyter.dateplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.thefluyter.dateplanner.exception.FriendPlanningException;
import com.thefluyter.dateplanner.model.Friend;
import com.thefluyter.dateplanner.model.Friends;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class DatePlannerService {

    private final ObjectMapper objectMapper;

    public DatePlannerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<Friend> getAllFriends(String pathToFriendsJson) {
        try {
            String json = Files.readString(Path.of(pathToFriendsJson));
            Friends friends = objectMapper.readValue(json, Friends.class);
            return friends.getFriendList();
        } catch (IOException exception) {
            throw new FriendPlanningException("Failed to get an overview of friends due to an I/O error:" + exception.getMessage());
        }
    }

    public Friend planDate(String pathToFriendsJson) {
        try {
            String json = Files.readString(Path.of(pathToFriendsJson));
            Friends friends = objectMapper.readValue(json, Friends.class);
            return friends.selectFriendToPlanWith();
        } catch (IOException exception) {
            throw new FriendPlanningException("Failed to plan a date with friends due to an I/O error:" + exception.getMessage());
        }
    }

    public void recordPlannedDate(String pathToFriendsJson, String name) {
        Path path = Path.of(pathToFriendsJson);
        String json;
        Friends friends;
        try {
            json = Files.readString(path);
            friends = objectMapper.readValue(json, Friends.class);
        } catch (IOException exception) {
            throw new FriendPlanningException("Failed to record a date with friend due to an I/O error:" + exception.getMessage());
        }

        for (Friend friend : friends.getFriendList()) {
            if (name.equals(friend.getName())) {
                friend.updateDateCounter();
                friend.updateDateToToday();
                break;
            }
        }

        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        String updatedJson;
        try {
            updatedJson = writer.writeValueAsString(friends);
            Files.write(path, updatedJson.getBytes());
        } catch (IOException exception) {
            throw new FriendPlanningException("Failed to record a date with friend due to an I/O error:" + exception.getMessage());
        }
    }

}
