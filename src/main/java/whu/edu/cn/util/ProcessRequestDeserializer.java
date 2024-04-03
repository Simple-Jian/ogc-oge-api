package whu.edu.cn.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import whu.edu.cn.entity.process.literal.RequestInput;
import whu.edu.cn.entity.process.literal.RequestOutput;
import whu.edu.cn.entity.process.request.ProcessRequest;
import whu.edu.cn.entity.process.request.RequestInputs;
import whu.edu.cn.entity.process.request.RequestOutputs;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonComponent
public class ProcessRequestDeserializer  extends JsonDeserializer<ProcessRequest> {

    @Override
    public ProcessRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        //get request body object mapper
        ObjectMapper codec = (ObjectMapper) jsonParser.getCodec();

        //get root node
        TreeNode readValueAsTree = jsonParser.readValueAsTree();

        //get processInputNode
        TreeNode processInputNode = readValueAsTree.get("inputs");
        List<RequestInput> literalInputs = codec.convertValue(processInputNode, codec.getTypeFactory().constructCollectionType(List.class, RequestInput.class));
        RequestInputs requestInputs = new RequestInputs(); requestInputs.setRequestInputs(literalInputs);

        //get processOutputNode
        TreeNode processOutputNode = readValueAsTree.get("outputs");
        List<RequestOutput> literalOutputs = codec.convertValue(processOutputNode, codec.getTypeFactory().constructCollectionType(List.class, RequestOutput.class));
        RequestOutputs requestOutputs = new RequestOutputs(); requestOutputs.setRequestOutputs(literalOutputs);

        ProcessRequest processRequest = new ProcessRequest();
        processRequest.setInputs(requestInputs);
        processRequest.setOutputs(requestOutputs);

        return processRequest;
    }

    public static ProcessRequest deserializeTest(String path) throws IOException{
        Resource resource = new ClassPathResource(path);
        InputStream is = resource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        TreeNode readValueAsTree = objectMapper.readTree(is);

        TreeNode processInputNode = readValueAsTree.get("inputs");
        List<RequestInput> literalInputs = objectMapper.convertValue(processInputNode, objectMapper.getTypeFactory().constructCollectionType(List.class, RequestInput.class));
        RequestInputs requestInputs = new RequestInputs(); requestInputs.setRequestInputs(literalInputs);

        TreeNode processOutputNode = readValueAsTree.get("outputs");
        List<RequestOutput> literalOutputs = objectMapper.convertValue(processOutputNode, objectMapper.getTypeFactory().constructCollectionType(List.class, RequestOutput.class));
        RequestOutputs requestOutputs = new RequestOutputs(); requestOutputs.setRequestOutputs(literalOutputs);

        return null;
    }

    public static void main(String[] args) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(formatted);
//        deserializeTest("processRequest/ndwi_request.json");
    }

}
