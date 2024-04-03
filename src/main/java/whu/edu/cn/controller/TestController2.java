package whu.edu.cn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import whu.edu.cn.util.AliOSSUtil;
import whu.edu.cn.util.FolderToZipUtil;
import whu.edu.cn.util.ZipUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
//@RestController
//@Api(tags = "一个测试类")
//@RequestMapping("/processes_api")
//@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController2 {

    //阿里云工具类bean
    @Autowired
    private AliOSSUtil aliOSSUtil;
    /**
     * 输入一个.shp文件及缓冲区距离,生成缓冲区,坐标系为WGS84
     *
     * @param url
     * @param distance
     * @throws Exception
     */
    @ApiOperation("输入一个.shp文件及缓冲区距离,生成缓冲区,坐标系为WGS84")
    @PostMapping("/buffer/execution")
    public String mybuffer(
            String url,
            Double distance
    ) throws Exception {

        //将接收的的MultiFile文件先转换为本地文件
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

        //将结果打包成压缩包,并存放在项目目录下
        File tempZipDir = new File("/tempZipDir");   //临时存放压缩包的文件夹,当前项目目录
        tempZipDir.mkdirs();
        File tempZip = new File(tempZipDir, "tempDir.zip");   //临时存放的压缩包

        try {
            ZipUtil.myZip(tempDir, tempZipDir);
            //将压缩包上传到阿里云OSS
            String resultUrl = aliOSSUtil.upload(tempZip);
            //返回url
            return resultUrl;
        } catch (Exception e) {
            throw e;
        } finally {
            boolean deleteTag = true;         //这是临时文件是否清理成功的标志,不成功,抛出错误
            //清理临时文件
            deleteTag = tempShpFile.delete();              //清理输入的临时文件
            deleteTag = deleteTag ? tempZip.delete() : false;    //清理临时压缩文件
            deleteTag = deleteTag ? tempZipDir.delete() : false;        //清理临时生成的压缩文件夹
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
