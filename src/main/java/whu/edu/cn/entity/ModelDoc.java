package whu.edu.cn.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whu.edu.cn.entity.process.Link;
import whu.edu.cn.entity.process.ModelResource;
import whu.edu.cn.entity.process.ModelResourceParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(description = "算子中心的实体对象")
public class ModelDoc {
    private String id;
    private String alias;
    private List<Link> links;
    private String time;
    private Geometry geometry;
    private String belongTo;
    private Properties properties;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Properties{
        private String title;
        private String description;
        private String[] keywords;
        private String created;
        private String updated;
        private String type;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Geometry{
        private String type;
        private String coordinates;
    }
    public ModelDoc(ModelResource model){
        this.id=model.getName();
        this.alias=model.getAlias();
        this.belongTo="models";
        String []kw ={"模型资源","model"};
        Properties properties=new Properties(model.getName(),model.getDescription(),kw,
                LocalDateTime.now().toString(),LocalDateTime.now().toString(),"model");
        this.properties=properties;

        List<Link> links=new ArrayList<Link>();
        links.add(new Link("http://openge.org.cn/Records_Api/clollections/model/items/"+model.getName(),
                "self","applicaiton/json","Model record" ));
        this.links=links;
    }
}
