package whu.edu.cn.entity.catalog.dataResource;

import lombok.Data;

import java.util.List;

/**
 * 目录服务中产品对应的实体类
 */
@Data
public class ProductRecord {
    private int id;
    private String title;         //t1.name
    private String productType;
    private String owner;
    private  String label;
    private String description;
    private String subject;
    private String alias;
    private String dataSize;
    private String imageAmount;
    private String startTime;
    private String endTime;
    private String coverArea;
    private String resolution;
    private String sensorName;              //sensorkey对应的sensor
    private String sensorPlatForm;              //
    private String sensorDescription;
    private List<MeasurementRecord> measurements;

//    private List<Measurement> measurements;
//    private String crs;
}
