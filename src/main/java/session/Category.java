package session;

public class Category implements Comparable<Category>{
    private long categoryId;
    private long payCatgoryCount;
    private long clickCategoryCount;
    private long orderCategoryCount;

    public Category(){}
    public Category(long categoryId,long payCatgoryCount,long orderCategoryCount,long clickCategoryCount){
        this.categoryId=categoryId;
        this.payCatgoryCount=payCatgoryCount;
        this.orderCategoryCount=orderCategoryCount;
        this.clickCategoryCount=clickCategoryCount;
    }
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getPayCatgoryCount() {
        return payCatgoryCount;
    }

    public void setPayCatgoryCount(long payCatgoryCount) {
        this.payCatgoryCount = payCatgoryCount;
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

    @Override
    public String toString() {
        return this.categoryId+" "+this.payCatgoryCount+" "+this.orderCategoryCount+" "+this.clickCategoryCount;
    }

    @Override
    public int compareTo(Category o) {
       if(payCatgoryCount!=o.getPayCatgoryCount()){
           return payCatgoryCount-o.getPayCatgoryCount() > 0 ? 1:-1;
       }else{
           if(orderCategoryCount!=o.getOrderCategoryCount()){
               return orderCategoryCount-o.getOrderCategoryCount() >0 ?1:-1;
           }else{
               if(clickCategoryCount>=o.getPayCatgoryCount()){
                   return 1;
               }else{
                   return -1;
               }
           }
       }

    }
}
