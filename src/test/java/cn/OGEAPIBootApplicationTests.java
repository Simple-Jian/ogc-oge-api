//package cn;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import io.swagger.util.Json;
//import org.gdal.gdal.gdal;
//import org.gdal.ogr.DataSource;
//import org.gdal.ogr.Driver;
//import org.gdal.ogr.ogr;
//import org.geotools.data.shapefile.ShapefileDataStore;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.data.simple.SimpleFeatureIterator;
//import org.geotools.data.simple.SimpleFeatureSource;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.junit.jupiter.api.Test;
//import org.opengis.feature.simple.SimpleFeature;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import whu.edu.cn.OGEAPIBootApplication;
//
//import java.io.*;
//import java.nio.charset.Charset;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//
//@SpringBootTest(classes = OGEAPIBootApplication.class)
//class OGEAPIBootApplicationTests {
//    /**
//     * ProcessJSON转换为DagJSON
//     */
////    @Test
//    public void jsonToDag() {
//        try {
//            // 指定 JSON 文件的路径
//            String filePath = "src/main/resources/example.json";
//            String targetPath = "src/main/resources/view.json";
//            // 创建一个 BufferedReader 对象，用于逐行读取文件内容
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetPath));
//            // 创建一个 StringBuilder 对象，用于存储读取的内容
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            // 逐行读取文件内容并追加到StringBuilder中
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            // 关闭 BufferedReader
//            bufferedReader.close();
//            // 将读取的内容转换为字符串
//            String jsonString = stringBuilder.toString();
//            JSONObject jsonObject = JSON.parseObject(jsonString);
//
//            JSONObject dag = traverseJson(jsonObject);
//
//            bufferedWriter.write(dag.toString());
//            bufferedWriter.close();
//            System.out.println(dag);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private static JSONObject traverseJson(JSONObject processJson/*, JSONObject dag*/) {
//        JSONObject dag=new JSONObject();
//        for (String key : processJson.keySet()) {
//            Object value = processJson.get(key);
//            if (key == "process") {
//                JSONObject jo = new JSONObject();
//                String str = value.toString();
//                jo.put("functionName", str.substring(str.lastIndexOf("/") + 1));
//                dag.put("functionInvocationValue", jo);
//            } else if (key == "inputs") {
//                JSONObject processRecursionResult=traverseJson((JSONObject) value);
//                JSONObject temp = dag.getJSONObject("functionInvocationValue");
//                temp.put("arguments", processRecursionResult);
//                dag.put("functionInvocationValue", temp);
//            } else {
//                if (value instanceof JSONObject) {
//                    JSONObject traverseJson = traverseJson((JSONObject) value);
//                    dag.put(key,traverseJson);
////               } else if (value instanceof JSONArray) {
////                  traverseJsonArray((JSONArray) value);
//                } else {
//                    dag.put(key, value);
//                }
//            }
//        }
//        return dag;
//    }
//
////    private static void traverseJsonArray(JSONArray jsonArray) {
////        for (Object element : jsonArray) {
////            if (element instanceof JSONObject) {
////                traverseJson((JSONObject) element);
////            } else if (element instanceof JSONArray) {
////                traverseJsonArray((JSONArray) element);
////            } else {
////                System.out.println("Value: " + element);
////            }
////        }
////    }
//
//
//
//    /**
//     * spring的Resource接口
//     */
////    @Test
////    void contextLoads() throws IOException {
////        Resource r=new UrlResource(new URL("https://web-tlias-jian.oss-cn-beijing.aliyuncs.com/polygon1.shp"));
////        InputStream inputStream = r.getInputStream();
////        FileOutputStream fos=new FileOutputStream("C:\\Users\\Acer\\Desktop\\p.shp");
////        int len;
////        byte bytes[]=new byte[1024];
////        while((len=inputStream.read(bytes))!=-1){
////            fos.write(bytes,0,len);
////        }
////    }
//
//    /**
//     * GDAL转换.shp文件为geojson格式
//     */
////    @Test
//    public void testGDAL() {
//        // 注册所有的驱动
//        ogr.RegisterAll();
//// 为了支持中文路径，请添加下面这句代码
//        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
//// 为了使属性表字段支持中文，请添加下面这句
//        gdal.SetConfigOption("SHAPE_ENCODING", "");
//        String strVectorFile = "C:\\Users\\Acer\\Desktop\\polygon1.shp";
////打开数据
//        DataSource ds = ogr.Open(strVectorFile, 0);
//        if (ds == null) {
//            System.out.println("打开文件失败！");
//            return;
//        }
//        System.out.println("打开文件成功！");
//        Driver dv = ogr.GetDriverByName("GeoJSON");
//        if (dv == null) {
//            System.out.println("打开驱动失败！");
//            return;
//        }
//        System.out.println("打开驱动成功！");
//        dv.CopyDataSource(ds, "C:\\Users\\Acer\\Desktop\\polygon1.geojson");
//        System.out.println("转换成功！");
//
//    }
//
//
//}