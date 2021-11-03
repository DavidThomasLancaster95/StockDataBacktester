package Indicators;

import Objects.DayData;

import java.util.ArrayList;

public class MovingAverages {
    public static void calculateMovingAverage5By5(DayData dayData) {
        for (int i = 0; i < dayData.priceL.size() - 299; i++) {
            // now we have to sub loop for each of these.
            ArrayList<Double> last5MinValues = new ArrayList<>();
            for (int j = i; j < (i + 299); j = j + 5) { // skip 5 seconds each time
                last5MinValues.add(dayData.priceL.get(j));
            }
            // now we add the average to the main array
            double average5Min = last5MinValues.stream().mapToDouble(a -> a).sum() / last5MinValues.size();
            dayData.movingAverage5By5.add(average5Min);
        }

        for (int i = 0; i < 299; i++) {
            dayData.movingAverage5By5.add(0.0);
        }
    }

    public static void calculateMovingAverage5By5AtTrigger(DayData dayData, int trigger) {
        dayData.triggerMovingAverage5By5 = dayData.movingAverage5By5.get(trigger);
    }
}
