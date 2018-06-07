package com.lonelygamer.dmc;

/**
 * Created by dyl on 7/24/17.
 */

public class LobbyInformation {

    private String gameName;
    private String console;
    private String mic;
    private String note;
    private String playerCountIn;
    private String playerCountTotal;
    private String host;
    private String DbImgName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPlayerCountIn() {
        return playerCountIn;
    }

    public void setPlayerCountIn(String playerCountIn) {
        this.playerCountIn = playerCountIn;
    }

    public String getPlayerCountTotal() {
        return playerCountTotal;
    }

    public void setPlayerCountTotal(String playerCountTotal) {
        this.playerCountTotal = playerCountTotal;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDbImgName(){
        return DbImgName;
    }

    public void setDbImgName(String var1){
        DbImgName = var1;
    }

}
