package whu.edu.cn.entity.coverage;

import java.util.List;

public class GridLimits {
    private String type;
    private String id;
    private String srsName;
    private List<String> axisLabels;
    private List<AxisInfo> axis;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrsName() {
        return srsName;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    public List<String> getAxisLabels() {
        return axisLabels;
    }

    public void setAxisLabels(List<String> axisLabels) {
        this.axisLabels = axisLabels;
    }

    public List<AxisInfo> getAxis() {
        return axis;
    }

    public void setAxis(List<AxisInfo> axis) {
        this.axis = axis;
    }
}
