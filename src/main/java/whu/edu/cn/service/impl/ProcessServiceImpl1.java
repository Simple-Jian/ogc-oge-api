package whu.edu.cn.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import whu.edu.cn.service.ProcessService1;
import whu.edu.cn.util.AliOSSUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProcessServiceImpl1 implements ProcessService1 {
    //AliOSS utils bean
    @Autowired
    private AliOSSUtil aliOSSUtil;

    @Override
    public String myBuffer(String url, Double distance) throws Exception {
        //将接收的文件先转换为本地文件
        File tempShpFile = new File("temp.shp");
        //copyToLocalDir是一个将url中的文件保存到本地的方法
        copyToLocalDir(url, tempShpFile);

        //在本地创建临时文件夹,存储处理结果
        File tempDir = new File("tempDir");   //当前项目根路径下
        if (!tempDir.exists()) tempDir.mkdirs();

        //目标路径
        String buffile = tempDir.getName() + "\\" + "shpBuffer.shp";

        try {
            //读取shp文件
            File file = tempShpFile;
            ShapefileDataStore shpDataStore = null;
            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();

            //创建shape文件对象
            File fileBuf = new File(buffile);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, fileBuf.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);

            SimpleFeatureType sft = featureSource.getSchema();
            List<AttributeDescriptor> attrs = sft.getAttributeDescriptors();

            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setCRS(DefaultGeographicCRS.WGS84);
            tb.setName("shapefile");
            for (int i = 0; i < attrs.size(); i++) {
                AttributeDescriptor attr = attrs.get(i);
                String fieldName = attr.getName().toString();
                if (fieldName == "the_geom") {
                    tb.add(fieldName, Polygon.class);
                } else {
                    tb.add(fieldName, String.class);
                }
            }
            ds.createSchema(tb.buildFeatureType());
            //设置编码
            ds.setCharset(charset);

            //设置Writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            while (itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                SimpleFeature featureBuf = writer.next();
                featureBuf.setAttributes(feature.getAttributes());

                Geometry geo = (Geometry) feature.getAttribute("the_geom");
                Geometry geoBuffer = geo.buffer(distance);
                featureBuf.setAttribute("the_geom", geoBuffer);
            }
            writer.write();
            writer.close();
            itertor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        //将结果打包成压缩包,并存放在项目目录下
//        File tempZipDir = new File("/tempZipDir");   //临时存放压缩包的文件夹,当前项目目录
//        tempZipDir.mkdirs();
//        File tempZip = new File(tempZipDir, "tempDir.zip");   //临时存放的压缩包

        try {
          /*  ZipUtil.myZip(tempDir, tempZipDir);
            //将压缩包上传到阿里云OSS
            String resultUrl = aliOSSUtil.upload(tempZip);
            //返回url*/
            File bufferedFile=new File(tempDir,"bufferedFile.geojson");
            //将文件转换为geojson格式
            toGeojson(new File(buffile),bufferedFile);
            String resultUrl=aliOSSUtil.upload(bufferedFile);
            return resultUrl;
        } catch (Exception e) {
            throw e;
        } finally {
            boolean deleteTag = true;         //这是临时文件是否清理成功的标志,不成功,抛出错误
            //清理临时文件
            deleteTag = tempShpFile.delete();              //清理输入的临时文件
//            deleteTag = deleteTag ? tempZip.delete() : false;    //清理临时压缩文件
//            deleteTag = deleteTag ? tempZipDir.delete() : false;        //清理临时生成的压缩文件夹
            File[] files = tempDir.listFiles();        //临时文件夹中的所有文件
            for (File file : files) {                  //清理生成的所有临时文件
                if (deleteTag) deleteTag = file.delete();
                else deleteTag = false;
            }
            deleteTag = deleteTag ? tempDir.delete() : false;                          //清理临时文件夹
            if (!deleteTag) {
                log.info("临时文件清理失败!");
                throw new Exception("临时文件清理失败");
            }
            log.info("临时文件清理成功!");
        }
    }

    /**
     * 给一个Shp文件的url,处理后返回一个geojson的url
     * @param url
     * @return
     * @throws Exception
     */
    @Override
    public String ShpToGeojson(String url) throws Exception {
        File tempDir=new File("tempDir");
        tempDir.mkdirs();
        //将接收的的MultiFile文件先转换为本地文件
        File tempShpFile = new File(tempDir,"temp.shp");
        //copyToLocalDir是一个将url中的文件保存到本地的方法
        copyToLocalDir(url, tempShpFile);
        try {
            //返回url
            File tempGeojsonFile=new File(tempDir,"temp.geojson");
            //将文件转换为geojson格式
            toGeojson(tempShpFile,tempGeojsonFile);
            //上传到阿里云
            String resultUrl=aliOSSUtil.upload(tempGeojsonFile);
            return resultUrl;
        } catch (Exception e) {
            throw e;
        } finally {
            boolean deleteTag = true;         //这是临时文件是否清理成功的标志,不成功,抛出错误
            //清理临时文件
            File[] files = tempDir.listFiles();        //临时文件夹中的所有文件
            for (File file : files) {                  //清理生成的所有临时文件
                if (deleteTag) deleteTag = file.delete();
                else deleteTag = false;
            }
            deleteTag = deleteTag ? tempDir.delete() : false;                          //清理临时文件夹
            if (!deleteTag) {
                log.info("临时文件清理失败!");
                throw new Exception("临时文件清理失败");
            }
            log.info("临时文件清理成功!");
        }
    }

    /**
     * 将.shp文件转换为geojson文件
     * @param shpPath
     * @param jsonPath
     */
    private void toGeojson(File shpPath,File jsonPath) {
        //新建json对象
        FeatureJSON fjson = new FeatureJSON();
        JSONObject geojsonObject = new JSONObject();
        geojsonObject.put("type", "FeatureCollection");
        try {
            //获取featurecollection
            File file = shpPath;
            ShapefileDataStore shpDataStore = null;
            shpDataStore = new ShapefileDataStore(file.toURL());//设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();
            JSONArray array = new JSONArray();
            //遍历feature转为json对象
            while (itertor.hasNext()) {
                //将json字符串使用字节流写入文件
                SimpleFeature feature = itertor.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                String temp = writer.toString();
                byte[] b = temp.getBytes("iso8859-1");
                temp = new String(b, "gbk");
                JSONObject json = JSON.parseObject(temp);
                array.add(json);
            }
            geojsonObject.put("features", array);
            itertor.close();
            //将json字符串使用字符流写入文件
            File outputfile = jsonPath;
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputfile));
            bufferedWriter.write(JSON.toJSONString(geojsonObject));
            bufferedWriter.flush();
            bufferedWriter.close();
            FileOutputStream fileOutputStream = new FileOutputStream(outputfile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
            outputStreamWriter.write(JSON.toJSONString(geojsonObject));
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将url指向的文件拷贝到本地
     *
     * @param
     * @param tempShpFile
     * @throws IOException
     */
    private void copyToLocalDir(String url, File tempShpFile) throws IOException {
        //从url获取输入流
        Resource r = new UrlResource(new URL(url));
        InputStream inputStream = r.getInputStream();

        //拷贝到当前项目的文件夹下
        FileOutputStream fos = new FileOutputStream(tempShpFile);
        int len;
        byte bytes[] = new byte[1024 * 1024 * 5];
        while ((len = inputStream.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        //
        fos.close();
        inputStream.close();
    }
}
