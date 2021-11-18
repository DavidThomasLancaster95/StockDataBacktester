package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;

public class MovingAverageStrategy {
    public static double calculateMovingAverageStrategy(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = dayData.priceL;

        int secondsBack = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        int interval = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));

        int end = secondsBack;

        final ArrayList<Double> finalArray = pointerArray;

        UniversalIndicator.TriggerMethod m1 = (index)-> {

            double priceSum = 0;
            int counter = 0;
            while (counter < secondsBack) {
                priceSum += finalArray.get(index + counter);
                counter += interval;
            }

            return priceSum/((int)(secondsBack / interval));
        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        int topBufferSize = 0;
        int bottomBufferSize = (int) Math.max(secondsBack, 0);


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
