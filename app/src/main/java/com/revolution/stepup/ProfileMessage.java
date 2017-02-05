package com.revolution.stepup;

/**
 * Created by vvvro on 2/5/2017.
 */

public class ProfileMessage {
    String userName, message, imageURL;
    public ProfileMessage(){

    }
    public ProfileMessage(String userName, String message,String imageURL){
        this.userName = userName;
        this.message = message;
        this.imageURL = imageURL;
    }
}
