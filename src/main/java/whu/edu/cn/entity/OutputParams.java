package whu.edu.cn.entity;

import java.io.Serializable;

public class OutputParams implements Serializable {
    private String output;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "OutputParams{" +
                "output='" + output + '\'' +
                '}';
    }
}
