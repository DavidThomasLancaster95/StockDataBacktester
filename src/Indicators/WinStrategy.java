package Indicators;

import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.util.ArrayList;

import static Objects.DayData.calculatePCIBetween2Points;

public class WinStrategy {
    public static double calculateWinRatio(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        double winPercentage = Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        double lossPercentage = Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));
        ArrayList<Double> array = new ArrayList<>();
        if (onlyTrigger) {
            double val = calculateWinStrategyByTypeAndStart(dayData, winPercentage, lossPercentage, ifTrigger);
            return val;
        } else {
            array = calculateWinStrategy(dayData, winPercentage, lossPercentage);
        }

        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return 0;
    }
    private static ArrayList<Double> calculateWinStrategy(DayData dayData, double winPercentage, double lossPercentage) {
        ArrayList<Double> strategyL = new ArrayList<>();
        for (int i = 0; i < dayData.priceL.size(); i++) {
            double iPrice = dayData.priceL.get(i);
            for (int j = i; j >=0; j--) {
                double jPrice = dayData.priceL.get(j);
                if (calculatePCIBetween2Points(jPrice, iPrice) > winPercentage) {
                    strategyL.add(1.0);
                    break;
                }
                if (calculatePCIBetween2Points(jPrice, iPrice) < lossPercentage) {
                    strategyL.add(0.0);
                    break;
                }
                if (j == 0) {
                    strategyL.add(-9999.0);
                    break;
                }
            }
        }
        return strategyL;
    }

    public static int calculateWinStrategyByTypeAndStart(DayData dayData, double winPercentage, double lossPercentage, int startingLine) {
        double currentPrice = dayData.priceL.get(startingLine);
        int success = 0;
        for (int i = startingLine; i > 0; i--) {
            double pciAtThisLine = calculatePCIBetween2Points(dayData.priceL.get(i), currentPrice);
            if (pciAtThisLine >= winPercentage) {
                success = 1;
                break;
            }
            if (pciAtThisLine <= lossPercentage){
                break;
            }
        }
        return success;
    }
}
