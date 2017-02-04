package com.revolution.stepup;

import android.widget.Button;

/**
 * Created by vvvro on 2/4/2017.
 */

public class Post {
    public String pid;
    public String uid;
    public String userName;
    public String postedDate;
    public String content;
    public String userProfileURL;
    public String imageURL;

    public String eventVenue;
    public String eventName;
    public String eventDate;

    public String eid;

    public Boolean attending;


    public int type;
    public Post(){

    }
    public Post(String postID, String userName, String userID, String postDate, String profileImageURL, String postContent){
        pid = postID;
        uid = userID;
        this.userName = userName;
        postedDate = postDate;
        userProfileURL = profileImageURL;
        content = postContent;
        type = 1;
    }
    public Post(String postID, String userName, String userID, String postDate, String profileImageURL, String postContent, String postImageURL){
        pid = postID;
        uid = userID;
        this.userName = userName;
        postedDate = postDate;
        userProfileURL = profileImageURL;
        imageURL = postImageURL;
        if(postContent.equals("")){
            type = 2;
        }else{
            content = postContent;
            type = 3;
        }
    }

    public Post(String postID, String userName, String userID, String postDate, String profileImageURL, String postContent, String postImageURL, String eventDate, String eventName, String eventVenue, Boolean attend, String eid ){
        pid = postID;
        uid = userID;
        this.userName = userName;
        postedDate = postDate;
        userProfileURL = profileImageURL;
        imageURL = postImageURL;
        content = postContent;
        type = 4;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventVenue = eventVenue;
        attending = attend;
        this.eid = eid;
    }
}
