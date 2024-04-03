package whu.edu.cn.entity.process;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "算子参数的实体对象")
public class ModelResourceParam {
    @ApiModelProperty(value = "算子类型")
    private String type; // 算子类型
    @ApiModelProperty(value = "算子参数（Json形式）")
    private String paramJson;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }
}
