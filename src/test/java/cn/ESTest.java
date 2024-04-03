package cn;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import whu.edu.cn.OGEAPIBootApplication;
import whu.edu.cn.entity.ModelDoc;
import whu.edu.cn.entity.ProjectDoc;
import whu.edu.cn.entity.TempProject;
import whu.edu.cn.entity.catalog.ApplicationModelDoc;
import whu.edu.cn.entity.catalog.TempApplicationModel;
import whu.edu.cn.entity.catalog.dataResource.MeasurementRecord;
import whu.edu.cn.entity.catalog.dataResource.ProductRecord;
import whu.edu.cn.entity.catalog.dataResource.ProductRecordDoc;
import whu.edu.cn.entity.process.ModelResource;
import whu.edu.cn.mapper.DataBaseMapper;
import whu.edu.cn.mapper.ProductRecordMapper;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = OGEAPIBootApplication.class)
public class ESTest {
    @Autowired
    private DataBaseMapper dataBaseMapper;

    private RestHighLevelClient restClient;

    @Autowired
    private ProductRecordMapper productRecordMapper;

    /**
     * 添加文档(数据)
     */
    @Test
    void addProductRecord() throws IOException {
        String type="data-product";
        //1.创建Request对象
        GetIndexRequest request=new GetIndexRequest(type);
        //2.发送请求
        boolean isExists = restClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(isExists?"索引库存在":"索引库不存在");

        if(isExists){
            List<ModelResource> allModels = dataBaseMapper.getAllModels();
            BulkRequest bulkRequest = new BulkRequest();
            //组装请求
            int count=0;
            List<ProductRecord> allProductRecords = productRecordMapper.getAllProductRecords();
            for (ProductRecord productRecord : allProductRecords) {
                System.out.println("当前是："+(++count)+"次");
                int id = productRecord.getId();
                List<MeasurementRecord> measurementRecords= productRecordMapper.getMeasurementRecordByProductId(id);
                productRecord.setMeasurements(measurementRecords);
                ProductRecordDoc productRecordDoc = new ProductRecordDoc(productRecord);
                bulkRequest.add(new IndexRequest("data-product").id(id+"").source(
                        JSON.toJSONString(productRecordDoc), XContentType.JSON)
                );
            }

            //发送请求
            BulkResponse bulkResponse = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            BulkItemResponse[] items = bulkResponse.getItems();
            for (BulkItemResponse item : items) {
                System.out.println(item.getFailure());
                System.out.println(item.getFailureMessage());
            }
        }
    }
    /**
     * 添加文档（模型）
     */
    @Test
    public void addIndex() throws IOException {
        String type="models";
        //1.创建Request对象
        GetIndexRequest request=new GetIndexRequest(type);
        //2.发送请求
        boolean isExists = restClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(isExists?"索引库存在":"索引库不存在");

        if(isExists){
        List<ModelResource> allModels = dataBaseMapper.getAllModels();
        BulkRequest bulkRequest = new BulkRequest();
        //组装请求
            int count=0;
            for (ModelResource model : allModels) {
                System.out.println("当前是："+(++count)+"次");
                ModelDoc modelDoc=new ModelDoc(model);
                String id=modelDoc.getId();
                bulkRequest.add(new IndexRequest("models").id(id).source(
                        JSON.toJSONString(modelDoc), XContentType.JSON)
                );
            }
        //发送请求
        BulkResponse bulkResponse = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.status());
        }
    }
    /**
     * 添加文档（工程）
     */
    @Test
    public void addIndex2() throws IOException {
        String type="projects";
        //1.创建Request对象
        GetIndexRequest request=new GetIndexRequest(type);
        //2.发送请求
        boolean isExists = restClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(isExists?"索引库存在":"索引库不存在");

        if(isExists){
            //要读取的文件名字
            String fileName = "C:\\Users\\Acer\\Desktop\\oge资源records.xlsx";
            BulkRequest bulkRequest = new BulkRequest();  //组装请求

            EasyExcel.read(fileName, TempProject.class, new AnalysisEventListener<TempProject>() {
                int num=0;
                //解析一行运行一次此方法。
                @Override
                public void invoke(TempProject project, AnalysisContext analysisContext) {
                    num++;
                    ProjectDoc projectDoc = new ProjectDoc(project);
                    System.out.println("当前是："+num+"次");
                    String id=projectDoc.getId();
                    bulkRequest.add(new IndexRequest(type).id(id).source(
                            JSON.toJSONString(projectDoc), XContentType.JSON));
                }
                //解析所有数据完成，运行此方法。
                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    System.out.println("读取完成"+"共 "+num+" 条数据");
                }
            }).sheet().doRead();
            //发送请求
            BulkResponse bulkResponse = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulkResponse.status());
        }
        }

    /**
     * 添加文档（应用模型）
     */
    @Test
    public void addIndex3() throws IOException {
        String type="application-models";
        //1.创建Request对象
        GetIndexRequest request=new GetIndexRequest(type);
        //2.发送请求
        boolean isExists = restClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(isExists?"索引库存在":"索引库不存在");

        if(isExists){
            //要读取的文件名字
            String fileName = "C:\\Users\\Acer\\Desktop\\应用模型资源.xlsx";
            BulkRequest bulkRequest = new BulkRequest();  //组装请求

            EasyExcel.read(fileName, TempApplicationModel.class, new AnalysisEventListener<TempApplicationModel>() {
                int num=0;
                //解析一行运行一次此方法。
                @Override
                public void invoke(TempApplicationModel applicationModel, AnalysisContext analysisContext) {
                    num++;
                    ApplicationModelDoc applicationModelDoc = new ApplicationModelDoc(applicationModel);
                    System.out.println("当前是："+num+"次");
                    String id=applicationModelDoc.getId();
                    bulkRequest.add(new IndexRequest(type).id(id).source(
                            JSON.toJSONString(applicationModelDoc), XContentType.JSON));
                }
                //解析所有数据完成，运行此方法。
                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    System.out.println("读取完成"+"共 "+num+" 条数据");
                }
            }).sheet().doRead();
            //发送请求
            BulkResponse bulkResponse = restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulkResponse.status());
        }
    }
    @Test
    void testQuery() throws IOException {
        BoolQueryBuilder booleanQuery = QueryBuilders.boolQuery();
        SearchRequest request=new SearchRequest("model");

        String[] keywords= {"model", "age"};
        booleanQuery.filter(QueryBuilders.termsQuery("properties.keywords",keywords));

        String ids[]={"Coverage.ruggednessIndexByQGIS","Algorithm.hargreaves"};
        booleanQuery.filter(QueryBuilders.termsQuery("id.keyword",ids));

        booleanQuery.filter(QueryBuilders.rangeQuery("properties.created")
                .gte("2024-03-02T11:00:00").lte("2024-03-02T11:00:00"));

        request.source().query(booleanQuery);

        SearchResponse searchResponse = restClient.search(request, RequestOptions.DEFAULT);
        System.out.println("一共检索到"+searchResponse.getHits().getTotalHits().value+"条记录");
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }


    @BeforeEach
        //这个注解表示在每个单元测试要执行时需要进行的操作，这里是初始化restHighLevelClient对象
    void setUp() {
        this.restClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.88.100:9200")
        ));
    }

    @AfterEach
        //表示每个单元测试之后要执行的操作，这里是销毁该对象
    void tearDown() throws IOException {
        this.restClient.close();
//    }

    }
}
