package Objects;

public class Constraint {
    public String headerName;
    public double min;
    public double max;

    public Constraint(String headerName, double min, double max) {
        this.headerName = headerName;
        this.min = min;
        this.max = max;
    }
}
