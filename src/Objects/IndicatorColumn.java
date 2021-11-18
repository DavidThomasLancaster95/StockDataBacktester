package Objects;

import java.util.ArrayList;

public class IndicatorColumn {

    public String indicatorHeadName;
    public ArrayList<Double> valueColumn;

    public IndicatorColumn(String indicatorHeadName, ArrayList<Double> valueColumn) {
        this.indicatorHeadName = indicatorHeadName;
        this.valueColumn = valueColumn;
    }

    public String getIndicatorHeadName() {
        return indicatorHeadName;
    }

    public void setIndicatorHeadName(String indicatorHeadName) {
        this.indicatorHeadName = indicatorHeadName;
    }

    public ArrayList<Double> getValueColumn() {
        return valueColumn;
    }

    public void setValueColumn(ArrayList<Double> valueColumn) {
        this.valueColumn = valueColumn;
    }
}
