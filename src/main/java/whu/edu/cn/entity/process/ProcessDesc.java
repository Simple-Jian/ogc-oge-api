package whu.edu.cn.entity.process;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDesc implements Serializable {
    private String id; //name
    private String title;
    private String version;
    private List<String> jobControlOptions;
    private List<String> outputTransmission;
    List<Link> links;
}
