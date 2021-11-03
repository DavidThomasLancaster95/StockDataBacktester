package Indicators;

import Calculation.TimeCalculation;
import Objects.DayData;

public class PriceTimeLevels {

    public static void calculateFirstHourLow(DayData dayData) {
        //System.out.println(this.priceL.size());
        for (int i = TimeCalculation.lineOfTime930(); i > TimeCalculation.lineOfTime1030(); i--) {
            if (dayData.priceL.get(i) < dayData.firstHourLow) dayData.firstHourLow = dayData.priceL.get(i);
        }
    }
}
