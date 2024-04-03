package whu.edu.cn.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import whu.edu.cn.bean.Image;
import whu.edu.cn.entity.coverage.*;
import whu.edu.cn.entity.extent.Extent;
import whu.edu.cn.entity.extent.SpatialExtent;
import whu.edu.cn.entity.extent.TemporalExtent;
import whu.edu.cn.entity.process.Link;
import whu.edu.cn.mapper.CoverageMapper;
import whu.edu.cn.service.CoverageService;
import whu.edu.cn.util.GeoUtil;
import whu.edu.cn.util.MinIOUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CoverageServiceImpl implements CoverageService {
    @Autowired
    private CoverageMapper coverageMapper;

    @Resource
    private GeoUtil geoUtil;

    @Resource
    private MinIOUtil minIOUtil;

    private static final String OGE_PATH = "http://125.220.153.22:9006/oge/";


    /**
     * 根据时间范围和空间范围查询Collection的元数据 并返回所有元数据的数组
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param WKT 空间范围的WKT
     * @param limit 限制条数
     * @return 返回JSONArray CollectionInfo的数组
     */
    @Override
    public JSONArray getCollectionMetaByParams(Timestamp startTime, Timestamp endTime, String WKT, int limit) {
        List<Image> images = coverageMapper.getCollectionMetaByParams(startTime, endTime, WKT, limit);
        JSONArray collections = new JSONArray();
        for(Image image : images){
//            List<String> bandNums = coverageMapper.getBandNumByProductKey(image.getProductKey());
//            for(String bandNum : bandNums){
//               Image newImage = new Image(image);
//               newImage.setImageIdentification(newImage.getImageIdentification() + bandNum);
//            }
            CollectionInfo collectionInfo = getCollectionInfoByImage(image);
            collections.add(JSONObject.parseObject(JSONObject.toJSONString(collectionInfo)));
        }
        return collections;
    }

    /**
     * Collection元信息
     * @param image Image 获取的影像实体
     * @return 返回CollectionInfo
     */
    private CollectionInfo getCollectionInfoByImage(Image image){
        CollectionInfo collectionInfo = new CollectionInfo();
        String imageIdentification = image.getBandNum()==null ? image.getImageIdentification():
                image.getImageIdentification() + "-" +image.getBandNum();
        collectionInfo.setId(imageIdentification);
        collectionInfo.setTitle(imageIdentification);
        List<List<Double>> bbox = new ArrayList<>();
        bbox.add(Arrays.asList(image.getLowerLeftLong(), image.getLowerLeftLat(), image.getLowerRightLong(), image.getUpperRightLat()));
        List<String> crs = Arrays.asList(geoUtil.getCRSHref(image.getCrs()));
        collectionInfo.setCrs(crs);
        SpatialExtent spatialExtent = new SpatialExtent(bbox, geoUtil.getCRSHref(image.getCrs()));
        List<List<String>> interval = new ArrayList<>();
        interval.add(Arrays.asList(image.getPhenomenonTime().toString(), image.getResultTime().toString()));
        TemporalExtent temporalExtent = new TemporalExtent(interval);
        Extent extent = new Extent(spatialExtent, temporalExtent);
        collectionInfo.setExtent(extent);
        List<Link> links = new ArrayList<>();
        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api", "root", "application/json", "The landing page of this server as JSON"));
        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api/collections/" + imageIdentification,
                "self", "application/json", "Detailed Coverage metadata in JSON"));
//        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api/collections/" + image.getImageIdentification(),
//                "collection", "application/json", "Detailed Coverage metadata in JSON"));
        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api/collections/" + imageIdentification + "/domainset",
                "http://www.opengis.net/def/rel/ogc/1.0/coverage-domainset", "application/json", "Coverage domain set of collection in JSON"));
        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api/collections/" + imageIdentification + "/rangetype",
                "http://www.opengis.net/def/rel/ogc/1.0/coverage-rangetype", "application/json", "Coverage range type of collection in JSON"));
        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api/collections/" + imageIdentification + "/rangeset",
                "http://www.opengis.net/def/rel/ogc/1.0/coverage-rangeset", "image/tiff; application=geotiff", "Coverage range set of collection in tif"));
        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api/collections/" + imageIdentification + "/coverage",
                "http://www.opengis.net/def/rel/ogc/1.0/coverage", "image/tiff; application=geotiff", "Coverage data"));
        collectionInfo.setLinks(links);
        return collectionInfo;
    }

    /**
     * DomainSet信息
     * @param image Image 获取的影像实体
     * @return 返回DomainSet
     */
    private DomainSet getDomainSetByImage(Image image){
        DomainSet domainSet = new DomainSet();
        domainSet.setType("DomainSet");
        GeneralGrid generalGrid = new GeneralGrid();
        generalGrid.setType("GeneralGridCoverage");
        generalGrid.setSrsName(geoUtil.getCRSHref(image.getCrs()));
        generalGrid.setAxisLabels(Arrays.asList("Long", "Lat"));
        AxisInfo axisInfoLong = new AxisInfo("RegularAxis", "Long", image.getLowerLeftLong(),
                image.getLowerRightLong(), image.getRowResolution(), image.getUnit());
        AxisInfo axisInfoLat = new AxisInfo("RegularAxis", "Lat", image.getLowerLeftLat(),
                image.getUpperRightLat(), image.getColResolution(), image.getUnit());
        generalGrid.setAxis(Arrays.asList(axisInfoLong, axisInfoLat));
        GridLimits gridLimits = new GridLimits();
        gridLimits.setType("GridLimits");
        gridLimits.setSrsName(geoUtil.getCRSHref(image.getCrs()));
        gridLimits.setAxisLabels(Arrays.asList("i", "j"));
        AxisInfo axisInfoI = new AxisInfo("IndexAxis", "i", 0.0,
                Double.parseDouble(String.valueOf(image.getWidth())));
        AxisInfo axisInfoJ = new AxisInfo("IndexAxis", "j", 0.0,
                Double.parseDouble(String.valueOf(image.getHeight())));
        gridLimits.setAxis(Arrays.asList(axisInfoI, axisInfoJ));
        generalGrid.setGridLimits(gridLimits);
        domainSet.setGeneralGrid(generalGrid);
        return domainSet;
    }

    /**
     * RangeType信息
     * @param image Image 获取的影像实体
     * @return 返回RangeType
     */
    private RangeType getRangeTypeByImage(Image image){
        RangeType rangeType = new RangeType();
        rangeType.setType("DataRecord");
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        if(image.getImageIdentification().contains("precipitation")){
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setId(1);
            fieldInfo.setType("Quantity");
            fieldInfo.setName("Meter [m]");
            fieldInfo.setUom(new Uom("UnitReference", "m"));
            fieldInfoList.add(fieldInfo);
        }else if(image.getImageIdentification().contains("temperature")){
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setId(1);
            fieldInfo.setType("Quantity");
            fieldInfo.setName("Temperature [K]");
            fieldInfo.setUom(new Uom("UnitReference", "K"));
            fieldInfoList.add(fieldInfo);
        }
        rangeType.setFieldInfoList(fieldInfoList);
        return rangeType;
    }

    @Override
    public CollectionInfo getCollectionsById(String name) {
        // 首先判断是否包含“-”，如果包含表示有波段
        String bandNum = null;
        if(name.contains("-")){
            bandNum = name.split("-")[1];
            name = name.split("-")[0];
        }
        CollectionInfo collectionInfo = new CollectionInfo();
        Image image = null;
        try {
            image = coverageMapper.getCollectionByName(name);
            image.setBandNum(bandNum);
            collectionInfo = getCollectionInfoByImage(image);
            DomainSet domainSet = getDomainSetByImage(image);
            collectionInfo.setDomainSet(domainSet);
            RangeType rangeType = getRangeTypeByImage(image);
            collectionInfo.setRangeType(rangeType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectionInfo;
    }

    /**
     * 通过CollectionId下载文件
     * @param name collection Id
     * @return 返回文件流
     * @throws IOException IO错误 出错则返回null
     */
    @Override
    public void downloadCollectionByName(String name, HttpServletResponse response) throws IOException {
        String bandNum = null;
        if(name.contains("-")){
            bandNum = name.split("-")[1];
            name = name.split("-")[0];
        }
        Image image = null;
        try {
            image = coverageMapper.downloadCollectionsByName(name);
            image.setBandNum(bandNum);
            minIOUtil.downloadFile(image, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据collection Id获取DomainSet
     * @param name coverageId
     * @return DomainSet 空间坐标系
     */
    @Override
    public DomainSet getDomainSetByName(String name){
        DomainSet domainSet = new DomainSet();
        try {
            Image image = coverageMapper.getCollectionByName(name);
            domainSet = getDomainSetByImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domainSet;
    }

    /**
     * 根据collection Id获取像素值的范围
     * @param name collection Id
     * @return RangeType 像素值的范围
     */
    @Override
    public RangeType getRangeTypeByName(String name){
        RangeType rangeType = new RangeType();
        try {
            Image image = coverageMapper.getCollectionByName(name);
            rangeType = getRangeTypeByImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rangeType;
    }
}
