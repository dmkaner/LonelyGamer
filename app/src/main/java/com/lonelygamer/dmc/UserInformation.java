package com.lonelygamer.dmc;

public class UserInformation  {

    private String uName;
    private String gTag;

    public UserInformation(){

    }

    public UserInformation(String username) {
        //this.gTag = gamertag;
        this.uName = username;
    }

    public String getGamertag() {
        return gTag;
    }

    public void setGamertag(String gamertag) {
        this.gTag = gamertag;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String username) {
        this.uName = username;
    }


}
