package whu.edu.cn.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * 转换processesJSON为DagJSON
 */
@Component
public class JsonToDagUtil {
    public JSONObject convertJson(JSONObject processJson) {
        JSONObject dag = new JSONObject();
        for (String key : processJson.keySet()) {
            Object value = processJson.get(key);
            if (key == "process") {
                JSONObject jo = new JSONObject();
                String str = value.toString();
                jo.put("functionName", str.substring(str.lastIndexOf("/") + 1));
                dag.put("functionInvocationValue", jo);
            } else if (key == "inputs") {
                JSONObject processRecursionResult = convertJson((JSONObject) value);
                JSONObject temp = dag.getJSONObject("functionInvocationValue");
                temp.put("arguments", processRecursionResult);
                dag.put("functionInvocationValue", temp);
            } else {
                if (value instanceof JSONObject) {
                    JSONObject convertJson = convertJson((JSONObject) value);
                    dag.put(key, convertJson);
//               } else if (value instanceof JSONArray) {
//                  traverseJsonArray((JSONArray) value);
                } else {
                    dag.put(key, value);
                }
            }
        }
        return dag;
    }

    /**
     * 对于JsonArray的数据类型，可考虑加入
     */
//    private void traverseJsonArray(JSONArray jsonArray) {
//        for (Object element : jsonArray) {
//            if (element instanceof JSONObject) {
//                traverseJson((JSONObject) element);
//            } else if (element instanceof JSONArray) {
//                traverseJsonArray((JSONArray) element);
//            } else {
//                System.out.println("Value: " + element);
//            }
//        }
//    }
}
