package Indicators;

import Objects.DayData;

import java.util.ArrayList;
import java.util.Collections;

public class PointHigh {

    public static void calculatePointHighSumArray(DayData dayData) {
        // if it hasn't already been calculated, we need to initiate the trade strategy.
        if (dayData.pointHighArray.size() == 0) calculatePointHighArray(dayData);

        for (int i = 0; i < dayData.pointHighArray.size() - 1; i++) {
            int sumUpToIndex = dayData.pointHighArray.subList(i, dayData.pointHighArray.size() - 1).stream().mapToInt(a -> a).sum();
            dayData.pointHighSumArray.add(sumUpToIndex);
        }


        dayData.pointHighSumArray.add(0);
    }

    public static void calculatePointHighSumArrayAtTrigger(DayData dayData, int trigger) {
        dayData.triggerPointHighSumArray = dayData.pointHighSumArray.get(trigger);
    }

    public static void calculatePointHighArray(DayData dayData) {
        //System.out.println("calculating point high array...");
        for (int i = 0; i < 150; i++) {
            dayData.pointHighArray.add(0/*null*/);
        }
        for (int i = 150; i < dayData.priceL.size() - 150; i++) {
            // get max value in array before
            ArrayList<Double> beforeArray = new ArrayList<>(dayData.priceL.subList((i-150), (i)));
            double beforeMax = Collections.max(beforeArray);
            // get max value in array after wards
            ArrayList<Double> afterArray = new ArrayList<>(dayData.priceL.subList((i+1), (i+150)));
            double afterMax = Collections.max(afterArray);
            // if it fits, add it.
            double currentPrice = dayData.priceL.get(i);
            if ((currentPrice > beforeMax) && (currentPrice > afterMax)) {
                dayData.pointHighArray.add(1);
                System.out.println("found a point");
            } else {
                dayData.pointHighArray.add(0);
            }
        }
        for (int i = 0; i < 150; i++) {
            dayData.pointHighArray.add(0/*null*/);
        }
        //System.out.println("finished calculating.");
    }
}
