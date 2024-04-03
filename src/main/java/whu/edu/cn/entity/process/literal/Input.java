package whu.edu.cn.entity.process.literal;

import java.io.Serializable;

public class Input implements Serializable {
    private DataType dataType;
    private String value;

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
