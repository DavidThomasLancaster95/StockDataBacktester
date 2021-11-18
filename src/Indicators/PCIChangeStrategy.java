package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;

public class PCIChangeStrategy {
    public static double calculatePCIStrategy(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = dayData.priceL;


        int start = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        int end = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));

        final ArrayList<Double> finalArray = pointerArray;

        UniversalIndicator.TriggerMethod m1 = (index)-> {

            Double startVal = (Double) finalArray.get(index + start);
            Double endVal = (Double) finalArray.get(index + end);

            return (startVal - endVal) / endVal;
        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        int topBufferSize = (int) Math.max(-indicatorDetails.getParams().get(0), 0);
        int bottomBufferSize = (int) Math.max(indicatorDetails.getParams().get(1), 0);


        // adding top buffer
        for (int i = 0; i < topBufferSize; i++) {
            array.add(0.0);
        }

        // main loop
        for (int i = 0; i < pointerArray.size() - Math.abs(end) - 1; i++) {

//            Double startVal = (Double) pointerArray.get(i + start);
//            Double endVal = (Double) pointerArray.get(i + end);
//            array.add(startVal - endVal);

            array.add(m1.calculateTrigger(i));
        }

        // adding bottom buffer
        for (int i = 0; i < bottomBufferSize + 1; i++) {
            array.add(0.0);
        }

        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return 0;

    }
}
