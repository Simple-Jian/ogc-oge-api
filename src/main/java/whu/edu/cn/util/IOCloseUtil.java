package whu.edu.cn.util;
 
import java.io.Closeable;
import java.io.IOException;
 
/**
 * @program: microfront-server
 * @description: IO流关闭工具Utils
 * @author: jql
 * @create: 2023/9/8
 */
public class IOCloseUtil {
 
    /**
     *   IO流关闭工具类
     */
    public static void close(Closeable... io) {
        for (Closeable temp : io) {
            try {
                if (null != temp)
                    temp.close();
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            }
        }
    }
 
    public static <T extends Closeable> void closeAll(T... io) {
        for (Closeable temp : io) {
            try {
                if (null != temp)
                    temp.close();
            } catch (IOException e) {
                System.out.println("" + e.getMessage());
            }
        }
 
    }
}