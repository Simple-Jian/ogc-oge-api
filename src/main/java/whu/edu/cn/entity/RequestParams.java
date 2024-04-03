package whu.edu.cn.entity;

import java.io.Serializable;

public class RequestParams implements Serializable {
    private InputParams inputParams;
    private OutputParams outputParams;

    public RequestParams(InputParams inputParams, OutputParams outputParams){
        this.inputParams = inputParams;
        this.outputParams = outputParams;
    }

    public InputParams getInputParams() {
        return inputParams;
    }

    public void setInputParams(InputParams inputParams) {
        this.inputParams = inputParams;
    }

    public OutputParams getOutputParams() {
        return outputParams;
    }

    public void setOutputParams(OutputParams outputParams) {
        this.outputParams = outputParams;
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "inputParams=" + inputParams +
                ", outputParams=" + outputParams +
                '}';
    }
}


