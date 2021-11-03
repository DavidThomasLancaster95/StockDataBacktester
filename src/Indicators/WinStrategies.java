package Indicators;

import Objects.DayData;

import java.util.ArrayList;

import static Objects.DayData.calculatePCIBetween2Points;

public class WinStrategies {
    public static void calculate4To2WinStrategy(DayData dayData) {
        dayData.tradeStrategy4To2 = calculateWinStrategy(dayData,0.04, -0.02);
    }

    public static void calculate10To2WinStrategy(DayData dayData) {
        dayData.tradeStrategy10To2 = calculateWinStrategy(dayData,0.10, -0.02);
    }

    private static ArrayList<String> calculateWinStrategy(DayData dayData, double winPercentage, double lossPercentage) {
        ArrayList<String> strategyL = new ArrayList<>();
        for (int i = 0; i < dayData.priceL.size(); i++) {
            double iPrice = dayData.priceL.get(i);
            for (int j = i; j >=0; j--) {
                double jPrice = dayData.priceL.get(j);
                if (calculatePCIBetween2Points(jPrice, iPrice) > winPercentage) {
                    strategyL.add("1");
                    break;
                }
                if (calculatePCIBetween2Points(jPrice, iPrice) < lossPercentage) {
                    strategyL.add("0");
                    break;
                }
                if (j == 0) {
                    strategyL.add("No Movement");
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
