package com.revolution.stepup;

/**
 * Created by Nandash on 04-02-2017.
 */

public class Buddy {
    String username;
    String imageUrl;
    Boolean followStatus;
    String email;
    public Buddy(){

    }
    public Buddy(String uname, String imgUrl ,Boolean fStatus){
        this.username=uname;
        this.imageUrl=imgUrl;
        this.followStatus=fStatus;
    }
}
