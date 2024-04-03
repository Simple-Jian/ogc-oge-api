package whu.edu.cn.entity.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import whu.edu.cn.entity.catalog.TempApplicationModel;
import whu.edu.cn.entity.process.Link;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationModelDoc {
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
        public ApplicationModelDoc(TempApplicationModel applicationModel) {
            this.id = applicationModel.getId();
            this.alias = applicationModel.getAlias()==null?"无":applicationModel.getAlias();
            this.belongTo = "application-models";
            String[] kw = applicationModel.getKeywords().split("[,，]");           //解析关键词
            Properties properties = new Properties(applicationModel.getTitle(), applicationModel.getDescription(), kw,
                    LocalDateTime.now().toString(), LocalDateTime.now().toString(), applicationModel.getType(),
                    applicationModel.getWebSiteContent());
            this.properties = properties;

            List<Link> links = new ArrayList<Link>();
            //本身
            links.add(new Link("http://openge.org.cn/Records_Api/clollections/application-models/items/" + applicationModel.getId(),
                    "self", "applicaiton/json", applicationModel.getTitle() + "(Project Record)"));
            //所属集合
            links.add(new Link("http://openge.org.cn/Records_Api/clollections/application-models",
                    "collection", "applicaiton/json", "Collection of application model records"));

            //上一条
            if (applicationModel.getId() != "application-model1") {
                Integer id = Integer.parseInt(applicationModel.getId().split("application-model")[1]);
                links.add(new Link("http://openge.org.cn/Records_Api/clollections/applicationModel/items/" + (id - 1),
                        "prev", "applicaiton/json", "Previous application model record"));
            }
            //下一条
            if (applicationModel.getId() != "application-model17") {
                Integer id = Integer.parseInt(applicationModel.getId().split("application-model")[1]);
                links.add(new Link("http://openge.org.cn/Records_Api/clollections/applicationModel/items/" + (id + 1),
                        "next", "applicaiton/json", "Next application model record"));
            }
            links.add(new Link(applicationModel.getUri(),
                    "describes", "text/html", applicationModel.getTitle()));
            this.links = links;
        }
}
