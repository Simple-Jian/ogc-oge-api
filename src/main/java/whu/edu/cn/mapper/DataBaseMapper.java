package whu.edu.cn.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import whu.edu.cn.entity.process.ModelResource;

import java.util.List;

/**
 * 百度云数据库Mapper
 */
@Mapper
public interface DataBaseMapper {
    @Select("select * from oge_model_resource")
    public List<ModelResource> getAllModels();
}
