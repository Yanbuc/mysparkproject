package domain;

public class AreaTop3Product {
     private  int taskId;
     private String area;
     private String areaLevel;
     private long product_id;
     private  String cityNames;
     private int clickCount;
     private String productName;
     private String productStatus;

     public AreaTop3Product(){

     }
     public AreaTop3Product(int taskId,String area,String areaLevel,long productId,String ctyNames,int clickCount,
                            String productName,String productStatus){
         this.taskId=taskId;
         this.area=area;
         this.areaLevel=areaLevel;
         this.product_id=productId;
         this.cityNames=ctyNames;
         this.clickCount=clickCount;
         this.productName=productName;
         this.productStatus=productStatus;
     }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(String areaLevel) {
        this.areaLevel = areaLevel;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getCityNames() {
        return cityNames;
    }

    public void setCityNames(String cityNames) {
        this.cityNames = cityNames;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
