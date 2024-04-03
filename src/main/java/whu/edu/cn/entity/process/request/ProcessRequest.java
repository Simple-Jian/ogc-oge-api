package whu.edu.cn.entity.process.request;

import java.io.Serializable;

public class ProcessRequest implements Serializable {
    private RequestInputs inputs;
    private RequestOutputs outputs;

    public RequestInputs getInputs() {
        return inputs;
    }

    public void setInputs(RequestInputs inputs) {
        this.inputs = inputs;
    }

    public RequestOutputs getOutputs() {
        return outputs;
    }

    public void setOutputs(RequestOutputs outputs) {
        this.outputs = outputs;
    }
}
