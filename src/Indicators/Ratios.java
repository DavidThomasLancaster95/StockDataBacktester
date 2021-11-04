package Indicators;

import Objects.DayData;

public class Ratios {
    public static void calculateVolumeToTickRatio10Second(DayData dayData) {
        double tickSum = 0;
        double volumeSum = 0;
        for (int i = 0; i < dayData.tickL.size() - 10; i++) {
            int currentTick = dayData.tickL.get(i);
            int tick10SecondsAgo = dayData.tickL.get(i + 9);
            tickSum = currentTick - tick10SecondsAgo;

            double currentVol = dayData.volumeL.get(i);
            double vol10SecondsAgo = dayData.volumeL.get(i + 9);
            volumeSum = currentVol - vol10SecondsAgo;

            dayData.volumeToTickRatio10Second.add(volumeSum/tickSum);
        }

        for (int i = 0; i <= 10; i++) {
            dayData.volumeToTickRatio10Second.add(0.0);
        }
    }

    public static void calculateVolumeToTickRatio10SecondAtTrigger(DayData dayData ,int trigger) {
        if (dayData.volumeToTickRatio10Second.size() == 0) calculateVolumeToTickRatio10Second(dayData);
        dayData.triggerVolumeToTickRatio10Second = dayData.volumeToTickRatio10Second.get(trigger);
    }

}
