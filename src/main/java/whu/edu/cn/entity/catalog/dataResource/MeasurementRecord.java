package whu.edu.cn.entity.catalog.dataResource;

import lombok.Data;

@Data
public class MeasurementRecord {
    private String measurementKey;
    private String measurementName;
    private String polarisation;
    private String bandNum;
    private float imageResolution;
}
