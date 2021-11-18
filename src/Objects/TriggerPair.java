package Objects;

public class TriggerPair {
    String headerName;
    double triggerValue;

    public TriggerPair(String headerName, double triggerValue) {
        this.headerName = headerName;
        this.triggerValue = triggerValue;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public double getTriggerValue() {
        return triggerValue;
    }

    public void setTriggerValue(double triggerValue) {
        this.triggerValue = triggerValue;
    }
}
