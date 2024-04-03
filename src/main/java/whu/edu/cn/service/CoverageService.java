package whu.edu.cn.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import whu.edu.cn.entity.coverage.CollectionInfo;
import whu.edu.cn.entity.coverage.DomainSet;
import whu.edu.cn.entity.coverage.RangeType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@Service
public interface CoverageService {
    /**
     * 查询符合条件的collection元信息
     */
    JSONArray getCollectionMetaByParams(Timestamp startTime, Timestamp endTime, String WKT, int limit);

    /**
     * 根据CoverageId获取一个collections的元信息
     */
    CollectionInfo getCollectionsById(String name);

    /**
     * 根据CoverageId下载一个collections
     * @return
     */

    void downloadCollectionByName(String name, HttpServletResponse response) throws IOException;

    /**
     * 根据collectionId获得一个Coverage
     * @param name coverageId
     * @return DomainSet
     */
    DomainSet getDomainSetByName(String name);

    RangeType getRangeTypeByName(String name);
}
