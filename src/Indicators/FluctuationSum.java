package Indicators;

import Objects.DayData;

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
}
