package whu.edu.cn.entity;

import java.io.Serializable;

public class InputParams implements Serializable {
    private String rasterProductName;
    private String vectorProductName;
    private String extent;
    private String startTime;
    private String endTime;

    public String getRasterProductName() {
        return rasterProductName;
    }

    public void setRasterProductName(String rasterProductName) {
        this.rasterProductName = rasterProductName;
    }

    public String getVectorProductName() { return vectorProductName; }

    public void setVectorProductName(String vectorProductName) { this.vectorProductName = vectorProductName; }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "InputParams{" +
                "rasterProductName='" + rasterProductName + '\'' +
                ", extent='" + extent + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
