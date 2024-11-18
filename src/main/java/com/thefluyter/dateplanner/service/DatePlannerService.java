package com.thefluyter.dateplanner.service;

import com.thefluyter.dateplanner.model.Friend;
import com.thefluyter.dateplanner.model.Friends;
import org.springframework.stereotype.Component;

@Component
public class DatePlannerService {

    public Friend planDate(Friends friends) {
        return friends.selectFriendToPlanWith();
    }
}
