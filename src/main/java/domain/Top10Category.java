package domain;

public class Top10Category {
    private int taskId;
    private long categoryId;
    private long clickCategoryCount;
    private long orderCategoryCount;
    private long payCategoryCount;

    public Top10Category(){}
    public Top10Category(int taskId,long categoryId,long payCategoryCount,long orderCategoryCount,long clickCategoryCount){
        this.taskId=taskId;
        this.categoryId=categoryId;
        this.payCategoryCount=payCategoryCount;
        this.orderCategoryCount=orderCategoryCount;
        this.clickCategoryCount=clickCategoryCount;
    }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getClickCategoryCount() {
        return clickCategoryCount;
    }

    public void setClickCategoryCount(long clickCategoryCount) {
        this.clickCategoryCount = clickCategoryCount;
    }

    public long getOrderCategoryCount() {
        return orderCategoryCount;
    }

    public void setOrderCategoryCount(long orderCategoryCount) {
        this.orderCategoryCount = orderCategoryCount;
    }

    public long getPayCategoryCount() {
        return payCategoryCount;
    }

    public void setPayCategoryCount(long payCategoryCount) {
        this.payCategoryCount = payCategoryCount;
    }
}
