package Indicators;

import Objects.DayData;

public class Tick {
    public static void calculateTickSeconds(DayData dayData) {
        for (int i = 0; i < dayData.tickL.size() - 1; i++) {
            double currentSecondVolume = dayData.tickL.get(i);
            double previousSecondVolume = dayData.tickL.get(i + 1);
            dayData.tickSeconds.add((int) (currentSecondVolume - previousSecondVolume));
        }
        dayData.tickSeconds.add(0);
    }
    public static void calculateTick30MinutesBefore5(DayData dayData) {
        for (int i = 0; i < dayData.tickL.size() - 2099; i++) {
            int tick5MinutesAgo = dayData.tickL.get(i + 299);
            int tick35MinutesAgo = dayData.tickL.get(i + 2099);
            dayData.tick30MinutesBefore5.add(tick5MinutesAgo - tick35MinutesAgo);
        }
        for (int i = 0; i < 2099; i++) {
            dayData.tick30MinutesBefore5.add(0);
        }
    }

    public static void calculateTick30MinutesBefore5AtTrigger(DayData dayData, int trigger) {
        int returnTick = -1;
        try {
            int tick5MinutesAgo = dayData.tickL.get(trigger + 299);
            int tick35MinutesAgo = dayData.tickL.get(trigger + 2099);
            returnTick = tick5MinutesAgo-tick35MinutesAgo;
        } catch (Exception e){
            e.printStackTrace();
        }
        dayData.triggerTick30MinutesBefore5 = returnTick;
    }
    public static void calculateTick15Second(DayData dayData) {
        //System.out.println("calculatingTick15Second");
        for (int i = 0; i < dayData.tickL.size() - 15; i++) {
            int currentTick = dayData.tickL.get(i);
            int tick15SecondsAgo = dayData.tickL.get(i + 14);
            dayData.tick15Second.add(currentTick - tick15SecondsAgo);
        }
        for (int i = 0; i <= 15; i++) {
            dayData.tick15Second.add(0);
        }
    }

    public static void calculateTick15SecondAtTrigger(DayData dayData ,int trigger) {
        dayData.triggerTick15Second = dayData.tick15Second.get(trigger);
    }

    public static void calculateMinuteTicks(DayData dayData) {
        for (int i = 0; i < dayData.tickL.size() - 59; i++) {
            int currentTick = dayData.tickL.get(i);
            int tick59SecondsAgo = dayData.tickL.get(i + 59);
            dayData.tickMinute.add(currentTick - tick59SecondsAgo);
        }
        for (int i = 0; i < 59; i++) {
            dayData.tickMinute.add(0);
        }
    }

    public static void calculateMinuteTicksAtTrigger(DayData dayData, int trigger) {
        int returnInt = -1;
        try {
            int currentTick = dayData.tickL.get(trigger);
            int tick59SecondsAgo = dayData.tickL.get(trigger + 59);
            returnInt = currentTick - tick59SecondsAgo;
        } catch(Exception e) {
            e.printStackTrace();
            dayData.triggerTickMinute = -2;
        }
        dayData.triggerTickMinute = returnInt;
    }

    public static void calculateTick10Second(DayData dayData) {

        for (int i = 0; i < dayData.tickL.size() - 10; i++) {
            int currentTick = dayData.tickL.get(i);
            int tick10SecondsAgo = dayData.tickL.get(i + 9);
            dayData.tick10Second.add(currentTick - tick10SecondsAgo);
        }
        for (int i = 0; i <= 10; i++) {
            dayData.tick10Second.add(0);
        }
    }

    public static void calculateTick10SecondAtTrigger(DayData dayData ,int trigger) {
        dayData.triggerTick10Second = dayData.tick10Second.get(trigger);
    }

    public static void calculateTick4Second(DayData dayData) {

        for (int i = 0; i < dayData.tickL.size() - 4; i++) {
            int currentTick = dayData.tickL.get(i);
            int tick4SecondsAgo = dayData.tickL.get(i + 3);
            dayData.tick4Second.add(currentTick - tick4SecondsAgo);
        }
        for (int i = 0; i <= 4; i++) {
            dayData.tick4Second.add(0);
        }
    }

    public static void calculateTick4SecondAtTrigger(DayData dayData ,int trigger) {
        if (dayData.tick4Second.size() == 0) calculateTick4Second(dayData);
        dayData.triggerTick4Second = dayData.tick4Second.get(trigger);
    }

    public static void calculateTick10SecondBefore10Second(DayData dayData) {
        for (int i = 0; i < dayData.tickL.size() - 19; i++) { // 35 minutes - 1 second
            double tick10SecondsAgo = dayData.tickL.get(i + 9);
            double tick20SecondsAgo = dayData.tickL.get(i + 19);
            dayData.tick10SecondBefore10Second.add((int)(tick10SecondsAgo - tick20SecondsAgo));
        }
        for (int i = 0; i < 19; i++) {
            dayData.tick10SecondBefore10Second.add(0);
        }
    }

    public static void calculateTick10SecondBefore10SecondAtTrigger(DayData dayData, int trigger) {
        if (dayData.tick10SecondBefore10Second.size() == 0) calculateTick10SecondBefore10Second(dayData);
        dayData.triggerTick10SecondBefore10Second = dayData.tick10SecondBefore10Second.get(trigger);
    }
}
