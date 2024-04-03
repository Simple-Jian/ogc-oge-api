package whu.edu.cn.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import whu.edu.cn.entity.process.Link;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OGC API-Records
 * Implementation
 */

@Api("OGC API-Records")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/Records_Api")
public class RecordsController {
    @Autowired
    private RestHighLevelClient restClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @ApiOperation("Landing Page")
    @GetMapping("/")
    public JSONObject getLandingPage() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:staticJson/LandingPage-Records.json");
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        String s = new String(bytes);
        JSONObject result = JSONObject.parseObject(s);
        return result;
    }

    // TODO:一个产品对应多个景以及多个波段,每个景的空间范围不一样, 在MinIO中景对应着产品找到的波段,因此可以先找波段再找景
    @ApiOperation(value = "Conformance class", notes = "Conformance class")
    @GetMapping(value = "/conformance")
    public JSONObject getConformanceClasses() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:staticJson/Comformance-Records.json");
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        String s = new String(bytes);
        JSONObject result = JSONObject.parseObject(s);
        return result;
    }

    @ApiOperation(value = "/collections", notes = "Collections Page")
    @GetMapping(value = "/collections")
    public JSONObject getCollections() throws IOException {
        SearchRequest request = new SearchRequest("collections");
        request.source().query(QueryBuilders.matchAllQuery());
        JSONObject result = new JSONObject();
        try {
            SearchResponse response = restClient.search(request, RequestOptions.DEFAULT);
            JSONArray jsonArray = new JSONArray();
            System.out.println("共搜索到" + response.getHits().getTotalHits().value + "条结果");
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                JSONObject collection = JSONObject.parseObject(sourceAsString);
                jsonArray.add(collection);
            }
            result.put("collections", jsonArray);

            Link link = new Link("http://oenge.org.cn/Records_Api/collections", "self", "application/json",
                    "All the records collections");
            JSONArray links = new JSONArray();
            links.add(link);
            result.put("Links", links);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "/collections/{collectionId}", notes = "Get a collection by id")
    @GetMapping(value = "collections/{collectionId}")
    public JSONObject getCollectionById(@PathVariable String collectionId) {
        GetRequest request = new GetRequest("collections", collectionId);
        JSONObject result = new JSONObject();
        try {
            GetResponse response = restClient.get(request, RequestOptions.DEFAULT);
            String sourceAsString = response.getSourceAsString();
            JSONObject jsonObject = JSONObject.parseObject(sourceAsString);
            result = jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "/collections/{collectionId}/sortables", notes = "Get a collection sortable")
    @GetMapping(value = "collections/{collectionId}/sortables",produces = {"application/schema+json"})
    public JSONObject getCollectionSortables(@PathVariable String collectionId, HttpServletResponse response) {
        JSONObject result=new JSONObject();
        result.put("type","array");
        return result;
    }

    @ApiOperation(value = "collections/{collectionId}/items", notes = "Records search items page")
    @GetMapping(value = "collections/{collectionId}/items")
    public JSONObject getRecords(@PathVariable(value = "collectionId", required = true) String collectionId,
                                 @RequestParam(value = "q", required = false) String q,
                                 @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(value = "bbox", required = false) double[] bbox,
                                 @RequestParam(value = "datetime", required = false) String datetime,
                                 @RequestParam(value = "ids", required = false) String[] ids,
                                 @RequestParam(value = "externalIds", required = false) String[] externalIds,
                                 @RequestParam(value = "type", required = false) String[] type,
                                 @RequestParam(value = "keywords", required = false) String[] keywords,
                                 @RequestParam(value = "sortby", required = false, defaultValue = "["+"]") String[] sortby
    ) {
        //all表示是否搜索所有目录
        String []collectionIds;         //这种写法在Records规范中不规范
        if(collectionId.equals("all")) {
            collectionIds = new String[]{"models", "projects", "application-models","data-product"};
        }
        else collectionIds=new String[]{collectionId};

        if (page <= 0) page = 1;
        if (limit <= 0) limit = 1;
        //多个条件使用bool查询
        BoolQueryBuilder boolquery = QueryBuilders.boolQuery();
        SearchRequest request = new SearchRequest(collectionIds);

        if (q == null || q.trim().isEmpty()) {
            boolquery.must(QueryBuilders.matchAllQuery());
        } else boolquery.must(QueryBuilders.matchQuery("all", q));

        if (!(ids == null || ids.length == 0))
            boolquery.filter(QueryBuilders.idsQuery(ids));

        if (!(externalIds == null || externalIds.length == 0))
            boolquery.filter(QueryBuilders.termsQuery("properties.externalId.keyword", externalIds));

        if (!(keywords == null || keywords.length == 0))
            boolquery.filter(QueryBuilders.termsQuery("properties.keywords", keywords));

        if (!(type == null || type.length == 0))
            boolquery.filter(QueryBuilders.termsQuery("properties.type.keyword", type));

        //datetime格式为2024/03/02T11:00:00/..2024/03/02T11:00:00
        if (!(datetime == null || datetime.trim().isEmpty())) {
            if (datetime.contains("../..")) {
                String[] interval = datetime.split("../..");
                boolquery.should(QueryBuilders.rangeQuery("time.date").gt(interval[0]).lt(interval[1]));
                boolquery.should(QueryBuilders.rangeQuery("time.interval").gt(interval[0]).lt(interval[1]));
            } else if (datetime.contains("/..")) {
                String[] interval = datetime.split("/..");
                boolquery.should(QueryBuilders.rangeQuery("time.date").gte(interval[0]).lt(interval[1]));
                boolquery.should(QueryBuilders.rangeQuery("time.interval").gte(interval[0]).lt(interval[1]));
            } else if (datetime.contains("../")) {
                String[] interval = datetime.split("../");
                boolquery.should(QueryBuilders.rangeQuery("time.date").gt(interval[0]).lte(interval[1]));
                boolquery.should(QueryBuilders.rangeQuery("time.interval").gt(interval[0]).lte(interval[1]));
            } else if (datetime.contains("/")) {
                String[] interval = datetime.split("/");
                boolquery.should(QueryBuilders.rangeQuery("time.date").gte(interval[0]).lte(interval[1]));
                boolquery.should(QueryBuilders.rangeQuery("time.interval").gte(interval[0]).lte(interval[1]));
            } else {
                //参数为一个instant, 搜索当天的结果
                LocalDateTime DateTime = LocalDateTime.parse(datetime);
                LocalDate date = DateTime.toLocalDate();
                boolquery.should(QueryBuilders.rangeQuery("time.date").gte(date).lte(date.plusDays(1)));
                boolquery.should(QueryBuilders.rangeQuery("time.interval").gte(date).lte(date.plusDays(1)));
            }
        }
        //bbox搜索
        if (!(bbox == null || bbox.length == 0)) {
            GeoPoint bottomLeft = new GeoPoint(bbox[0], bbox[1]);   //左下纬/经度为数组的前两个值
            GeoPoint topRight = new GeoPoint(bbox[2], bbox[3]);
            boolquery.filter(QueryBuilders.geoBoundingBoxQuery("geometry").setCornersOGC(bottomLeft, topRight));
        }

        request.source().query(boolquery);
        request.source().from((page - 1) * limit).size(limit);
//        request.source().sort(params.getSortBy(), SortOrder.ASC);
        //请求并获得结果
        SearchResponse response;
        JSONObject result = new JSONObject();
        try {
            response = restClient.search(request, RequestOptions.DEFAULT);
            long total = response.getHits().getTotalHits().value;
            System.out.println("共搜索到" + total + "条记录");
            result.put("numberMatched", total);
            if (page * limit > total)
                result.put("numberReturned", total % limit);
            else if (total > limit)
                result.put("numberReturned", limit);

            JSONArray records = new JSONArray();
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                JSONObject record = JSONObject.parseObject(hit.getSourceAsString());
                records.add(record);
            }
            result.put("records", records);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "collections/{collectionId}/items/{recordId}", notes = "Get a record by id")
    @GetMapping(value = "collections/{collectionId}/items/{recordId}")
    public JSONObject getRecordById(@PathVariable String collectionId, @PathVariable String recordId) throws IOException {
        GetRequest request = new GetRequest(collectionId, recordId);
        GetResponse response = restClient.get(request, RequestOptions.DEFAULT);
        //从响应获取结果内容
        String json = response.getSourceAsString();
        JSONObject result = JSONObject.parseObject(json);
        return result;
    }

    @ApiOperation(value = "collections/{collectionId}/items", notes = "Add a record by id")
    @PostMapping(value = "collections/{collectionId}/items")
    public JSONObject addRecord(@PathVariable(value = "colletionId") String indexId,
                                @PathVariable(value = "recordId") String docId,
                                @RequestBody(required = true) JSONObject body) {
        return null;
    }

    /**
     * 更新文档（记录）
     *
     * @return
     */
    @ApiOperation(value = "collections/{collectionId}/items/{recordId}", notes = "Update a record by id")
    @PutMapping(value = "collections/{collectionId}/items/{recordId}")
    public JSONObject updateRecordById(@PathVariable(value = "collectionId") String indexId,
                                       @PathVariable(value = "recordId") String docId,
                                       @RequestBody(required = true) JSONObject body) {
        
        UpdateRequest request=new UpdateRequest(indexId,docId);
        request.doc(body);
        try{
            UpdateResponse updateResponse = restClient.update(request, RequestOptions.DEFAULT);
            RestStatus status = updateResponse.status();
            String statusOK = status.toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status",statusOK);
            return jsonObject;
        }catch (IOException ioException){
            ioException.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文档（记录）
     *
     * @param docId
     * @return
     */
    @ApiOperation(value = "collections/{collectionId}/items/{recordId}", notes = "Delete a record by id")
    @DeleteMapping(value = "collections/{collectionId}/items/{recordId}")
    public JSONObject deleteRecordById(
            @PathVariable(value = "collectionId") String indexId,
            @PathVariable(value = "recordId") String docId) {
        DeleteRequest request = new DeleteRequest(indexId, docId);
        try {
            DeleteResponse response = restClient.delete(request, RequestOptions.DEFAULT);
            RestStatus status = response.status();
            JSONObject result = new JSONObject();
            if (status.toString().toUpperCase() == "OK") {
                result.put("msg", "成功");
            } else {
                result.put("msg", "失败");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
