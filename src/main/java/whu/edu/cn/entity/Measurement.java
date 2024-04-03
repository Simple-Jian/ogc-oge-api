package whu.edu.cn.entity;

import lombok.Data;

@Data
public class Measurement {
    private String measurementKey;
    private String measurementName;
    private String dType;
    private String bandNum;

    public Measurement(){}

    public String getMeasurementKey() {
        return measurementKey;
    }

    public void setMeasurementKey(String measurementKey) {
        this.measurementKey = measurementKey;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }
}
