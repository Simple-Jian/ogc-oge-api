package whu.edu.cn.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.edu.cn.entity.catalog.dataResource.MeasurementRecord;
import whu.edu.cn.entity.catalog.dataResource.ProductRecord;

import java.util.List;

/**
 * 查询ProductRecord记录
 */
@Mapper
public interface ProductRecordMapper {
    List<ProductRecord> getAllProductRecords();

    List<MeasurementRecord> getMeasurementRecordByProductId(Integer productId);
}


