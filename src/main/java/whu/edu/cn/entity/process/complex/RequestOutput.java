package whu.edu.cn.entity.process.complex;

public class RequestOutput {
    private String id;
    private String transmissionMode;
    private Format format;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(String transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
