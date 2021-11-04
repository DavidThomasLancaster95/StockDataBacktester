package Indicators;

import Objects.DayData;

import java.util.ArrayList;

import static Objects.DayData.calculatePCIBetween2Points;

public class FluctuationSum {
    public static void calculateWin4To2FluctuationSum(DayData dayData) {
        // if it hasn't already been calculated, we need to initiate the trade strategy.
        if (dayData.tradeStrategy4To2.size() == 0) WinStrategies.calculate4To2WinStrategy(dayData);//dayData.calculate4To2WinStrategy();


        for (int i = 0; i < dayData.tradeStrategy4To2.size() - 1; i++) {
            String currentValue = dayData.tradeStrategy4To2.get(i);
            String nextValue = dayData.tradeStrategy4To2.get(i + 1);
            if (!currentValue.equals(nextValue)) {
                dayData.win4To2Fluctuation.add(1);
            } else {
                dayData.win4To2Fluctuation.add(0);
            }
        }

        dayData.win4To2Fluctuation.add(0);

        for (int i = 0; i < dayData.win4To2Fluctuation.size() - 1; i++) {
            int sumUpToIndex = dayData.win4To2Fluctuation.subList(i, dayData.win4To2Fluctuation.size() -1 ).stream().mapToInt(a -> a).sum();
            dayData.win4To2FluctuationSum.add(sumUpToIndex);
        }
    }

    public static void calculateWin4To2FluctuationSumAtTrigger(DayData dayData, int trigger) {
        dayData.triggerWin4To2FluctuationSum = dayData.win4To2FluctuationSum.get(trigger);
    }


    public static void calculatePrevious1Hour4To2WinFluctuationSumAtTrigger(DayData dayData, int trigger) {
        ArrayList<String> previousHourWinArray = new ArrayList<>();
        for (int i = trigger + 3600; i > trigger; i--) {
            double iPrice = dayData.priceL.get(i);
            for (int j = i; j >= trigger; j--) {
                double jPrice = dayData.priceL.get(j);
                if (calculatePCIBetween2Points(jPrice, iPrice) > 0.04) {
                    previousHourWinArray.add("1");
                    break;
                }
                if (calculatePCIBetween2Points(jPrice, iPrice) < -0.02) {
                    previousHourWinArray.add("0");
                    break;
                }
                if (j == trigger) {
                    previousHourWinArray.add("No Movement");
                    break;
                }
            }
        }

        int previousHourWinFluctuationCount = 0;
        for (int i = 0; i < 3600 - 1; i++) {
            String currentValue = previousHourWinArray.get(i);
            String nextValue = previousHourWinArray.get(i + 1);
            if (!currentValue.equals(nextValue)) {
                previousHourWinFluctuationCount++;
            }
        }
        dayData.triggerPrevious1Hour4To2WinFluctuationSum = previousHourWinFluctuationCount;
    }
}
