package Indicators;

import Objects.DayData;

import static Objects.DayData.calculatePCIBetween2Points;

public class PCI {

    public static void calculatePCIMinute(DayData dayData){
        for (int i = 0; i < dayData.priceL.size() - 59; i++) {
            double currentPrice = dayData.priceL.get(i);
            double price59SecondsAgo = dayData.priceL.get(i + 59);
            double PCI = calculatePCIBetween2Points(currentPrice, price59SecondsAgo);
            dayData.PCIMinute.add(PCI);
        }
        for (int i = 0; i < 59; i++) {
            dayData.PCIMinute.add(0.0);
        }
    }

    public static void calculatePCIMinuteAtTrigger(DayData dayData, int trigger) {
        double returnPCI = -9999.9;
        try {
            double currentPrice = dayData.priceL.get(trigger);
            double price59SecondsAgo = dayData.priceL.get(trigger + 59);
            returnPCI = calculatePCIBetween2Points(currentPrice, price59SecondsAgo);
        } catch (Exception e) {
            e.printStackTrace();
            dayData.triggerPCIMinute = -9999.9;
        }
        dayData.triggerPCIMinute = returnPCI;
    }


    public static void calculatePCI(DayData dayData) {
        for (int i = 0; i <= dayData.priceL.size() - 2; i++) {
            double currentSecondVolume = dayData.priceL.get(i);
            double previousSecondVolume = dayData.priceL.get(i + 1);
            dayData.PCI.add(((currentSecondVolume - previousSecondVolume)/ previousSecondVolume));
        }
        dayData.PCI.add(0.0);
    }

    public static void calculatePCI15Second(DayData dayData) {

        for (int i = 0; i < dayData.priceL.size() - 16; i++) {
            double currentPrice = dayData.priceL.get(i);
            double price15SecondsAgo = dayData.priceL.get(i + 14);
            dayData.PCI15Second.add(DayData.calculatePCIBetween2Points(currentPrice, price15SecondsAgo));
        }
        for (int i = 0; i < 15; i++) {
            dayData.PCI15Second.add(0.0);
        }
    }

    public static void calculatePCI15SecondAtTrigger(DayData dayData, int trigger) {
        dayData.triggerPCI15Second = dayData.PCI15Second.get(trigger);
    }
}
