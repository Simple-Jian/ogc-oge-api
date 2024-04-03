package whu.edu.cn.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component   //将其交给IOC容器管理
public class AliOSSUtil {
    //使用@Value注解注入配置文件中配置的属性
    @Value("${oss.aliyun.endpoint}")
    private String endpoint;
    @Value("${oss.aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.aliyun.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.aliyun.bucketName}")
    private String bucketName;

    //使用@comfigurationProperties(prefix=)批量注入属性
        /*@Autowired
        private AliProperties aliProperties;


        */

    /**
     * 实现上传图片到OSS
     */
    public String upload(File file) throws IOException {
           /* String endpoint = aliProperties.getEndpoint();
            String accessKeyId = aliProperties.getAccessKeyId();
            String accessKeySecret = aliProperties.getAccessKeySecret();
            String bucketName = aliProperties.getBucketName();*/
        Resource r=new PathResource(file.getPath());
        // 获取上传的文件的输入流
        InputStream inputStream = r.getInputStream();

        // 避免文件覆盖
        String fileName = UUID.randomUUID().toString() + file.getName().substring(file.getName().lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}
