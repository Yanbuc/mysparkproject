package domain;

public class PageSplitConvertRate {
    private int taskId;
    private String convertRate;

    public PageSplitConvertRate(){}
    public PageSplitConvertRate(int taskId,String convertRate){
        this.taskId=taskId;
        this.convertRate=convertRate;
    }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(String convertRate) {
        this.convertRate = convertRate;
    }
}
