package whu.edu.cn.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import whu.edu.cn.entity.process.ModelResource;
import whu.edu.cn.entity.process.ModelResourceParam;

import java.util.List;

@Mapper
public interface ProcessMapper {
    /**
     * 根据限制条数获取计算模型资源
     */

    List<ModelResource> getModelResources(Integer limit);

    /**
     * 根据name获取计算模型资源
     * @param name
     * @return
     */
    @Select("select * from oge_model_resource where name=#{name}")
    ModelResource getProcessByName(String name);

    @Select("select * from oge_model_resource_param where id=#{id}")
    ModelResourceParam getParamById(int id);

}
