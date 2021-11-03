package Indicators;

import Objects.DayData;

import java.util.ArrayList;

public class Volume {
    public static void calculateVolume15Second(DayData dayData) {
        for (int i = 0; i < dayData.volumeL.size() - 16; i++) {
            Double currentVolume = dayData.volumeL.get(i);
            Double volume15SecondAgo = dayData.volumeL.get(i + 14);
            dayData.volume15Second.add((int) (currentVolume - volume15SecondAgo));
        }
        for (int i = 0; i <= 15; i++) {
            dayData.volume15Second.add(0);
        }
    }

    public static void calculateVolume15SecondAtTrigger(DayData dayData, int trigger) {
        dayData.triggerVolume15Second = dayData.volume15Second.get(trigger);
    }

    public static void calculateVolume10Second(DayData dayData) {
        for (int i = 0; i < dayData.volumeL.size() - 11; i++) {
            Double currentVolume = dayData.volumeL.get(i);
            Double volume10SecondAgo = dayData.volumeL.get(i + 9);
            dayData.volume10Second.add((int) (currentVolume - volume10SecondAgo));
        }
        for (int i = 0; i <= 10; i++) {
            dayData.volume10Second.add(0);
        }
    }

    public static void calculateVolume10SecondAtTrigger(DayData dayData, int trigger) {
        dayData.triggerVolume10Second = dayData.volume10Second.get(trigger);
    }

    // this is technically 30 minutes and 1 second.
    public static void calculateVolume30MinutesBefore5(DayData dayData){
        for (int i = 0; i < dayData.volumeL.size() - 2099; i++) { // 35 minutes - 1 second
            double volume5MinutesAgo = dayData.volumeL.get(i + 299);
            double volume35MinutesAgo = dayData.volumeL.get(i + 2099);
            dayData.volume30MinutesBefore5.add((int)(volume5MinutesAgo - volume35MinutesAgo));
        }
        for (int i = 0; i < 2099; i++) {
            dayData.volume30MinutesBefore5.add(0);
        }
    }

    public static void calculateVolume30MinutesBefore5AtTrigger(DayData dayData, int trigger){
        int returnVol = -1;
        try {
            double volume5MinutesAgo = dayData.volumeL.get(trigger + 299);
            double volume35MinutesAgo = dayData.volumeL.get(trigger + 2099);
            returnVol = (int)(volume5MinutesAgo - volume35MinutesAgo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error with volume calculation");
            dayData.triggerVolume30MinutesBefore5 = -1;
        }
        dayData.triggerVolume30MinutesBefore5 = returnVol;
    }

    public static void calculateVolumeMinute(DayData dayData) {
        for (int i = 0; i < dayData.volumeL.size() - 59; i++) {
            double currentVolume = dayData.volumeL.get(i);
            double volume59SecondsAgo = dayData.volumeL.get(i + 59);
            dayData.volumeMinute.add((int)(currentVolume - volume59SecondsAgo));
        }
        for (int i = 0; i < 59; i++) { // add the dead values to the end.
            dayData.volumeMinute.add(0);
        }
    }

    public static void calculateVolumeSeconds(DayData dayData) {
        for (int i = 0; i < dayData.volumeL.size() - 1; i++) {
            double currentSecondVolume = dayData.volumeL.get(i);
            double previousSecondVolume = dayData.volumeL.get(i + 1);
            dayData.volumeSeconds.add((int) (currentSecondVolume - previousSecondVolume));
        }
        dayData.volumeSeconds.add(0);
    }

    public static void calculateAverageMinuteVolume30PreMarket(DayData dayData) {
        Double minutesCandleVolTotal = 0.0;
        for (int i = 27902; i <= 29702; i = i + 60) {
            Double candleCloseVolume = dayData.volumeL.get(i);
            Double candleOpenVolume = dayData.volumeL.get(i + 60);
            Double diff = candleCloseVolume - candleOpenVolume;
            minutesCandleVolTotal += diff;
        }
        dayData.averageMinuteVolume30PreMarket = minutesCandleVolTotal / 30;
    }

    public static void calculateTotalMorningVolume(DayData dayData) {
        dayData.totalMorningVolume = dayData.volumeL.get(27902); // 27902 is 1 second before the market opens.
    }


}
