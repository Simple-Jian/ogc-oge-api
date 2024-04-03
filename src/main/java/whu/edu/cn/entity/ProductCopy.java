package whu.edu.cn.entity;

import java.util.List;

/**
 * 原來的Product
 */
public class ProductCopy {
    private String productKey;
    private String productName;
    private String platformName;
    private String sensorName;
    private List<Measurement> measurements;
    private String crs;
    private String tileSize;
    private String cellRes;
    private String level;
    private String phenomenonTime;
    private String imagingLength;
    private String imagingWidth;
    private String resultTime;
    private String upperLeftLat;
    private String upperLeftLong;
    private String upperRightLat;
    private String upperRightLong;
    private String lowerLeftLat;
    private String lowerLeftLong;
    private String lowerRightLat;
    private String lowerRightLong;
    private Boolean isPublish;

    public ProductCopy(){}

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

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

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public String getTileSize() {
        return tileSize;
    }

    public void setTileSize(String tileSize) {
        this.tileSize = tileSize;
    }

    public String getCellRes() {
        return cellRes;
    }

    public void setCellRes(String cellRes) {
        this.cellRes = cellRes;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPhenomenonTime() {
        return phenomenonTime;
    }

    public void setPhenomenonTime(String phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    public String getImagingLength() {
        return imagingLength;
    }

    public void setImagingLength(String imagingLength) {
        this.imagingLength = imagingLength;
    }

    public String getImagingWidth() {
        return imagingWidth;
    }

    public void setImagingWidth(String imagingWidth) {
        this.imagingWidth = imagingWidth;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
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

    @Override
    public String toString() {
        return "ProductCopy{" +
                "productKey='" + productKey + '\'' +
                ", productName='" + productName + '\'' +
                ", platformName='" + platformName + '\'' +
                ", sensorName='" + sensorName + '\'' +
                ", measurements=" + measurements +
                ", crs='" + crs + '\'' +
                ", tileSize='" + tileSize + '\'' +
                ", cellRes='" + cellRes + '\'' +
                ", level='" + level + '\'' +
                ", phenomenonTime='" + phenomenonTime + '\'' +
                ", imagingLength='" + imagingLength + '\'' +
                ", imagingWidth='" + imagingWidth + '\'' +
                ", resultTime='" + resultTime + '\'' +
                ", upperLeftLat='" + upperLeftLat + '\'' +
                ", upperLeftLong='" + upperLeftLong + '\'' +
                ", upperRightLat='" + upperRightLat + '\'' +
                ", upperRightLong='" + upperRightLong + '\'' +
                ", lowerLeftLat='" + lowerLeftLat + '\'' +
                ", lowerLeftLong='" + lowerLeftLong + '\'' +
                ", lowerRightLat='" + lowerRightLat + '\'' +
                ", lowerRightLong='" + lowerRightLong + '\'' +
                '}';
    }
}
