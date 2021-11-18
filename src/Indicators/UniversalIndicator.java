package Indicators;

import Objects.*;

import java.util.ArrayList;

public class UniversalIndicator {

    // Goes through each IndicatorDetails(Json Blueprint) and checks if it is equal to the constraint name(the stuff on the side)
    public static void ProcessIndicators(DayData dayData, IndicatorDetailsArray indicatorDetailsArray, String constraintName) {
        for (IndicatorDetails indicatorDetails : indicatorDetailsArray.getIndicatorParamsSetArrayList()) {
            if (indicatorDetails.getTagname().equals(constraintName)) {

                // Once it finds one, it processes that specific indicator.
                processIndicator(indicatorDetails, dayData, false, indicatorDetailsArray, 0);

            }
        }
    }

    // Determines what type of strategy the IndicatorDetails(Json Blueprint) describes and then runs the right strategy on it
    public static double processIndicator(IndicatorDetails indicatorDetails, DayData dayData, Boolean onlyTrigger, IndicatorDetailsArray indicatorDetailsArray, int ifTrigger) {
        double returnVal = -666;
        // check all the preReqIndicators
        ArrayList<String> prerequisiteIndicators = indicatorDetails.getDependencies();
        for (String preReq: prerequisiteIndicators) {
            if (dayData.getIndicatorColumnByName(preReq) == null) {
                IndicatorDetails preReqIndicatorDetails = indicatorDetailsArray.getIndicatorDetailsByTagName(preReq);
                processIndicator(preReqIndicatorDetails, dayData, false, indicatorDetailsArray, 0);
            }
        }

        String indicatorType = indicatorDetails.getIndicatortype();

        if (indicatorType.equals("difference")) {
            returnVal = DifferenceStrategy.calculateDifferenceStrategy(indicatorDetails, dayData, onlyTrigger, indicatorDetailsArray, ifTrigger);
        }
        if (indicatorType.equals("PCIChange")) {
            returnVal = PCIChangeStrategy.calculatePCIStrategy(indicatorDetails, dayData, onlyTrigger, indicatorDetailsArray, ifTrigger);
        }
        if (indicatorType.equals("winRatio")) {
            returnVal = WinStrategy.calculateWinRatio(indicatorDetails, dayData, onlyTrigger, indicatorDetailsArray, ifTrigger);
        }

        //...
        return returnVal;
    }

    interface TriggerMethod {
        double calculateTrigger(int index);
    }



    // Processes the triggers
    // if the array is already defined it adds to the triggerPairArray the trigger-value
    // if the array does not exist, there are a few possibilities
    // -----> A. the indicator is not dependent on anything. Calculate the selective processing
    // -----> B. the indicator is dependent.
    public static void ProcessTriggers(DayData dayData, String head, int trigger, IndicatorDetailsArray indicatorDetailsArray) {

        //Check if the column already exists. If so add the Trigger Pair by just referencing via the index.
        IndicatorColumn indicatorColumn = dayData.getIndicatorColumnByName(head);

        if (indicatorColumn != null) {
            double triggerValue = indicatorColumn.getValueColumn().get(trigger);
            dayData.triggerPairArray.add(new TriggerPair(head, triggerValue));
            return;
        }

        // process all the preReqs
        IndicatorDetails indicatorDetails = indicatorDetailsArray.getIndicatorDetailsByTagName(head);
        for (String dependency: indicatorDetails.dependencies) {
            // check if the dependency is already generated.
            if (dayData.getIndicatorColumnByName(head).valueColumn == null) {
                IndicatorDetails dependencyIndicatorDetails = indicatorDetailsArray.getIndicatorDetailsByTagName(dependency);
                processIndicator(dependencyIndicatorDetails, dayData, false, indicatorDetailsArray, 0);
            }
        }

        double triggerVal = processIndicator(indicatorDetails, dayData, true, indicatorDetailsArray, trigger);

        dayData.triggerPairArray.add(new TriggerPair(head, triggerVal));

    }

}
