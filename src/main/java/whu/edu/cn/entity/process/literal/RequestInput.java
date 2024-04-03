package whu.edu.cn.entity.process.literal;

import java.io.Serializable;

public class RequestInput implements Serializable {
    private String id;
    private Input input;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }




}
