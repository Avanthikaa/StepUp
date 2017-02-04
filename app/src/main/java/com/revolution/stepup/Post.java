package com.revolution.stepup;

/**
 * Created by vvvro on 2/4/2017.
 */

public class Post {
    public String pid;
    public String uid;
    public String postedDate;
    public String content;
    public String userProfileURL;
    public String imageURL;
    public int type;
    public Post(){

    }
    public Post(String postID, String userID, String postDate, String profileImageURL, String postContent){
        pid = postID;
        uid = userID;
        postedDate = postDate;
        userProfileURL = profileImageURL;
        content = postContent;
        type = 1;
    }
    public Post(String postID, String userID, String postDate, String profileImageURL, String postContent, String postImageURL){
        pid = postID;
        uid = userID;
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
}
