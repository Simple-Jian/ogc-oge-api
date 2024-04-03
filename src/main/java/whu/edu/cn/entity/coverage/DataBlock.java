package whu.edu.cn.entity.coverage;

import java.util.List;

public class DataBlock {
    private String type;
    private List<Object> values;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
