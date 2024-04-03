package whu.edu.cn.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;
import whu.edu.cn.entity.InputParams;
import whu.edu.cn.entity.OutputParams;
import whu.edu.cn.entity.RequestParams;

import java.io.IOException;

@JsonComponent
public class RequestParamsDeserializer extends JsonDeserializer<RequestParams> {

    @Override
    public RequestParams deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        //get request body object mapper
        ObjectCodec codec = jsonParser.getCodec();

        //get root node
        TreeNode readValueAsTree = jsonParser.readValueAsTree();

        //get inputParamsNode
        TreeNode inputParamsNode = readValueAsTree.get("InputParams");
        InputParams inputParams = codec.treeToValue(inputParamsNode, InputParams.class);

        //get outputParamsNode
        TreeNode outputParamsNode = readValueAsTree.get("OutputParams");
        OutputParams outputParams = codec.treeToValue(outputParamsNode, OutputParams.class);

        return new RequestParams(inputParams, outputParams);
    }
}
