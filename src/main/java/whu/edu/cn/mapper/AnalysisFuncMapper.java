package whu.edu.cn.mapper;

import org.apache.ibatis.annotations.*;
import whu.edu.cn.entity.AnalysisFunc;

import java.util.List;

@Mapper
public interface AnalysisFuncMapper {
    //getCapabilities
    @Select("select * from analysis_function")
    public List<AnalysisFunc> getAllAnalysisFuncs();

    //getDescription
    @Select("select * from analysis_function where name=#{name}")
    public AnalysisFunc getAnalysisFuncByName(String name);

    @Select("select * from analysis_function where id=#{id}")
    public AnalysisFunc getAnalysisFuncById(String id);

    @Delete("delete from analysis_function where name=#{name}")
    public int deleteAnalysisFuncByName(String name);

    @Insert("insert into analysis_function(name, description) values(#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insertAnalysisFunc(AnalysisFunc analysisFunction);

    @Update("update analysis_function set description=#{description} where name=#{name}")
    public int updateAnalysisFunc(AnalysisFunc analysisFunction);

}
