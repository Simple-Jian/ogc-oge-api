package whu.edu.cn.entity;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    private Integer id;
    private String name;
    private String productType;
    private String dType;
    private List<Measurement> measurements;
    private String crs;
    private String senserKey;
    private String owner;
    private String label;
    private String description;
    private String alias;
    private String dataSize;
    private String imageAmount;
    private String startTime;
    private String endTime;
    private String coverArea;
    private String resolution;
    private Boolean isPublish;
}
