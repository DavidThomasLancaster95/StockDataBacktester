package Indicators;

import Calculation.MathCalculation;
import Objects.DayData;
import Objects.IndicatorColumn;
import Objects.IndicatorDetails;
import Objects.IndicatorDetailsArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

public class MovingAverageStrategy {
    public static double calculateMovingAverageStrategy(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = dayData.priceL;

        int secondsBack = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));
        int interval = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(1)));

        int end = secondsBack;

        final ArrayList<Double> finalArray = pointerArray;

        UniversalIndicator.TriggerMethod m1 = (index)-> {

            double priceSum = 0;
            int counter = 0;
            while (counter < secondsBack) {
                priceSum += finalArray.get(index + counter);
                counter += interval;
            }

            return priceSum/((int)(secondsBack / interval));
        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        int topBufferSize = 0;
        int bottomBufferSize = (int) Math.max(secondsBack, 0);


        // adding top buffer
        for (int i = 0; i < topBufferSize; i++) {
            array.add(0.0);
        }

        // main loop
        for (int i = 0; i < pointerArray.size() - Math.abs(end) - 1; i++) {

//            Double startVal = (Double) pointerArray.get(i + start);
//            Double endVal = (Double) pointerArray.get(i + end);
//            array.add(startVal - endVal);

            array.add(m1.calculateTrigger(i));
        }

        // adding bottom buffer
        for (int i = 0; i < bottomBufferSize + 1; i++) {
            array.add(0.0);
        }

        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return 0;
    }
    public static double calculateMovingAverageChangeDirectionBinaryStrategy(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        // #1
        ArrayList<Double> array = new ArrayList<>();

        // #2
        String dependantRangeTag = indicatorDetails.getDependencies().get(0);

        // #3
        final ArrayList<Double> finalArray = dayData.getIndicatorColumnByName(dependantRangeTag).valueColumn;

        UniversalIndicator.TriggerMethod m1 = (index)-> {

            //index = index - 1;

            double oneSecondLater = (double) finalArray.get(index + 1);
            double oneSecondBefore = 0.0;
            if (index > 0) {
                oneSecondBefore = (double) finalArray.get(index - 1);
            } else {
                oneSecondBefore = oneSecondLater;
            }

            double current = (double) finalArray.get(index);

            int currentBD = (int) (current * 1000000);
            int oneSecondBeforeBD = (int) (oneSecondBefore * 1000000);
            int oneSecondLaterBD = (int) (oneSecondLater * 1000000);

            if ((oneSecondBeforeBD > currentBD) & (oneSecondLaterBD > currentBD)) {
                return -1.0;
            } else if ((oneSecondBeforeBD < currentBD) & (oneSecondLaterBD < currentBD)) {
                return 1.0;
            } else {
                return 0.0;
            }
        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        // #4

        int topBufferSize = 0;
        int bottomBufferSize = 1;

        // #5

        // adding top buffer
        for (int i = 0; i < topBufferSize; i++) {
            array.add(0.0);
        }

        // main loop
        for (int i = 0; i < finalArray.size() - 2; i++) {

//            Double startVal = (Double) pointerArray.get(i + start);
//            Double endVal = (Double) pointerArray.get(i + end);
//            array.add(startVal - endVal);

            array.add(m1.calculateTrigger(i));
        }

        // adding bottom buffer
        for (int i = 0; i < bottomBufferSize + 1; i++) {
            array.add(0.0);
        }

        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return 0;

    }

    public static double calculatePercentageDifFromMovingAverage(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        // #1
        ArrayList<Double> array = new ArrayList<>();
        ArrayList pointerArray = dayData.priceL;

        // #2
        String dependantRangeTag = indicatorDetails.getDependencies().get(0);
        ArrayList<Double> movingAverage = dayData.getIndicatorColumnByName(dependantRangeTag).valueColumn;

        // #3
        final ArrayList<Double> finalArray = pointerArray;
        final ArrayList<Double> finalMovingAverage = movingAverage;

        UniversalIndicator.TriggerMethod m1 = (index)-> {

            double movingAveragePrice = finalMovingAverage.get(index);
            double currentPrice = finalArray.get(index);

            return (currentPrice - movingAveragePrice) / movingAveragePrice;

        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        // #4

        // #5
        for (int i = 0; i < dayData.priceL.size(); i++) {
            array.add(m1.calculateTrigger(i));
        }

        // #6
        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return 0;
    }

    public static double calculateMovingAverageCrossover(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        // #1
        ArrayList<Double> array = new ArrayList<>();

        // #2
        String dependantMATag = indicatorDetails.getDependencies().get(0);
        ArrayList<Double> movingAverage1 = dayData.getIndicatorColumnByName(dependantMATag).valueColumn;
        String dependantMATag2 = indicatorDetails.getDependencies().get(1);
        ArrayList<Double> movingAverage2 = dayData.getIndicatorColumnByName(dependantMATag2).valueColumn;

        int oneOverTwo1twoOverOne2Both3 = (int) Double.parseDouble(String.valueOf(indicatorDetails.getParams().get(0)));

        int start = 0;
        int end = 4;

        // #3
        final ArrayList<Double> finalArrayMA1 = movingAverage1;
        final ArrayList<Double> finalArrayMA2 = movingAverage2;

        UniversalIndicator.TriggerMethod m1 = (index)-> {

            double movingAverage1FourSecondsAgo = finalArrayMA1.get(index + 4);
            double movingAverage1TwoSecondsAgo = finalArrayMA1.get(index + 3);
            double movingAverage2FourSecondsAgo = finalArrayMA2.get(index + 3);
            double movingAverage2TwoSecondsAgo = finalArrayMA2.get(index + 2);


            if (oneOverTwo1twoOverOne2Both3 == 1) {
                if ((movingAverage1FourSecondsAgo < movingAverage2FourSecondsAgo) && (movingAverage1TwoSecondsAgo > movingAverage2TwoSecondsAgo)) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (oneOverTwo1twoOverOne2Both3 == 2) {
                if ((movingAverage1FourSecondsAgo > movingAverage2FourSecondsAgo) && (movingAverage1TwoSecondsAgo < movingAverage2TwoSecondsAgo)) {
                    return 1;
                } else {
                    return 0;
                }
            } else if (oneOverTwo1twoOverOne2Both3 == 3) {
                if (((movingAverage1FourSecondsAgo > movingAverage2FourSecondsAgo) && (movingAverage1TwoSecondsAgo < movingAverage2TwoSecondsAgo)) ||
                        (movingAverage1FourSecondsAgo < movingAverage2FourSecondsAgo) && (movingAverage1TwoSecondsAgo > movingAverage2TwoSecondsAgo)) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                System.out.println("you did something wrong");
            }

            return -666.0;
        };

        if (onlyTrigger) {
            return m1.calculateTrigger(ifTrigger);
        }

        // #4
        // get the longest value for buffer. We get the plans for the moving averages and we find the max amount of seconds.
        IndicatorDetails indicatorDetailsMA1 = indicatorDetailsArray.getIndicatorDetailsByTagName(dependantMATag);
        IndicatorDetails indicatorDetailsMA2 = indicatorDetailsArray.getIndicatorDetailsByTagName(dependantMATag2);

        double MA1Seconds = indicatorDetailsMA1.getParams().get(0);
        double MA2Seconds = indicatorDetailsMA2.getParams().get(0);

        int topBufferSize = (int) 0;
        int bottomBufferSize = (int) Math.max(4, Math.max(MA1Seconds, MA2Seconds));



        // adding top buffer
        for (int i = 0; i < topBufferSize; i++) {
            array.add(0.0);
        }

        // main loop
        for (int i = 0; i < dayData.priceL.size() - Math.abs(end) - 1; i++) {

//            Double startVal = (Double) pointerArray.get(i + start);
//            Double endVal = (Double) pointerArray.get(i + end);
//            array.add(startVal - endVal);

            array.add(m1.calculateTrigger(i));
        }

        // adding bottom buffer
        for (int i = 0; i < bottomBufferSize + 1; i++) {
            array.add(0.0);
        }

        IndicatorColumn indicatorColumn = new IndicatorColumn(indicatorDetails.tagname, array);
        dayData.indicatorColumnArray.add(indicatorColumn);
        return 0;
    }

}
