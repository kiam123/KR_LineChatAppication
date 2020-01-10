package com.example.kr_linechatappication.datas;

import java.util.ArrayList;

//TODO Refactor friend
public class UserInfo {
    private static UserInfo userInfo = new UserInfo();
    private String mName;
    private String headerImage;
    private String friend;
    private String friendHeaderImage;
    private ArrayList<String> iconList = new ArrayList<>();
    private ArrayList<String> friendsList = new ArrayList<>();

    private UserInfo() {}

    public static synchronized UserInfo getInstance() {
        if(userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getFriendHeaderImage() {
        return friendHeaderImage;
    }

    public void setFriendHeaderImage(String friendHeaderImage) {
        this.friendHeaderImage = friendHeaderImage;
    }

    public void addIcon(String iconUrl) {
        iconList.add(iconUrl);
    }

    public ArrayList<String> getIconList() {
        return iconList;
    }

    public void addFriend(String friendId) {
        friendsList.add(friendId);
    }

    public ArrayList<String> getFriendList() {
        return friendsList;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    class friend {

    }
}
