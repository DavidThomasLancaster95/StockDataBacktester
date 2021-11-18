package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;

public class SetTimeRangeStrategy {
    public static double calculateTimeRangeStrategy(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = null;

        if (indicatorDetails.primetype.equals("volume")) pointerArray = dayData.volumeL;
        if (indicatorDetails.primetype.equals("tick")) pointerArray = dayData.tickL;

        int start = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        int end = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));

        final ArrayList<Double> finalArray = pointerArray;

        Double startVal = (Double) finalArray.get(start);
        Double endVal = (Double) finalArray.get(end);

        double rangeValue = startVal - endVal;

        if (onlyTrigger) return rangeValue;

        for (int i = 0; i < dayData.priceL.size(); i++) {
            array.add(rangeValue);
        }


        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return rangeValue;
    }
}
