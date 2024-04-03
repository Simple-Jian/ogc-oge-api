package whu.edu.cn.entity.process;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(description = "算子中心的实体对象")
public class ModelResource {
    @ApiModelProperty(value = "id")
    private int id;
    @ApiModelProperty(value = "算子名称")
    private String name;
    @ApiModelProperty(value = "算子别名")
    private String alias;
    @ApiModelProperty(value = "菜单项名称")
    private String label;
    @ApiModelProperty(value = "菜单项编号")
    private int labelId;
    @ApiModelProperty(value = "注册时间")
    private Timestamp registerTime;
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;
    @ApiModelProperty(value = "所属类别")
    private String subject; // 目录类型
    @ApiModelProperty(value = "模型参数ID")
    private int paramId; // 模型参数ID
    @ApiModelProperty(value = "模型参数")
    private ModelResourceParam param; // 模型参数
    @ApiModelProperty(value = "算子描述")
    private String description;
    @ApiModelProperty(value = "算子/模型来源")
    private String source;
    @ApiModelProperty(value = "案例代码")
    private String sampleCode;
}
