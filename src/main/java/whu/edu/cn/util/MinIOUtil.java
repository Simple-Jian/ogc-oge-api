package whu.edu.cn.util;

import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import whu.edu.cn.bean.Image;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MinIOUtil {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accesskey}")
    private String accesskey;

    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     *
     * @param objectNameList 桶内对象名称列表
     * @param storePath 存储路径
     * @param overwrite true 重写 false 不重写会进行判断 已有文件不会再下载
     * @return the absolute paths of store
     */
    public List<String> downloadFiles(List<String> objectNameList, String storePath, boolean overwrite){
        List<String> pathList = new ArrayList<>();
        try{
            // Create a MinIO client object with endpoint, access key, and secret key.
            MinioClient minioClient = new MinioClient.Builder().endpoint(endpoint).credentials(accesskey, secretKey).build();
            // Set the bucket name and object name
            String bucketName = "oge";
            // Download the object
            // create the store path
            File storeDir = new File(storePath);
            if(!storeDir.exists()){
                storeDir.mkdirs();
            }
            for(String objectName: objectNameList){
                objectName = objectName + ".tif";
                String filePath = storePath + File.separator + objectName.split("/")[objectName.split("/").length - 1];
                if(!overwrite && new File(filePath).exists()){
                    log.info(filePath + "文件已经存在，不会再下载");
                    pathList.add(filePath);
                }else{
                    try {
                        minioClient.downloadObject(DownloadObjectArgs.builder()
                                .overwrite(true)
                                .bucket(bucketName)
                                .object(objectName)
                                .filename(filePath)
                                .build());
                        log.info(objectName + "downloaded successfully.");
                        pathList.add(filePath);
                    } catch (Exception e) {
                        log.info(objectName + " doesn't exist");
                    }
                    /*boolean exist = minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build()).etag() != null;
                    if (exist) {
                        minioClient.downloadObject(DownloadObjectArgs.builder()
                                .overwrite(true)
                                .bucket(bucketName)
                                .object(objectName)
                                .filename(filePath)
                                .build());
                        log.info(objectName + "downloaded successfully.");
                    }*/
                }

            }
            return pathList;
        }catch (Exception e){
            e.printStackTrace();
            log.error("Error in downloadFiles");
            return pathList;
        }
    }

    /**
     *
     * @param image 输入查询出来的Image实体
     * @return ResponseEntity<InputStreamResource> 返回给前端文件流
     */
    public void downloadFile(Image image, HttpServletResponse response) throws IOException {
        try {

            String fileName = /*image.getBandRank() == 0 ? image.getImageIdentification()
                    + ".tif":*/ image.getImageIdentification()+"_"+image.getBandNum() + ".tif";
            String filePath = image.getPath() + "/" + fileName;
            System.out.println(filePath);
            MinioClient minioClient = new MinioClient.Builder().endpoint(endpoint).credentials(accesskey, secretKey).build();
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket("oge").object(filePath).build());
            byte[] buf = new byte[1024];
            int length = 0;
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            OutputStream outputStream = response.getOutputStream();
            // 输出文件
            while ((length = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            // 关闭输出流
            outputStream.close();
//            minioClient.downloadObject(DownloadObjectArgs.builder()
//                    .overwrite(true)
//                    .bucket("oge")
//                    .object(filePath)
//                    .filename("E:\\LaoK\\data2\\miniotest.tif")
//                    .build());
//            InputStreamResource resource = new InputStreamResource(inputStream);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(inputStream.available())
//                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            String data = "文件下载失败";
            OutputStream ps = response.getOutputStream();
            ps.write(data.getBytes("UTF-8"));
        }
    }


}
