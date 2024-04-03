package whu.edu.cn.entity.catalog.dataResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

import whu.edu.cn.entity.catalog.Time;
import whu.edu.cn.entity.process.Link;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 目录服务中产品对应的实体类
 */
@Data
public class ProductRecordDoc {
    private static int count=0;
    private int id;
    private String alias;
    private Time time;
    private String geometry;   // 占位
    private List<Link> links;
    private String owner;
    private String belongTo;
    private Properties properties;

    public ProductRecordDoc(ProductRecord productRecord){
        String baseUrl="http://openge.org.cn/Records_Api/";
        this.id=productRecord.getId();
        this.alias=productRecord.getAlias();
        this.owner=productRecord.getOwner()!=null? productRecord.getOwner() : "admin";
        this.belongTo="data-product";               //所属目录
        //设置links
        ArrayList<Link> links = new ArrayList<Link>();
        links.add(new Link(baseUrl+"collections/data-product/"+productRecord.getId(),
                "self","application/json","This record"));
        links.add(new Link(baseUrl+"collections/data-product",
                "collection","application/json","The collection this record belong to"));
        links.add(new Link(baseUrl+"collections/"+productRecord.getTitle()+"/items",
                "items","application/json","Data Catalog "+productRecord.getTitle()+" items"));
        this.links=links;
        //设置时间
        try{
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String startTime = productRecord.getStartTime();
            String endTime = productRecord.getEndTime();
            LocalDate start = LocalDate.parse(startTime, formatter);
            LocalDate end = LocalDate.parse(endTime, formatter);
            this.time.setInterval(new Time.DateRange(start,end));
        }catch (Exception e){
            //解析不成功不采取任何操作
        }
        //传感器
        Sensor sensor = new Sensor(productRecord.getSensorName(),
                productRecord.getSensorPlatForm(), productRecord.getSensorDescription());
        //波段
        List<MeasurementRecord> measurements = productRecord.getMeasurements();
        ArrayList<Band> bands = new ArrayList<>();
        try{
            if (measurements != null) {
                measurements.forEach(b -> {
                    bands.add(new Band(b.getMeasurementName(), b.getBandNum(), b.getPolarisation(),
                            b.getImageResolution()));
                });
            }
        }catch (Exception e){
            System.out.println("measurements为空!"+(++count));
        }

        //产品Image数
        String amount = productRecord.getImageAmount();
        if(amount!=null)amount=amount.replace("景","");
        Properties properties = new Properties(productRecord.getTitle(), "catalog", productRecord.getProductType(),
                productRecord.getLabel(), productRecord.getSubject(), productRecord.getDataSize(),
                amount, productRecord.getCoverArea(), productRecord.getResolution(),
                productRecord.getDescription(), new String[]{"数据产品", "目录"}, sensor, bands,
                LocalDateTime.now(), LocalDateTime.now()
        );
        this.properties=properties;

    }

    //    private List<Measurement> measurements;
//    private String crs;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Properties {
        private String title;
        private String type;
        private String productType;
        private String label;
        private String subject;
        private String dataSize;
        private String imageAmount;
        private String coverArea;
        private String resolution;
        private String description;
        private String[] keywords;
        private Sensor sensor;
        private List<Band> band;
        private LocalDateTime created;
        private LocalDateTime updated;
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    private class Sensor {
        private String name;
        private String platform;
        private String description;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Band {
        private String measurementName;
        private String bandNum;
        private String polarisation;
        private float resolution;
    }
}
