package domain;

public class Top10CategorySession {
    private int taskId;
    private String sessionId;
    private long categoryId;
    private long clickCount;

    public Top10CategorySession(){}
    public Top10CategorySession(int taskId,String sessionId,long categoryId,long clickCount){
       this.taskId=taskId;
       this.sessionId=sessionId;
       this.categoryId=categoryId;
       this.clickCount=clickCount;
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

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    @Override
    public String toString() {
        return this.taskId+" "+this.sessionId+" "+this.clickCount;
    }
}
