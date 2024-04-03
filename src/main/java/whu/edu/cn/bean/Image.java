package whu.edu.cn.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

@ApiModel(description = "影像表的实体对象")
public class Image {
    //来自影像表
    @ApiModelProperty(value = "影像编号")
    private int imageId;
    @ApiModelProperty(value = "产品编号")
    private int productKey;
    @ApiModelProperty(value = "影像描述")
    private String imageIdentification;
    @ApiModelProperty(value = "影像MinIO路径")
    private String path;
    @ApiModelProperty(value = "影像坐标系")
    private String crs;
    @ApiModelProperty(value = "云量")
    private Double coverCloud;
    @ApiModelProperty(value = "投影方式")
    private String mapProjection;
    @ApiModelProperty(value = "UTM编号")
    private String utmZone;
    @ApiModelProperty(value = "开始时间")
    private Timestamp phenomenonTime;
    @ApiModelProperty(value = "结束时间")
    private Timestamp resultTime;
    @ApiModelProperty(value = "左上纬度")
    private Double upperLeftLat;
    @ApiModelProperty(value = "左上经度")
    private Double upperLeftLong;
    @ApiModelProperty(value = "右上纬度")
    private Double upperRightLat;
    @ApiModelProperty(value = "右上经度")
    private Double upperRightLong;
    @ApiModelProperty(value = "左下纬度")
    private Double lowerLeftLat;
    @ApiModelProperty(value = "左下经度")
    private Double lowerLeftLong;
    @ApiModelProperty(value = "右下纬度")
    private Double lowerRightLat;
    @ApiModelProperty(value = "右下经度")
    private Double lowerRightLong;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    private String bandNum;
    private int bandRank;
    private String unit;
    private Double rowResolution;
    private Double colResolution;
    private int height;
    private int width;

    //来自产品表
    private String productName;
    private int sensorKey;
    private String label;
    private Integer landCoverAdapt;
    //来自传感器表
    private String sensorName;
    private String platformName;

//    public Image(Image image) {
//        this.imageId = image.getImageId();
//        this.productKey = image.getProductKey();
//        this.imageIdentification = image.getImageIdentification();
//        this.path = image.getPath();
//        this.crs = image.getCrs();
//        this.coverCloud = image.getCoverCloud();
//        this.mapProjection = image.getMapProjection();
//        this.utmZone = image.getUtmZone();
//        this.phenomenonTime = image.getPhenomenonTime();
//        this.resultTime = image.getResultTime();
//        this.upperLeftLat = image.getUpperLeftLat();
//        this.upperLeftLong = image.getUpperLeftLong();
//        this.upperRightLat = image.getUpperRightLat();
//        this.upperRightLong = image.getUpperRightLong();
//        this.lowerLeftLat = image.getLowerLeftLat();
//        this.lowerLeftLong = image.getLowerLeftLong();
//        this.lowerRightLat = image.getLowerRightLat();
//        this.lowerRightLong = image.getLowerRightLong();
//        this.createBy = image.getCreateBy();
//        this.createTime = image.getCreateTime();
//        this.updateBy = image.getUpdateBy();
//        this.updateTime = image.getUpdateTime();
//        this.unit = image.getUnit();
//        this.rowResolution = image.getRowResolution();
//        this.colResolution = image.getColResolution();
//        this.height = image.getHeight();
//        this.width = image.getWidth();
//        this.productName = image.getProductName();
//        this.sensorKey = image.getSensorKey();
//        this.label = image.getLabel();
//        this.landCoverAdapt = image.getLandCoverAdapt();
//        this.sensorName = image.getSensorName();
//        this.platformName = image.getPlatformName();
//    }


    public int getBandRank() {
        return bandRank;
    }

    public void setBandRank(int bandRank) {
        this.bandRank = bandRank;
    }

    public String getBandNum() {
        return bandNum;
    }

    public void setBandNum(String bandNum) {
        this.bandNum = bandNum;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getProductKey() {
        return productKey;
    }

    public void setProductKey(int productKey) {
        this.productKey = productKey;
    }

    public String getImageIdentification() {
        return imageIdentification;
    }

    public void setImageIdentification(String imageIdentification) {
        this.imageIdentification = imageIdentification;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    public Double getCoverCloud() {
        return coverCloud;
    }

    public void setCoverCloud(Double coverCloud) {
        this.coverCloud = coverCloud;
    }

    public String getMapProjection() {
        return mapProjection;
    }

    public void setMapProjection(String mapProjection) {
        this.mapProjection = mapProjection;
    }

    public String getUtmZone() {
        return utmZone;
    }

    public void setUtmZone(String utmZone) {
        this.utmZone = utmZone;
    }

    public Timestamp getPhenomenonTime() {
        return phenomenonTime;
    }

    public void setPhenomenonTime(Timestamp phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    public Timestamp getResultTime() {
        return resultTime;
    }

    public void setResultTime(Timestamp resultTime) {
        this.resultTime = resultTime;
    }

    public Double getUpperLeftLat() {
        return upperLeftLat;
    }

    public void setUpperLeftLat(Double upperLeftLat) {
        this.upperLeftLat = upperLeftLat;
    }

    public Double getUpperLeftLong() {
        return upperLeftLong;
    }

    public void setUpperLeftLong(Double upperLeftLong) {
        this.upperLeftLong = upperLeftLong;
    }

    public Double getUpperRightLat() {
        return upperRightLat;
    }

    public void setUpperRightLat(Double upperRightLat) {
        this.upperRightLat = upperRightLat;
    }

    public Double getUpperRightLong() {
        return upperRightLong;
    }

    public void setUpperRightLong(Double upperRightLong) {
        this.upperRightLong = upperRightLong;
    }

    public Double getLowerLeftLat() {
        return lowerLeftLat;
    }

    public void setLowerLeftLat(Double lowerLeftLat) {
        this.lowerLeftLat = lowerLeftLat;
    }

    public Double getLowerLeftLong() {
        return lowerLeftLong;
    }

    public void setLowerLeftLong(Double lowerLeftLong) {
        this.lowerLeftLong = lowerLeftLong;
    }

    public Double getLowerRightLat() {
        return lowerRightLat;
    }

    public void setLowerRightLat(Double lowerRightLat) {
        this.lowerRightLat = lowerRightLat;
    }

    public Double getLowerRightLong() {
        return lowerRightLong;
    }

    public void setLowerRightLong(Double lowerRightLong) {
        this.lowerRightLong = lowerRightLong;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getRowResolution() {
        return rowResolution;
    }

    public void setRowResolution(Double rowResolution) {
        this.rowResolution = rowResolution;
    }

    public Double getColResolution() {
        return colResolution;
    }

    public void setColResolution(Double colResolution) {
        this.colResolution = colResolution;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSensorKey() {
        return sensorKey;
    }

    public void setSensorKey(int sensorKey) {
        this.sensorKey = sensorKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getLandCoverAdapt() {
        return landCoverAdapt;
    }

    public void setLandCoverAdapt(Integer landCoverAdapt) {
        this.landCoverAdapt = landCoverAdapt;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", productKey=" + productKey +
                ", imageIdentification='" + imageIdentification + '\'' +
                ", path='" + path + '\'' +
                ", crs='" + crs + '\'' +
                ", coverCloud=" + coverCloud +
                ", mapProjection='" + mapProjection + '\'' +
                ", utmZone='" + utmZone + '\'' +
                ", phenomenonTime=" + phenomenonTime +
                ", resultTime=" + resultTime +
                ", upperLeftLat=" + upperLeftLat +
                ", upperLeftLong=" + upperLeftLong +
                ", upperRightLat=" + upperRightLat +
                ", upperRightLong=" + upperRightLong +
                ", lowerLeftLat=" + lowerLeftLat +
                ", lowerLeftLong=" + lowerLeftLong +
                ", lowerRightLat=" + lowerRightLat +
                ", lowerRightLong=" + lowerRightLong +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", unit='" + unit + '\'' +
                ", rowResolution=" + rowResolution +
                ", colResolution=" + colResolution +
                ", height=" + height +
                ", width=" + width +
                ", productName='" + productName + '\'' +
                ", sensorKey=" + sensorKey +
                ", label='" + label + '\'' +
                ", landCoverAdapt=" + landCoverAdapt +
                ", sensorName='" + sensorName + '\'' +
                ", platformName='" + platformName + '\'' +
                '}';
    }
}
