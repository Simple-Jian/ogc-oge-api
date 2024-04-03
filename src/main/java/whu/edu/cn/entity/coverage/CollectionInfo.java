package whu.edu.cn.entity.coverage;

import com.alibaba.fastjson.annotation.JSONField;
import whu.edu.cn.entity.extent.Extent;
import whu.edu.cn.entity.process.Link;

import java.util.List;

public class CollectionInfo {
    private String id; //required
    private String title;
    private String description;
    private Extent extent;
    private String itemType;
    private List<String> crs;
    private DomainSet domainSet;
    private RangeType rangeType;
    private List<Link> links; //required

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Extent getExtent() {
        return extent;
    }

    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public List<String> getCrs() {
        return crs;
    }

    public void setCrs(List<String> crs) {
        this.crs = crs;
    }

    public DomainSet getDomainSet() {
        return domainSet;
    }

    public void setDomainSet(DomainSet domainSet) {
        this.domainSet = domainSet;
    }

    public RangeType getRangeType() {
        return rangeType;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
