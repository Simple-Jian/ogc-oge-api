package whu.edu.cn.entity.process.boundingbox;

import java.util.List;

public class Input{
    private List<Double> bbox;
    private String crs;

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }
}
