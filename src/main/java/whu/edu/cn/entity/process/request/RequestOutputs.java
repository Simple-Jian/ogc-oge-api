package whu.edu.cn.entity.process.request;


import whu.edu.cn.entity.process.literal.RequestOutput;

import java.io.Serializable;
import java.util.List;

public class RequestOutputs implements Serializable {
    private List<RequestOutput> requestOutputs;

    public List<RequestOutput> getRequestOutputs() {
        return requestOutputs;
    }

    public void setRequestOutputs(List<RequestOutput> requestOutputs) {
        this.requestOutputs = requestOutputs;
    }
}
