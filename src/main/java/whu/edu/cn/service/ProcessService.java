package whu.edu.cn.service;

import com.alibaba.fastjson.JSONObject;
import whu.edu.cn.entity.process.ProcessesDesc;

public interface ProcessService {
    public ProcessesDesc getProcessList(Integer limit);

    JSONObject getProcessByName(String name);
}
