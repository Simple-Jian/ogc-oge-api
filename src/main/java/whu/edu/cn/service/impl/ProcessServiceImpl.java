package whu.edu.cn.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.edu.cn.entity.process.*;
import whu.edu.cn.mapper.ProcessMapper;
import whu.edu.cn.service.ProcessService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private ProcessMapper processMapper;

    @Override
    public ProcessesDesc getProcessList(Integer limit) {
        List<ModelResource> modelResources = processMapper.getModelResources(limit);
        List<ProcessDesc> processDescList = new ArrayList<>();

        modelResources.forEach(modelResource -> {
            ProcessDesc p=new ProcessDesc();
            p.setId(modelResource.getName());
            p.setTitle(modelResource.getName());
            p.setVersion("1.0.0");
            p.setOutputTransmission( Arrays.asList("value", "reference"));
            p.setJobControlOptions(Arrays.asList("async-execute"));
            Link descInfo = new Link("http://oge.whu.edu.cn/ogcapi/processes_api/processes/"+ p.getId(),
                    "self", "application/json", modelResource.getDescription());
            p.setLinks(Arrays.asList(descInfo));
            processDescList.add(p);
        });

        ProcessesDesc processesDesc=new ProcessesDesc();
        processesDesc.setProcesses(processDescList);
        processesDesc.setLinks(Arrays.asList(new Link("http://oge.whu.edu.cn/ogcapi/processes_api/processes", "self",
                "application/json", "the list of process description")));

        return processesDesc;
    }

    @Override
    public JSONObject getProcessByName(String name) {
        ModelResource modelResource = processMapper.getProcessByName(name);
        ProcessDesc p=new ProcessDesc();
        p.setId(modelResource.getName());
        p.setTitle(modelResource.getName());
        p.setVersion("1.0.0");
        p.setOutputTransmission( Arrays.asList("value", "reference"));
        p.setJobControlOptions(Arrays.asList("async-execute"));
        //Set related links
        Link relatedLink1 = new Link("http://oge.whu.edu.cn/ogcapi/processes_api/processes/"+ p.getId(),
                "self", "application/json", modelResource.getDescription());
        Link relatedLink2=new Link( "http://oge.whu.edu.cn/ogcpai/processes_api/processes/"+p.getId()+"/execution",
                "http://www.opengis.net/def/rel/ogc/1.0/execute","application/json","Execution for "+p.getId()+" process");
        Link relatedLink3=new Link( "http://oge.whu.edu.cn/ogcpai/processes_api/processes/"+p.getId()+"/execution/jobs",
                "http://www.opengis.net/def/rel/ogc/1.0/job-list","text/html","The jobs for "+p.getId()+" process");
        p.setLinks(Arrays.asList(relatedLink1,relatedLink2,relatedLink3));

        JSONObject jsonObject= (JSONObject) JSONObject.toJSON(p);
        //Get the params
        ModelResourceParam param = processMapper.getParamById(modelResource.getId());
        JSONObject paramObject = JSONObject.parseObject(param.getParamJson());
        JSONArray inputs=  paramObject.getJSONArray("inputs");
        JSONArray outputs=  paramObject.getJSONArray("outputs");

        jsonObject.put("input",inputs);
        jsonObject.put("output",outputs);

        return jsonObject;
    }
}
