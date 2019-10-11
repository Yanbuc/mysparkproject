package domain;

public class SessionRandomExtract {
    private int taskId;
    private String sessionId;
    private String startTime;
    private  String searchKeyWords;
    private String clickCatgeoryIds;
    public  SessionRandomExtract(){}
    public SessionRandomExtract(int taskId,String  sessionId,String startTime,String searchKeyWords,String clickCatgeoryIds){
        this.taskId=taskId;
        this.sessionId=sessionId;
        this.startTime=startTime;
        this.searchKeyWords=searchKeyWords;
        this.clickCatgeoryIds=clickCatgeoryIds;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSearchKeyWords() {
        return searchKeyWords;
    }

    public void setSearchKeyWords(String searchKeyWords) {
        this.searchKeyWords = searchKeyWords;
    }

    public String getClickCatgeoryIds() {
        return clickCatgeoryIds;
    }

    public void setClickCatgeoryIds(String clickCatgeoryIds) {
        this.clickCatgeoryIds = clickCatgeoryIds;
    }
}
