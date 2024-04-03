package whu.edu.cn.entity.process.request;


import whu.edu.cn.entity.process.literal.RequestInput;

import java.io.Serializable;
import java.util.List;

//support only LiteralInput for now
public class RequestInputs implements Serializable {
    private List<RequestInput> requestInputs;

    public List<RequestInput> getRequestInputs() {
        return requestInputs;
    }

    public void setRequestInputs(List<RequestInput> requestInputs) {
        this.requestInputs = requestInputs;
    }
}
