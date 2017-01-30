package xyz.azsoftware.timusstattrak.lib;

public class OriginalUserTimusData {

    private String originalUserData;

    OriginalUserTimusData(String string){originalUserData=string;}

    public String[] split() {
        return originalUserData.split("@");
    }
    public String getOriginalUserData() {
        return originalUserData;
    }
}
