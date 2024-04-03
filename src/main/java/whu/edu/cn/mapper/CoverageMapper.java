package whu.edu.cn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import whu.edu.cn.bean.Image;
import java.sql.Timestamp;
import java.util.List;

@Service
@Mapper
public interface CoverageMapper {
    /**
     * 查询符合条件的collection元信息
     */
    List<Image> getCollectionMetaByParams(@Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime,
                                          @Param("WKT") String WKT, @Param("limit") int limit);


    /**
     * 根据CoverageId获取一个collections
     */
    @Select("select t1.image_id as imageId, t1.product_key as productKey, t1.image_identification as imageIdentification, " +
            "t1.path as path, t1.crs as crs, t1.map_projection as mapProjection, t1.phenomenon_time as phenomenonTime," +
            "t1.result_time as resultTime, t1.upper_left_lat as upperLeftLat, t1.upper_left_long as upperLeftLong, " +
            "t1.upper_right_lat as upperRightLat, t1.upper_right_long as upperRightLong, t1.lower_left_lat as lowerLeftLat, " +
            "t1.lower_left_long as lowerLeftLong, t1.lower_right_lat as lowerRightLat, t1.lower_right_long as lowerRightLong," +
            "t1.unit=unit, t1.row_resolution as rowResolution, t1.col_resolution as colResolution, t1.height as height, " +
            "t1.width as width from oge_image as t1 where t1.image_identification=#{name}")
    Image getCollectionByName(@Param("name") String name);


    @Select("select t1.path as path,t1.image_identification as imageIdentification from oge_image as t1 where t1.image_identification=#{name}")
    Image downloadCollectionsByName(@Param("name") String name);

    @Select("select band_num from oge_product_measurement where product_key=#{productKey}")
    List<String> getBandNumByProductKey(@Param("productKey") int productKey);
}
