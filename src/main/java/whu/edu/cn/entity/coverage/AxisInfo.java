package whu.edu.cn.entity.coverage;

public class AxisInfo {
    private String type;
    private String axisLabel;
    private Double lowerBound;
    private Double upperBound;
    private Double resolution;
    private String uomLabel;

    public AxisInfo(String type,String axisLabel, Double lowerBound, Double upperBound){
        this.type = type;
        this.axisLabel = axisLabel;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    public AxisInfo(String type,String axisLabel, Double lowerBound, Double upperBound, Double resolution){
        this.type = type;
        this.axisLabel = axisLabel;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.resolution = resolution;
    }

    public AxisInfo(String type,String axisLabel, Double lowerBound, Double upperBound, Double resolution, String uomLabel){
        this.type = type;
        this.axisLabel = axisLabel;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.resolution = resolution;
        this.uomLabel = uomLabel;
    }

    public Double getResolution() {
        return resolution;
    }

    public void setResolution(Double resolution) {
        this.resolution = resolution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAxisLabel() {
        return axisLabel;
    }

    public void setAxisLabel(String axisLabel) {
        this.axisLabel = axisLabel;
    }

    public Double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
    }

    public String getUomLabel() {
        return uomLabel;
    }

    public void setUomLabel(String uomLabel) {
        this.uomLabel = uomLabel;
    }
}
