package whu.edu.cn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import whu.edu.cn.entity.NameDao;
import whu.edu.cn.entity.VectorDao;
import whu.edu.cn.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ProductMapper {
    @Update("update oge_data_resource_product set is_publish=true where id=#{id}")
    void publishProductById(Integer id);

    @Update("update oge_data_resource_product set is_publish=false where id=#{id}")
    void retractProductById(Integer id);

    @Update("update oge_data_resource_product set is_publish=true")
    void pulishAll();

    @Update("update oge_data_resource_product set is_publish=false")
    void retractAll();

    @Select("Select * from oge_data_resource_product")
    List<Product> getAllProducts();

    @Select("Select * from gc_product where product_type = \'Vector\'                                                                                          " )
    List<VectorDao> getVectorProducts();

    @Select("Select measurement_key,band_num from oge_product_measurement where product_key = #{ProductKey}")
    List<Measurement> getMeasurements(Integer ProductKey);

    @Select("Select distinct measurement_name from \"MeasurementsAndProduct\" where product_name = #{ProductName}")
    List<MeasurementName> getMeasurementNamesByName(String ProductName);

    @Select("Select * from \"SensorLevelAndProduct\"where product_name = #{ProductName}" )
    List<ProductCopy> getEOProductsByName(String ProductName);

    @Select("Select * from gc_product where product_name = #{ProductName}" )
    List<VectorDao> getVectorProductsByName(String ProductName);


    @Select("Select distinct product_name,product_type from gc_product")
    List<NameDao> getAllProductNames();

    List<ProductCopy> getProductsByParams(@Param("productName") String ProductName, @Param("startTime") Timestamp StartTime, @Param("endTime") Timestamp EndTime, @Param("WKT") String WKT);


}
