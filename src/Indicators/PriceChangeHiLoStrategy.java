package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;
import java.util.Collections;

public class PriceChangeHiLoStrategy {
    public static double calculatePriceChangeHiLo(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {

        // #1
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = dayData.priceL;

        // #2 - trailing1orFixed2 determines if (start, end) are relative seconds or hard day seconds.
        int start = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        int end = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));
        int trailing1orFixed2 = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(2)));


        // #3
        final ArrayList<Double> finalArray = pointerArray;
        UniversalIndicator.TriggerMethod m1 = (index)-> {

            double high = 0;
            double low = 0;
            double pciDif = 0;

            if (trailing1orFixed2 == 1) {
                // get price high for the range.
                high = Collections.max(finalArray.subList(index + start, index + end));
                // get price low for range.
                low = Collections.min(finalArray.subList(index + start, index + end));
                // calculate the PCI change.
                pciDif = (high - low) / low;
                return pciDif;
            } else if (trailing1orFixed2 == 2) {
                // get price high for the range.
                high = Collections.max(finalArray.subList(start,end));
                // get price low for range.
                low = Collections.min(finalArray.subList(start,end));
                // calculate the PCI change.
                pciDif = (high - low) / low;
                return pciDif;
            } else {
                System.out.println("! You did not format the json correctly! The 3rd parameter only accepts 1 or 2 !");
            }
            return -666;
        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        // #4

        int topBufferSize = (int) Math.max(-indicatorDetails.getParams().get(0), 0);
        int bottomBufferSize = (int) Math.max(indicatorDetails.getParams().get(1), 0);

        if (trailing1orFixed2 == 2) {
            topBufferSize = 0;
            bottomBufferSize = 0;
        }

        // # 5
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
