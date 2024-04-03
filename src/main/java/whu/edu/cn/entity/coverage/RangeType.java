package whu.edu.cn.entity.coverage;

import java.util.List;

public class RangeType {
    private String type;
    private List<FieldInfo> fieldInfoList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<FieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public void setFieldInfoList(List<FieldInfo> fieldInfoList) {
        this.fieldInfoList = fieldInfoList;
    }
}
