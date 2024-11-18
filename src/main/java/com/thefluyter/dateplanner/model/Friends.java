package com.thefluyter.dateplanner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friends {

    @JsonProperty("friends")
    private List<Friend> friendList;

    public Friend selectFriendToPlanWith() {
        Friends friendsWithLowestCounter = findFriendsWithLowestCounter();
        if (friendsWithLowestCounter.getFriendList().size() == 1) {
            return friendsWithLowestCounter.getFriendList().getFirst();
        }

        Friends friendsWithLatestDate = friendsWithLowestCounter.findFriendsWithOldestDate();
        if (friendsWithLatestDate.getFriendList().size() == 1) {
            return friendsWithLatestDate.getFriendList().getFirst();
        }

        return null;
    }

    public Friends findFriendsWithLowestCounter() {
        int minCounter = friendList.stream()
            .mapToInt(Friend::getDateCounter)
            .min()
            .orElse(0);

        List<Friend> friends = friendList.stream()
            .filter(friend -> friend.getDateCounter() == minCounter)
            .toList();

        return new Friends(friends);
    }

    private Friends findFriendsWithOldestDate() {
        LocalDate oldestDate = friendList.stream()
            .map(Friend::getLastDate)
            .min(LocalDate::compareTo)
            .orElse(null);

        List<Friend> friendsWithOldestDate = friendList.stream()
            .filter(friend -> friend.getLastDate().equals(oldestDate))
            .toList();

        return new Friends(friendsWithOldestDate);
    }

}
