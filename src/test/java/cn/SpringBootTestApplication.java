//package cn;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import whu.edu.cn.OGEAPIBootApplication;
//import whu.edu.cn.entity.catalog.dataResource.MeasurementRecord;
//import whu.edu.cn.entity.catalog.dataResource.ProductRecord;
//import whu.edu.cn.mapper.ProductRecordMapper;
//
//import java.util.List;
//@SpringBootTest(classes = OGEAPIBootApplication.class)
//public class SpringBootTestApplication {
//    @Autowired
//    private ProductRecordMapper productRecordMapper;
//    @Test
//    void testGetProductRecord(){
//        List<ProductRecord> allProductRecords = productRecordMapper.getAllProductRecords();
//        for (ProductRecord productRecord : allProductRecords) {
//            int id = productRecord.getId();
//            List<MeasurementRecord> measurementRecords= productRecordMapper.getMeasurementRecordByProductId(id);
//            productRecord.setMeasurements(measurementRecords);
//        }
//    }
//
//}
