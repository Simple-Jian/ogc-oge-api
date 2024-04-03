package whu.edu.cn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempProject {
    private String id;
    private String belongTo;
    private String type;
    private String title;
    private String description;
    private String keywords;
    private String webSiteContent;
    private String uri;
    private String image;
}
