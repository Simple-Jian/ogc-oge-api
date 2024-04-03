package whu.edu.cn.service;

import java.io.IOException;

public interface ProcessService1 {
    //Give a ShapeFile and return the Buffered result in geojson,return the url
    public String myBuffer(String url, Double distance) throws Exception;
    //Give a ShapeFile and return the result in geojson, return the url
    public String ShpToGeojson(String url) throws Exception;
}
