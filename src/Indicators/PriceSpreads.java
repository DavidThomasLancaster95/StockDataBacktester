package Indicators;

import Objects.DayData;

import java.util.Collections;

import static Objects.DayData.calculatePCIBetween2Points;

public class PriceSpreads {

    public static void calculatePriceSpread30MinutePCIPre5(DayData dayData){
        // for each line minus the last 35 minutes
        for (int i = 0; i < dayData.priceL.size() - 2099; i++) {
            // find the max and the min for the 30 minutes before that point
            double min = Collections.min(dayData.priceL.subList(i + 299, i + 2099));
            double max = Collections.max(dayData.priceL.subList(i + 299, i + 2099));
            dayData.priceSpread30MinutePCIPre5.add(calculatePCIBetween2Points(max, min));
        }
        for (int i = 0; i < 2099; i++) {
            dayData.priceSpread30MinutePCIPre5.add(0.0);
        }
    }

    public static void calculatePriceSpread30MinutePCIPre5AtTrigger(DayData dayData, int trigger) {
        double min = Collections.min(dayData.priceL.subList(trigger + 299, trigger + 2099));
        double max = Collections.max(dayData.priceL.subList(trigger + 299, trigger + 2099));
        dayData.triggerPriceSpread30MinutePCIPre5 = calculatePCIBetween2Points(max, min);
    }

}
