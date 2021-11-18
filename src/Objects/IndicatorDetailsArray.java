package Objects;

import java.util.ArrayList;

public class IndicatorDetailsArray {
    public ArrayList<IndicatorDetails> indicatorDetailsArrayList;

    public IndicatorDetailsArray() {
        indicatorDetailsArrayList = new ArrayList<>();
    }

    public void add(IndicatorDetails indicatorDetails) {
        indicatorDetailsArrayList.add(indicatorDetails);
    }

    public ArrayList<IndicatorDetails> getIndicatorParamsSetArrayList() {
        return indicatorDetailsArrayList;
    }

    public void setIndicatorParamsSetArrayList(ArrayList<IndicatorDetails> indicatorDetailsArrayList) {
        this.indicatorDetailsArrayList = indicatorDetailsArrayList;
    }

    public IndicatorDetails getIndicatorDetailsByTagName(String tagName) {
        for (IndicatorDetails id: this.indicatorDetailsArrayList) {
            if (id.tagname.equals(tagName)) return id;
        }
        System.out.println("trying to reference a dependency that does not exist. Your program will crash now");
        return null;
    }
}
