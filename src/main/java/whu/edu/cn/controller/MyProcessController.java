package whu.edu.cn.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import whu.edu.cn.entity.Result;
import whu.edu.cn.entity.process.Job;
import whu.edu.cn.entity.process.Link;
import whu.edu.cn.entity.process.ProcessDesc;
import whu.edu.cn.entity.process.ProcessesDesc;
import whu.edu.cn.entity.process.request.ProcessRequestBody;
import whu.edu.cn.mapper.AnalysisFuncMapper;
import whu.edu.cn.service.ProcessService1;
import whu.edu.cn.util.HttpUtil;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * OGC API - Processes
 *
 * Add process:
 *  (1) add process to List<ProcessDesc> getProcesses();
 *  (2) add process description to resource/processDescription/ as *.json
 *  (3) add request body to resource/processRequest as *.json
 *  (4) add custom results json to List<Object> getResults
 *
 * Note: only support input is literal input for now
 *
 * */
//@Api(tags = "OGE - OGC API - Processes ")
//@RestController
//@RequestMapping("/processes_api")
//@CrossOrigin(origins = "*", maxAge = 3600)
public class MyProcessController {
    final String localDataRoot = "/home/geocube/tomcat8/apache-tomcat-8.5.57/webapps/data/temp/";
    final String httpDataRoot = "http://125.220.153.26:8093/data/temp/";
    @Autowired
    private ProcessService1 processService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    AnalysisFuncMapper analysisFuncMapper;

    @Autowired
    private HttpUtil httpUtil;

    /**
     * Landing page of geocube processes API.
     */
    @ApiOperation(value = "Landing page", notes = "Landing page")
    @GetMapping(value = "/")
    public Map<String, Object> getLandingPage() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "OGE processing server");
        map.put("description", "OGE server implementing the OGC API - Processes 1.0");

        List<Link> linkList = new ArrayList<>();
        Link self = new Link();
        self.setHref("http://oge.whu.edu.cn/ogcapi/processes_api/");
        self.setRel("self");
        self.setType("application/json");
        self.setTitle("landing page");
        linkList.add(self);

        Link serviceDesc = new Link();
        serviceDesc.setHref("http://oge.whu.edu.cn/ogcapi/processes_api/api");
        serviceDesc.setRel("service-desc");
        serviceDesc.setType("application/openapi+json;version=3.0");
        serviceDesc.setTitle("the API definition");
        linkList.add(serviceDesc);

        Link conformance = new Link();
        conformance.setHref("http://oge.whu.edu.cn/ogcapi/processes_api/conformance");
        conformance.setRel("conformance");
        conformance.setType("application/json");
        conformance.setTitle("OGC API - Processes conformance classes implemented by this server");
        linkList.add(conformance);

        Link processes = new Link();
        processes.setHref("http://oge.whu.edu.cn/ogcapi/processes_api/processes");
        processes.setRel("processes");
        processes.setType("application/json");
        processes.setTitle("Metadata about the processes");
        linkList.add(processes);

        map.put("links", linkList);
        return map;
    }

    /**
     * The OpenAPI definition as JSON.
     */
    @ApiOperation(value = "Open api", notes = "The OpenAPI definition as JSON")
    @GetMapping(value = "/api")
    public Map<String, Object> getOpenAPI() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:static/openAPI.json");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(resource.getInputStream(), Map.class);
    }

    /**
     * Information about standards that this API conforms to.
     */
    @ApiOperation(value = "Conformance class", notes = "Conformance class")
    @GetMapping(value = "/conformance")
    public Map<String, Object> getConformanceClasses() {
        Map<String, Object> map = new HashMap<>();
        List<String> comformList = new ArrayList<>();
        comformList.add("http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/core");
        comformList.add("http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/json");
        comformList.add("http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/oas30");
        comformList.add("http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/ogc-process-description");
        comformList.add("http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/callback");
        comformList.add("http://www.opengis.net/spec/ogcapi-processes-1/1.0/conf/dismiss");
        map.put("conformsTo", comformList);
        return map;
    }

    /**
     * Lists the processes this API offers.
     */
    @ApiOperation(value = "Processes provided", notes = "Processes list")
    @GetMapping(value = "/processes")
    public ProcessesDesc getProcesses() {
        List<ProcessDesc> processDescList = new ArrayList<>();
        Link descInfo1 = new Link("http://oge.whu.edu.cn/ogcapi/processes_api/processes/spei",
                "self", "application/json", "process description");

        ProcessDesc spei = new ProcessDesc("spei", "spei", "1.0.0",  Arrays.asList("async-execute"),
                Arrays.asList("value", "reference"),  Arrays.asList(descInfo1));
        //定义process-buffer的描述信息
        Link descInfo2 = new Link("https://192.168.137.1:8099//processes_api/processes/buffer",
                "self", "application/json",
                "Enter a shapeFile and buffer distance, return a buffered folder in .zip format");
        ProcessDesc buffer = new ProcessDesc("buffer", "buffer", "1.0.0",  Arrays.asList("sync-execute"),
                Arrays.asList("value", "reference"),  Arrays.asList(descInfo2));

        processDescList.add(spei);
        processDescList.add(buffer);

        ProcessesDesc processesDesc = new ProcessesDesc();
        processesDesc.setProcesses(processDescList);
        processesDesc.setLinks(Arrays.asList(new Link("http://oge.whu.edu.cn/ogcapi/processes_api/processes", "self",
                "application/json", "the list of process description")));

        return processesDesc;
    }

    /**
     * Retrieve a process description
     * */

    /**
     * Retrieve a process description.
     *
     * @param name Process name
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "Process description", notes = "Process description")
    @GetMapping(value = "/processes/{processId}")
    public JSONObject getProcessDescription(@PathVariable("processId") String name) throws IOException {
        Resource resource = new ClassPathResource("processDescription/" + name + "_description.json");
        InputStream is = resource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(is);
        String s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        JSONObject result = JSONObject.parseObject(s);
        return result;
    }

    /**
     * Execute a process.
     *
     * @param processName Process name
     * @param processRequest 请求输入的参数
     * @param session http session
     * @return job 对象
     * @throws IOException
     * @throws InterruptedException
     */
    @ApiOperation(value = "Execute a process", notes = "Return the job id")
    @PostMapping(value = "/processes/{processId}/execution")
    public Object execute(@PathVariable("processId") String processName,
                             @RequestBody ProcessRequestBody processRequest,
                             @ApiIgnore HttpSession session)
            throws Exception {
        System.out.println(processName + " process is runing...");
        String sessionId = session.getId();
        String jobId = UUID.randomUUID().toString();
        // if process Id == spei
        if(processName.equals("spei")){
            JSONObject paramsObj = new JSONObject();
            JSONObject inputsObj = new JSONObject();
            inputsObj.put("cubeId", "27");
            inputsObj.put("rasterProductName", "LC08_L1TP_ARD_EO");
            inputsObj.put("extent", processRequest.getInputs().getString("extent"));
            inputsObj.put("startTime", processRequest.getInputs().getString("startTime"));
            inputsObj.put("endTime", processRequest.getInputs().getString("endTime"));
            inputsObj.put("timeScale", processRequest.getInputs().getString("timeScale"));
            paramsObj.put("InputParams", inputsObj);

            ResponseEntity<String> responseEntity = httpUtil.jsonHttp("http://oge.whu.edu.cn/api/oge-cube-boot/geocube/processes/spei/jobs/",
                    paramsObj.toString(), HttpMethod.POST, null);
            List<String> cookieList = responseEntity.getHeaders().get("Set-Cookie");
            String cookies = cookieList.stream().collect(Collectors.joining(";"));
            jobId = responseEntity.getBody();
            session.setAttribute(processName + "_" + jobId + "_cookies", cookies);
            Job job = new Job();
            job.setJobID(jobId);
            job.setStatus("accepted");
            job.setMessage("Process started");
            job.setProgress(0);
            LocalDateTime now = LocalDateTime.now();
            String formatted = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            job.setCreated(formatted);
            job.setLinks(Arrays.asList(new Link("http://oge.whu.edu.cn/ogcapi/processes_api/processes/" + processName
                    + "/jobs/" + jobId,
                    "self", "application/json", "spei")));
            session.setAttribute(processName + "_" + jobId + "_state", job);
            return Result.ok(job);
        }
        else if(processName.equals("buffer")){
            //Parse url and buffer distance from the Json "inputs"
            String url=processRequest.getInputs().getString("url");
            Double distance=Double.parseDouble(processRequest.getInputs().getString("distance"));
            //Call the method defined in the related service layer
            String resultUrl = processService.myBuffer(url, distance);
            Link link=new Link();
            link.setHref(resultUrl);
            link.setTitle("url");
            link.setType("String");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("url",link);
            return jsonObject;
        }
        //这个接口用于将.shp转换为geojson,并返回其url
        else if(processName.equals("toGeojson")){
            //Parse url from the Json "inputs"
            String url=processRequest.getInputs().getString("url");
            //Call the method defined in the related service layer
            String resultUrl = processService.ShpToGeojson(url);
            Link link=new Link();
            link.setHref(resultUrl);
            link.setTitle("url");
            link.setType("String");
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("url",link);
            return jsonObject;
        }
        else return Result.error("No such process!");
    }

    /**
     * Retrieve the status of a job.
     *
     * @param processName processName
     * @param jobId the Id of the job
     * @param session the http session
     * @return Job 对象
     */
    @ApiOperation(value = "Get the status of a job", notes = "Return the job status")
    @GetMapping("/processes/{processId}/jobs/{jobId}")
    public Job getStatus(@PathVariable("processId") String processName,
                            @PathVariable("jobId") String jobId,
                         @ApiIgnore HttpSession session){
        Job job = (Job) session.getAttribute(processName + "_" + jobId + "_state");
        String cookies = (String) session.getAttribute(processName + "_" + jobId + "_cookies");
        if(processName.equals("spei")){
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            String jobStatus = httpUtil.formHttp("http://oge.whu.edu.cn/api/oge-cube-boot/geocube/processes/spei/jobs/"+jobId,
                    params, HttpMethod.GET, cookies).getBody();
            if(jobStatus != null){
                String [] jobStatusList = jobStatus.split(",");
                job.setStatus(jobStatusList[0].toLowerCase(Locale.ROOT));
                job.setProgress(Integer.valueOf(jobStatusList[1].replace("%", "")));
            }else{
                job = null;
            }
        }
        return job;
    }

    /**
     * Retrieve the result(s) of a job/
     *
     * @param processName the name of the process
     * @param jobId the Id of the job
     * @param session the session of this process
     * @return Map<String, Object> return the map struct
     * @throws IOException IO Error
     */
    @ApiOperation(value = "Get the result of a job", notes = "Return the job result")
    @GetMapping("/processes/{processId}/jobs/{jobId}/results")
    public JSONArray getResults(@PathVariable("processId") String processName,
                                @PathVariable("jobId") String jobId,
                                @ApiIgnore HttpSession session) throws IOException {
        JSONArray resultArray = new JSONArray();
        String cookies = (String) session.getAttribute(processName + "_" + jobId + "_cookies");
        if(processName.equals("spei")){
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            String resultsTxt = httpUtil.formHttp(
                    "http://oge.whu.edu.cn/api/oge-cube-boot/geocube/processes/spei/jobs/"+ jobId +"/results",
                    params, HttpMethod.GET, cookies).getBody();
            JSONObject resultObj = JSONObject.parseObject(resultsTxt);
            for (String key : resultObj.keySet()) {
                JSONObject oneResultObj = resultObj.getJSONObject(key);
                JSONObject valueObj = new JSONObject();
                valueObj.put("url", oneResultObj.getString("path").replace("http://125.220.153.26:8093",
                        "http://oge.whu.edu.cn/api/oge-python"));
                valueObj.put("time", oneResultObj.getString("time"));
                JSONObject eleValueObj = new JSONObject();
                eleValueObj.put("value", valueObj);
                resultArray.add(eleValueObj);
            }
        }
        return resultArray;
    }

    /**
     * Cancel a job execution, remove a finished job.
     *
     * @param name
     * @param jobId
     * @param session
     * @return
     */
    @ApiOperation(value = "Cancel a job execution", notes = "Cancel a job execution")
    @DeleteMapping("/processes/{processId}/jobs/{jobId}")
    public Map<String, Object> dismiss(@PathVariable("processId") String name,
                          @PathVariable("jobId") String jobId, @ApiIgnore HttpSession session){
        Map<String, Object> map = new HashMap<>();
        map.put("jobID", jobId);
        map.put("progress", 0);

        String sparkAppId = (String) session.getAttribute(name + "_" + jobId + "_sparkAppId");
        System.out.println(sparkAppId);
        Runtime run = Runtime.getRuntime();
        String cmd = "curl -X POST \"http://125.220.153.26:9090/app/kill/?id=" + sparkAppId + "&terminate=true\"";
        try{
            Process process = run.exec(cmd);
            int status = process.waitFor();
            if(status != 0){
                map.put("message", "Fail to dismiss the process");
                map.put("status", status);
            }
            else{
                map.put("message", "Process dismissed");
                map.put("status", "dismissed");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Link link = new Link();
        link.setHref("http://125.220.153.26:8091/geocube/processes_api/processes/" + name + "/jobs/" + jobId);
        link.setRel("self");
        link.setType("application/json");
        link.setTitle(name);

        map.put("links", link);
        return map;
    }


}
