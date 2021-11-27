package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;
import java.util.Collections;

public class AboveOrBelowPriceRangeStrategy {
    public static double calculateAboveOrBelowPriceRange(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        // #1
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = dayData.priceL;

        // #2
        String dependantRangeTag = indicatorDetails.getDependencies().get(0);
        int high1Low2 = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));

        // #3
        final ArrayList<Double> finalArray = pointerArray;
        final ArrayList<Double> onDependantArray = dayData.getIndicatorColumnByName(dependantRangeTag).valueColumn;  //pointerArray;
        double valueInFinalArray = onDependantArray.get(0);

        UniversalIndicator.TriggerMethod m1 = (index)-> {
            if (high1Low2 == 1) {
                double val = finalArray.get(index);
                if (finalArray.get(index) > valueInFinalArray) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (high1Low2 == 2) {
                if (finalArray.get(index) < valueInFinalArray) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                System.out.println("you did something wrong");
                return -666;
            }
        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        // #5
        for (int i = 0; i < dayData.priceL.size(); i++) {
            array.add(m1.calculateTrigger(i));
        }

        // #6
        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return 0;
    }
}
