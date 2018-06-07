package com.lonelygamer.dmc;

import java.util.ArrayList;

/**
 * Created by dyl on 7/19/17.
 */

public class GameInformation {

    private String GameName;
    private String PicturePath;
    private ArrayList<String> Consoles = new ArrayList<>();
    private ArrayList<String> Genres = new ArrayList<>();
    private int ActiveLobbies;

    public GameInformation(String name, String path){
        this.GameName = name;
        this.PicturePath = path;
    }
    public GameInformation(){
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String gameName) {
        GameName = gameName;
    }

    public ArrayList getConsoles() {
        return Consoles;
    }

    public void setConsoles(String consoles) {
        Consoles.add(consoles);
    }

    public ArrayList getGenres() {
        return Genres;
    }

    public void setGenres(String genres) {
        Genres.add(genres);
    }

    public int getActiveLobbies() {
        return ActiveLobbies;
    }

    public void setActiveLobbies(int activeLobbies) {
        ActiveLobbies = activeLobbies;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

}
