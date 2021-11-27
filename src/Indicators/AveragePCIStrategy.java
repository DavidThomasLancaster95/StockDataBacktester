package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;

public class AveragePCIStrategy {
    public static double calculateAveragePCI(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        // #1
        ArrayList<Double> array = new ArrayList<>();

        // #2
        String dependantRangeTag = indicatorDetails.getDependencies().get(0);

        int start = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        int end = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));
        int total1Absolute2 = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(2)));
        int average1Sum2 = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(3)));

        // #3
        final ArrayList<Double> finalArray = dayData.getIndicatorColumnByName(dependantRangeTag).valueColumn;

        UniversalIndicator.TriggerMethod m1 = (index)-> {

            double pciSum = 0.0;

            if (total1Absolute2 == 1) {
                for (int i = start; i <= end; i++) {
                    pciSum += finalArray.get(index + i);
                }
            } else if (total1Absolute2 == 2) {
                for (int i = start; i <= end; i++) {
                    pciSum += Math.abs(finalArray.get(index + i));
                }
            }

            if (average1Sum2 == 1) {
                return pciSum / (end - start + 1);
            } else {
                return pciSum;
            }

        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        // #4
        int topBufferSize = (int) Math.max(-indicatorDetails.getParams().get(0), 0);
        int bottomBufferSize = (int) Math.max(indicatorDetails.getParams().get(1), 0);

        // #5
        // adding top buffer
        for (int i = 0; i < topBufferSize; i++) {
            array.add(0.0);
        }

        // main loop
        for (int i = 0; i < dayData.priceL.size() - Math.abs(end) - 1; i++) {

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
