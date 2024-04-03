package whu.edu.cn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whu.edu.cn.entity.coverage.CollectionInfo;
import whu.edu.cn.entity.coverage.DomainSet;
import whu.edu.cn.entity.coverage.RangeSet;
import whu.edu.cn.entity.coverage.RangeType;
import whu.edu.cn.entity.process.Link;
import whu.edu.cn.service.CoverageService;
import whu.edu.cn.util.GeoUtil;
import whu.edu.cn.util.TimeUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * OGC API - Coverages
 *
 * */
@Api(tags = "OGE - OGC API - Coverages")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/coverages_api")
public class CoverageController {

    @Resource
    private CoverageService coverageService;

    @Resource
    private ResourceLoader resourceLoader;

    @Resource
    private GeoUtil geoUtil;

    @Resource
    private TimeUtil timeUtil;

    /**
     * Landing page of geocube coverage API.
     */
    @ApiOperation(value = "Landing page", notes = "Landing page")
    @GetMapping(value = "/")
    public Map<String, Object> getLandingPage() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "OGE processing server");
        map.put("description", "OGE server implementing the OGC API - Coverages 1.0");

        List<Link> linkList = new ArrayList<>();
        Link self = new Link();
        self.setHref("http://oge.whu.edu.cn/ogcapi/coverages_api/");
        self.setRel("self");
        self.setType("application/json");
        self.setTitle("landing page");
        linkList.add(self);

        Link serviceDesc = new Link();
        serviceDesc.setHref("http://oge.whu.edu.cn/ogcapi/coverages_api/api");
        serviceDesc.setRel("service-desc");
        serviceDesc.setType("application/openapi+json;version=3.0");
        serviceDesc.setTitle("the API definition");
        linkList.add(serviceDesc);

        Link conformance = new Link();
        conformance.setHref("http://oge.whu.edu.cn/ogcapi/coverages_api/conformance");
        conformance.setRel("conformance");
        conformance.setType("application/json");
        conformance.setTitle("OGC API - Coverage conformance classes implemented by this server");
        linkList.add(conformance);

        Link processes = new Link();
        processes.setHref("http://oge.whu.edu.cn/ogcapi/coverages_api/collections");
        processes.setRel("coverage");
        processes.setType("application/json");
        processes.setTitle("The list of available collections");
        linkList.add(processes);

        map.put("links", linkList);
        return map;
    }

    /**
     * The OpenAPI definition as JSON.
     */
    @ApiOperation(value = "Open api", notes = "The OpenAPI definition as JSON")
    @GetMapping(value = "/api")
    public Map<String, Object> getOpenAPI() throws IOException {
        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:static/openAPI.json");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(resource.getInputStream(), Map.class);
    }

    /**
     * Information about standards that this API conforms to.
     */
    @ApiOperation(value = "Conformance class", notes = "Conformance class")
    @GetMapping(value = "/conformance")
    public Map<String, Object> getConformanceClasses() {
        Map<String, Object> map = new HashMap<>();
        List<String> comformList = new ArrayList<>();
        comformList.add("http://www.opengis.net/spec/ogcapi-common-1/1.0/conf/core");
        comformList.add("http://www.opengis.net/spec/ogcapi-common-1/1.0/conf/json");
        comformList.add("http://www.opengis.net/spec/ogcapi-common-1/1.0/conf/oas3");
        comformList.add("http://www.opengis.net/spec/ogcapi-common-2/1.0/conf/geodata");
        comformList.add("http://www.opengis.net/spec/ogcapi-common-2/1.0/conf/collections");
        comformList.add("http://www.opengis.net/spec/ogcapi-coverages-1/1.0/conf/geodata-coverage");
        comformList.add("http://www.opengis.net/spec/ogcapi-coverages-1/1.0/conf/geodata-coverage");
        comformList.add("http://www.opengis.net/spec/ogcapi-coverages-1/1.0/conf/geodata-subset");
        comformList.add("http://www.opengis.net/spec/ogcapi-coverages-1/1.0/conf/geodata-bbox");
        comformList.add("http://www.opengis.net/spec/ogcapi-coverages-1/1.0/conf/geodata-datetime");
        map.put("conformsTo", comformList);
        return map;
    }

    /**
     * Describe the collections in the dataset.
     * @param limit 限制的条数
     * @param bbox 空间范围框 160.6,-55.95,-170,-25.89
     * @param time 时间范围 2016-08-30T02:55:50Z/2018-08-30T02:55:50Z
     * @return
     * @throws ParseException
     */
    @ApiOperation(value = "Collections provided", notes = "Collections list")
    @GetMapping(value = "/collections")
    public JSONObject describeCollections(
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "bbox", required = false) String bbox,
            @RequestParam(value = "time", required = false) String time) throws ParseException {

        double minx = -179;
        double miny = -89;
        double maxx = 179;
        double maxy = 89;
        if(bbox !=null){
            String [] bboxList = bbox.split(",");
            minx = Double.parseDouble(bboxList[0]);
            miny = Double.parseDouble(bboxList[1]);
            maxx = Double.parseDouble(bboxList[2]);
            maxy = Double.parseDouble(bboxList[3]);
        }
        String startTime = null;
        String endTime = null;
        if(time != null){
            String [] timeList = time.split("/");
            startTime = timeUtil.convertTime(timeList[0]);
            endTime = timeUtil.convertTime(timeList[1]);
        }
        //先判断参数是否正确
        if (minx > 180 || minx < -180) {
            System.out.println(minx + " is not in global extent!");
            return null;
        }
        if (maxx > 180 || maxx < -180) {
            System.out.println(maxx + " is not in global extent!");
            return null;
        }
        if (miny > 90 || miny < -90) {
            System.out.println(miny + " is not in global extent!");
            return null;
        }
        if (maxy > 90 || maxy < -90) {
            System.out.println(maxy + " is not in global extent!！");
            return null;
        }

        String WKT_rec = "";
        if (minx == -180.0 && miny == -90.0 && maxx == 180.0 && maxy == 90.0) {
            System.out.println("Use default global spatial extent!");
        } else {
            WKT_rec = geoUtil.DoubleToWKT(minx, miny, maxx, maxy);
        }
        System.out.println(WKT_rec);

        Timestamp startTimeT;
        if (startTime == null || startTime.equals("")) {
            startTimeT = Timestamp.valueOf("2000-01-01 00:00:00");
        } else {
            startTimeT = Timestamp.valueOf(startTime);
        }
        System.out.println(startTimeT);

        Timestamp endTimeT;
        if (endTime == null || endTime.equals("")) {
            endTimeT = new Timestamp(System.currentTimeMillis());
        } else {
            endTimeT = Timestamp.valueOf(endTime);
        }
        System.out.println(endTimeT);

//        if ("null".equals(String.valueOf(limit)) || "0".equals(String.valueOf(limit)) || limit <= 0) {
//            limit = 10;
//        }

        JSONArray collectionInfoArray = coverageService.getCollectionMetaByParams(startTimeT, endTimeT, WKT_rec, limit);
        JSONObject resultObj = new JSONObject();
        resultObj.put("collections", collectionInfoArray);
        List<Link> links = new ArrayList<>();
        links.add(new Link("http://oge.whu.edu.cn/ogcapi/coverages_api/collections", "self", "application/json", "This document as JSON"));
        resultObj.put("links", links);
        return resultObj;
    }

    /**
     * Collection Information is the set of metadata which describes a single collection, or in the the case of API-Coverages, a single Coverage.
     *
     * @param name collectionId
     * @return CollectionInfo
     */
    @ApiOperation(value = "Collection description", notes = "Collection description")
    @GetMapping(value = "/collections/{collectionId}")
    public JSONObject describeCoverage(@PathVariable("collectionId") String name) {
        CollectionInfo collectionInfo = coverageService.getCollectionsById(name);
        return JSONObject.parseObject(JSONObject.toJSONString(collectionInfo));
    }

    /**
     * Description of the coverage offering identified by {coverageid} including envelope.
     *
     * @param name collectionId
     * @throws IOException IO
     */
    @ApiOperation(value = "Get a coverage", notes = "Return the coverage")
    @GetMapping(value = "/collections/{collectionId}/coverage")
    public void getCoverageOffering(@PathVariable("collectionId") String name, HttpServletResponse response) throws IOException {
       coverageService.downloadCollectionByName(name, response);
    }

//    /**
//     * Description of the coverage png identified by {coverageid} including envelope.
//     *
//     * @param name
//     * @param bbox bounding box
//     * @param subset String format of bounding box
//     * @return
//     * @throws ParseException
//     */
//    @ApiOperation(value = "Png format of a coverage", notes = "Return the coverage with png format")
//    @GetMapping(value = "/collections/{coverageId}/coverage.png", produces = MediaType.IMAGE_PNG_VALUE)
//    @ResponseBody
//    public byte[] getCoveragePng(@PathVariable("coverageId") String name,
//                                 @RequestParam(value = "bbox", required = false) String bbox,
//                                 @RequestParam(value = "subset", required = false) String[] subset) throws ParseException{
//
//        return null;
//    }

    /**
     * Retrieve a coverages domainset; use content negotiation to request HTML or GeoJSON.
     *
     * @param name collection Id
     * @return domainSet of the collection
     */
    @ApiOperation(value = "Domainset of a coverage", notes = "Domainset of a coverage")
    @GetMapping(value = "/collections/{coverageId}/coverage/domainset")
    public JSONObject getCoverageDomainSet(@PathVariable("coverageId") String name){
        DomainSet domainSet = coverageService.getDomainSetByName(name);
       return JSONObject.parseObject(JSONObject.toJSONString(domainSet));
    }

    /**
     * Retrieve a coverages rangetype; use content negotiation to request HTML or GeoJSON.
     *
     * @param name rangeType
     * @return rangeType
     */
    @ApiOperation(value = "Rangetype of a coverage", notes = "Rangetype of a coverage")
    @GetMapping(value = "/collections/{coverageId}/coverage/rangetype")
    public JSONObject getCoverageRangetype(@PathVariable("coverageId") String name){
        RangeType rangeType = coverageService.getRangeTypeByName(name);
        return JSONObject.parseObject(JSONObject.toJSONString(rangeType));
    }

    /**
     * Retrieve a coverages rangeset in geotif.
     *
     * @param name collectionId
     * @throws IOException IO
     */
    @ApiOperation(value = "Rangeset of a coverage", notes = "Rangeset of a coverage")
    @GetMapping(value = "/collections/{collectionId}/coverage/rangeset")
    public void getCoverageRangeSet(@PathVariable("collectionId") String name, HttpServletResponse response) throws IOException {
        coverageService.downloadCollectionByName(name, response);
    }

//    @GetMapping("/download-image")
//    public ResponseEntity<InputStreamResource> downloadImage() throws IOException {
//        String imageUrl = "http://125.220.153.22:8027/test_data/Sentinel2_GEE/GEE_S2A_MSIL1C_20201022T031801_N0209_R118_T49RCN_20201022T052801.tif"; // 影像的下载链接
//        URL url = new URL(imageUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        InputStream inputStream = connection.getInputStream();
//        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.tif");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(inputStreamResource);
//    }

}
