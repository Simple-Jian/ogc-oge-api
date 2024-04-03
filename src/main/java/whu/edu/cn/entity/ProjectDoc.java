package whu.edu.cn.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whu.edu.cn.entity.process.Link;
import whu.edu.cn.entity.process.ModelResource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用工程实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(description = "应用工程的实体对象")
public class ProjectDoc {
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
        private String content;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Geometry{
        private String type;
        private String coordinates;
    }
    public ProjectDoc(TempProject project){
        this.id=project.getId();
        this.alias=project.getTitle();
        this.belongTo="projects";
        String []kw =project.getKeywords().split("[,，]");           //解析关键词
        Properties properties=new Properties(project.getTitle(),project.getDescription(),kw,
                LocalDateTime.now().toString(),LocalDateTime.now().toString(),project.getType(),
                project.getWebSiteContent());
        this.properties=properties;

        List<Link> links=new ArrayList<Link>();
        //本身
        links.add(new Link("http://openge.org.cn/Records_Api/clollections/projects/items/"+project.getId(),
                "self","applicaiton/json",project.getTitle()+"(Project Record)" ));
        //所属集合
        links.add(new Link("http://openge.org.cn/Records_Api/clollections/projects",
                "collection","applicaiton/json","Collection of project records" ));

        //上一条
        if(project.getId()!="project1") {
            Integer id=Integer.parseInt(project.getId().split("project")[1]);
            links.add(new Link("http://openge.org.cn/Records_Api/clollections/project/items/" + (id-1),
                    "prev", "applicaiton/json", "Previous project record"));
        }
        //下一条
        if(project.getId()!="project9")
        {
            Integer id=Integer.parseInt(project.getId().split("project")[1]);
            links.add(new Link("http://openge.org.cn/Records_Api/clollections/project/items/" + (id+1),
                    "next", "applicaiton/json", "Next project record"));
        }
        links.add(new Link(project.getUri().contains("http")?project.getUri():"http://openge.org.cn"+project.getUri(),
                "describes","text/html",project.getTitle() ));
        this.links=links;
    }
}