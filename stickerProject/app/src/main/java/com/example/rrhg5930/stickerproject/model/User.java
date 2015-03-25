package com.example.rrhg5930.stickerproject.model;

import java.util.ArrayList;

/**
 * Created by pierre on 25/03/2015.
 */
public class User {

    public ArrayList<String> friendList;
    public ArrayList<String> pendingFriendList;

    public static User user;

    User(){
        friendList = new ArrayList<>();
        pendingFriendList = new ArrayList<>();

    }

    public static User getInstance(){
        if (user == null)
            user = new User();
        return user;
    }

    public void addFriend(String newFriend){
        friendList.add(newFriend);
    }

    public void addPendingFriend(String newPendingFriend){
        pendingFriendList.add(newPendingFriend);
    }


}
