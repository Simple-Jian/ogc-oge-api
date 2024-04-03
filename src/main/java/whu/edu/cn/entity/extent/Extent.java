package whu.edu.cn.entity.extent;

import java.util.List;

public class Extent {
    private SpatialExtent spatial;
    private TemporalExtent temporal;

    public Extent(SpatialExtent spatial, TemporalExtent temporal) {
        this.spatial = spatial;
        this.temporal = temporal;
    }

    public SpatialExtent getSpatial() {
        return spatial;
    }

    public void setSpatial(SpatialExtent spatial) {
        this.spatial = spatial;
    }

    public TemporalExtent getTemporal() {
        return temporal;
    }

    public void setTemporal(TemporalExtent temporal) {
        this.temporal = temporal;
    }
//    private List<Double> spatial;
//    private List<String> temporal;
//
//    public List<Double> getSpatial() {
//        return spatial;
//    }
//
//    public void setSpatial(List<Double> spatial) {
//        this.spatial = spatial;
//    }
//
//    public List<String> getTemporal() {
//        return temporal;
//    }
//
//    public void setTemporal(List<String> temporal) {
//        this.temporal = temporal;
//    }
}
