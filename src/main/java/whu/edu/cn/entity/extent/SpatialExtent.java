package whu.edu.cn.entity.extent;

import java.util.List;

public class SpatialExtent {
    /**
     * bbox of the image, example: [[ -180,-90,180, 90]]
     */
    List<List<Double>> bbox;

    /**
     * example: http://www.opengis.net/def/crs/OGC/1.3/CRS84
     */
    String crs;

    public SpatialExtent(List<List<Double>> bbox, String crs) {
        this.bbox = bbox;
        this.crs = crs;
    }

    public List<List<Double>> getBbox() {
        return bbox;
    }

    public void setBbox(List<List<Double>> bbox) {
        this.bbox = bbox;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }
}
