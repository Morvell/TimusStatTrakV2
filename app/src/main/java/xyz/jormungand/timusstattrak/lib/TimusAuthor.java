package xyz.jormungand.timusstattrak.lib;

import java.net.URL;

public class TimusAuthor {

    private URL url;
    private String userName;
    private String userRang;
    private String numberOfSolvedTasks;
    private String dataOfLastSolvedTasks;

    public TimusAuthor(){
        this.url= null;
        userName = "not specified";
        userRang = "not specified";
        numberOfSolvedTasks = "not specified";
        dataOfLastSolvedTasks = "not specified";
    }

    public URL getUrl () {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRang() {
        return userRang;
    }

    public void setUserRang(String userRang) {
        this.userRang = userRang;
    }

    public String getNumberOfSolvedTasks() {
        return numberOfSolvedTasks;
    }

    public void setNumberOfSolvedTasks(String numberOfSolvedTasks) {
        this.numberOfSolvedTasks = numberOfSolvedTasks;
    }

    public String getDataOfLastSolvedTasks() {
        return dataOfLastSolvedTasks;
    }

    public void setDataOfLastSolvedTasks(String dataOfLastSolvedTasks) {
        this.dataOfLastSolvedTasks = dataOfLastSolvedTasks;
    }

    @Override
    public String toString() {
        return "TimusAuthor{" +
                "url=" + url +
                ", userName='" + userName + '\'' +
                ", userRang='" + userRang + '\'' +
                ", numberOfSolvedTasks='" + numberOfSolvedTasks + '\'' +
                ", dataOfLastSolvedTasks='" + dataOfLastSolvedTasks + '\'' +
                '}';
    }

    public void setAllUserDate(OriginalUserTimusData date) {
        String [] string = date.split();
        this.setUserName(string[0]);
        this.setUserRang(string[1]);
        this.setNumberOfSolvedTasks(string[2]);
        this.setDataOfLastSolvedTasks(string[3]);
    }
}

