package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;
import java.util.Collections;

public class PriceHiOrLoRangeStrategy {
    public static double calculatePriceHiOrLo(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {

        // #1
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = dayData.priceL;

        // #2
        int start = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        int end = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));
        int high1orLow2 = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(2)));

        // #3
        final ArrayList<Double> finalArray = pointerArray;

        double returnVal;

        if (high1orLow2 == 1) {
            returnVal = Collections.max(finalArray.subList(start,end));
        } else if (high1orLow2 == 2) {
            returnVal = Collections.min(finalArray.subList(start,end));
        } else {
            System.out.println("! You did not format the json correctly! The 3rd parameter only accepts 1 or 2 !");
            returnVal = -666;
        }

        if (onlyTrigger) return returnVal;

        // #5
        for (int i = 0; i < dayData.priceL.size(); i++) {
            array.add(returnVal);
        }

        // #6
        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return returnVal;

    }
}
