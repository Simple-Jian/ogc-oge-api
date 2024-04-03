package whu.edu.cn.entity;

public class VectorDao {
    private String productKey;
    private String productName;
    private String crs;
    private String phenomenonTime;

    private String upperLeftLat;
    private String upperLeftLong;
    private String upperRightLat;
    private String upperRightLong;
    private String lowerLeftLat;
    private String lowerLeftLong;
    private String lowerRightLat;
    private String lowerRightLong;

    private String properties;

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public String getPhenomenonTime() {
        return phenomenonTime;
    }

    public void setPhenomenonTime(String phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    public String getUpperLeftLat() {
        return upperLeftLat;
    }

    public void setUpperLeftLat(String upperLeftLat) {
        this.upperLeftLat = upperLeftLat;
    }

    public String getUpperLeftLong() {
        return upperLeftLong;
    }

    public void setUpperLeftLong(String upperLeftLong) {
        this.upperLeftLong = upperLeftLong;
    }

    public String getUpperRightLat() {
        return upperRightLat;
    }

    public void setUpperRightLat(String upperRightLat) {
        this.upperRightLat = upperRightLat;
    }

    public String getUpperRightLong() {
        return upperRightLong;
    }

    public void setUpperRightLong(String upperRightLong) {
        this.upperRightLong = upperRightLong;
    }

    public String getLowerLeftLat() {
        return lowerLeftLat;
    }

    public void setLowerLeftLat(String lowerLeftLat) {
        this.lowerLeftLat = lowerLeftLat;
    }

    public String getLowerLeftLong() {
        return lowerLeftLong;
    }

    public void setLowerLeftLong(String lowerLeftLong) {
        this.lowerLeftLong = lowerLeftLong;
    }

    public String getLowerRightLat() {
        return lowerRightLat;
    }

    public void setLowerRightLat(String lowerRightLat) {
        this.lowerRightLat = lowerRightLat;
    }

    public String getLowerRightLong() {
        return lowerRightLong;
    }

    public void setLowerRightLong(String lowerRightLong) {
        this.lowerRightLong = lowerRightLong;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}
