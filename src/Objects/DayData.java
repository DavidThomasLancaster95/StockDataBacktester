package Objects;

import Calculation.TimeCalculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayData {

    //other things
    String stockDay;

    // primary indicators
    public float stockFloat;
    public String stockName;
    public ArrayList<Double> markDL;
    public ArrayList<Double> priceL;
    public ArrayList<Double> volumeL;
    public ArrayList<Integer> tickL;
    // secondaryIndicators
    public ArrayList<Boolean> sortIndicator;

    public ArrayList<Integer> volumeSeconds;
    public ArrayList<Integer> tickSeconds;
    public ArrayList<Double> PCI;
    public ArrayList<Integer> volumeMinute;
    public double firstHourLow;
    public ArrayList<Integer> tickMinute;
    public ArrayList<Integer> tick15Second;
    public ArrayList<Double> PCIMinute;
    public ArrayList<Double> PCI15Second;
    public ArrayList<Integer> volume30MinutesBefore5;
    public ArrayList<Integer> tick30MinutesBefore5;
    public ArrayList<Double> priceSpread30MinutePCIPre5;
    public ArrayList<Double> movingAverage5By5;
    public ArrayList<Integer> win4To2Fluctuation;
    public ArrayList<Integer> win4To2FluctuationSum;
    public ArrayList<Integer> volume15Second;

    public ArrayList<Integer> pointHighArray;
    public ArrayList<Integer> pointHighSumArray;
    public ArrayList<String> tradeStrategy4To2;
    public ArrayList<String> tradeStrategy2To1;


    // trigger value variables;
    public double triggerPCI;
    public int triggerVolumeSecond;
    public int triggerTickSecond;
    public int triggerVolumeMinute;
    public int triggerTime;
    public int triggerStrategy4To2;
    public int triggerStrategy2To1;
    public String triggerTimeAsString;
    public int triggerTickMinute;
    public double triggerPCIMinute;
    public int triggerVolume30MinutesBefore5;
    public int triggerTick30MinutesBefore5;
    public double triggerMarkD;
    public double triggerPriceSpread30MinutePCIPre5;
    public double triggerMovingAverage5By5;
    public int triggerWin4To2FluctuationSum;
    public int triggerPointHighSumArray;
    public int triggerTick15Second;
    public int triggerVolume15Second;
    public double triggerPCI15Second;

    public DayData(float stockFloat, String stockName) {
        this.stockFloat = stockFloat;
        this.stockName = stockName;
        markDL = new ArrayList<>();
        priceL = new ArrayList<>();
        volumeL = new ArrayList<>();
        tickL = new ArrayList<>();
        tradeStrategy4To2 = new ArrayList<>();
        tradeStrategy2To1 = new ArrayList<>();
        sortIndicator = new ArrayList<>();
        pointHighArray = new ArrayList<>();
        pointHighSumArray = new ArrayList<>();
        firstHourLow = 9999;
        tickMinute = new ArrayList<>();
        PCIMinute = new ArrayList<>();
        volume30MinutesBefore5 = new ArrayList<>();
        tick30MinutesBefore5 = new ArrayList<>();
        priceSpread30MinutePCIPre5 = new ArrayList<>();
        movingAverage5By5 = new ArrayList<>();
        win4To2Fluctuation = new ArrayList<>();
        win4To2FluctuationSum = new ArrayList<>();
        PCI = new ArrayList<>();
        volumeSeconds = new ArrayList<>();
        tickSeconds = new ArrayList<>();
        tick15Second = new ArrayList<>();
        volume15Second = new ArrayList<>();
        PCI15Second = new ArrayList<>();
        volumeMinute = new ArrayList<>();

        // trigger value variables initiate
        triggerPCI = -99999.0;
        triggerVolumeSecond = -1;
        triggerTickSecond = -1;
        triggerVolumeMinute = -1;
        triggerTime = -1;
        triggerStrategy4To2 = -1;
        triggerStrategy2To1 = -1;
        triggerTimeAsString = "";
        triggerTickMinute = -1;
        triggerPCIMinute = -9999.9;
        triggerVolume30MinutesBefore5 = -1;
        triggerTick30MinutesBefore5 = -1;
        triggerMarkD = -9999.9;
        triggerPriceSpread30MinutePCIPre5 = -1;
        triggerMovingAverage5By5 = -9999.9;
        triggerWin4To2FluctuationSum = -1;
        triggerTick15Second = -1;
        triggerVolume15Second = -1;
        triggerPCI15Second = -99999.0;

    }

    public void calculateTriggerValues(int trigger, List<String> headers) {

        triggerTime = trigger;
        triggerTimeAsString = TimeCalculation.ConvertTimeIntToString(trigger);

        for (String head: headers) {
            if (head.equals("volumeSecond")) {
                if (this.volumeSeconds.size() == 0) {
                    //System.out.println("Calculating VolumeSeconds");
                    calculateVolumeSeconds();
                }
                triggerVolumeSecond = this.volumeSeconds.get(trigger);
            }
            if (head.equals("PCI")) {
                if (this.PCI.size() == 0) {
                    //System.out.println("Calculating PCI");
                    calculatePCI();
                }
                triggerPCI = this.PCI.get(trigger);
            }
            if (head.equals("tickSecond")) {
                if (this.tickSeconds.size() == 0) {
                    calculateTickSeconds();
                }
                triggerTickSecond = this.tickSeconds.get(trigger);
            }
            if (head.equals("volumeMinute")) {
                if (this.volumeMinute.size() == 0) {
                    calculateVolumeMinute();
                }
                triggerVolumeMinute = this.volumeMinute.get(trigger);
            }
            if (head.equals("4% - 2%")) {
                this.triggerStrategy4To2 = calculateWinStrategyByTypeAndStart(0.04, -0.02, trigger);
            }
            if (head.equals("2% - 1%")) {
                this.triggerStrategy2To1 = calculateWinStrategyByTypeAndStart(0.02, -0.01, trigger);
            }
            if (head.equals("firstHourLow") && firstHourLow == 9999) {
                calculateFirstHourLow();
            }
            if (head.equals("tickMinute")) {
                calculateMinuteTicksAtTrigger(trigger);
            }
            if (head.equals("PCIMinute")) {
                calculatePCIMinuteAtTrigger(trigger);
            }
            if (head.equals("volume30MinutesBefore5")){
                calculateVolume30MinutesBefore5AtTrigger(trigger);
            }
            if (head.equals("tick30MinutesBefore5")) {
                calculateTick30MinutesBefore5AtTrigger(trigger);
            }
            if (head.equals("markD")) {
                this.triggerMarkD = this.markDL.get(trigger);
            }
            if (head.equals("priceSpread30MinutePCIPre5")) {
                calculatePriceSpread30MinutePCIPre5AtTrigger(trigger);
            }
            if (head.equals("movingAverage5By5")) {
                if (movingAverage5By5.size() == 0) calculateMovingAverage5By5();
                calculateMovingAverage5By5AtTrigger(trigger);
            }
            if (head.equals("win4To2FluctuationSum")) {
                if (win4To2FluctuationSum.size() == 0) calculateWin4To2FluctuationSum();
                calculateWin4To2FluctuationSumAtTrigger(trigger);
            }
            if (head.equals("pointHighSum")) {
                if (pointHighArray.size() == 0) calculatePointHighArray();
                if (pointHighSumArray.size() == 0) calculatePointHighSumArray();
                calculatePointHighSumArrayAtTrigger(trigger);
            }
            if (head.equals("tick15Second")) {
                calculateTick15SecondAtTrigger(trigger);
            }
            if (head.equals("volume15Second")) {
                calculateVolume15SecondAtTrigger(trigger);
            }
            if (head.equals("PCI15Second")) {
                calculatePCI15SecondAtTrigger(trigger);
            }
        }


    }

    public double calculateTriggerPCI(int trigger) {
        // get price before trigger
        double beforeTrigger = this.priceL.get(trigger);
        // get price at trigger
        double atTrigger = this.priceL.get(trigger - 1);

        return (atTrigger - beforeTrigger)/ beforeTrigger;
    }

    public void initiateSecondaryIndicators(List<SimpleParameter> simpleParameterList) {
        this.volumeSeconds = new ArrayList<>();
        this.tickSeconds = new ArrayList<>();
        this.PCI = new ArrayList<>();
        this.volumeMinute = new ArrayList<>();


        for (SimpleParameter parameter: simpleParameterList) {
            if (parameter.headerName.equals("volumeSecond")) {
                calculateVolumeSeconds();
            }
            if (parameter.headerName.equals("tickSeconds")) {
                calculateTickSeconds();
            }
            if (parameter.headerName.equals("PCI")) {
                calculatePCI();
            }
            if (parameter.headerName.equals("firstHourLow")) {
                calculateFirstHourLow();
            }
            if (parameter.headerName.equals("volumeMinute")) {
                calculateVolumeMinute();
            }
            if (parameter.headerName.equals("tickMinute")) {
                calculateMinuteTicks();
            }
            if (parameter.headerName.equals("PCIMinute")) {
                calculatePCIMinute();
            }
            if (parameter.headerName.equals("volume30MinutesBefore5")) {
                calculateVolume30MinutesBefore5();
            }
            if (parameter.headerName.equals("tick30MinutesBefore5")) {
                calculateTick30MinutesBefore5();
            }
            if (parameter.headerName.equals("priceSpread30MinutePCIPre5")) {
                calculatePriceSpread30MinutePCIPre5();
            }
            if (parameter.headerName.equals("movingAverage5By5")) {
                calculateMovingAverage5By5();
            }
            if (parameter.headerName.equals("win4To2FluctuationSum")) {
                calculateWin4To2FluctuationSum();
            }
            if (parameter.headerName.equals("pointHighSum")) {
                calculatePointHighSumArray();
            }
            if (parameter.headerName.equals("tick15Second")) {
                calculateTick15Second();
            }
            if (parameter.headerName.equals("volume15Second")) {
                calculateVolume15Second();
            }
            if (parameter.headerName.equals("PCI15Second")) {
                calculatePCI15Second();
            }
        }

        calculateSortIndicator(); // this has to run after PCI because it uses it.

        //this.tradeStrategy4To2 = calculateWinStrategy(0.04, -0.02);
        //this.tradeStrategy2To1 = calculateWinStrategy(0.02, -0.01);
        //calculatePointHighArray();
    }

    public void calculatePCI15Second() {

        for (int i = 0; i < priceL.size() - 16; i++) {
            double currentPrice = priceL.get(i);
            double price15SecondsAgo = priceL.get(i + 14);
            PCI15Second.add(calculatePCIBetween2Points(currentPrice, price15SecondsAgo));
        }
        for (int i = 0; i < 15; i++) {
            PCI15Second.add(0.0);
        }
    }


    public void calculatePCI15SecondAtTrigger(int trigger) {
        this.triggerPCI15Second = PCI15Second.get(trigger);
    }

    public void calculateVolume15Second() {
        for (int i = 0; i < volumeL.size() - 16; i++) {
            Double currentVolume = volumeL.get(i);
            Double volume15SecondAgo = volumeL.get(i + 14);
            volume15Second.add((int) (currentVolume - volume15SecondAgo));
        }
        for (int i = 0; i <= 15; i++) {
            volume15Second.add(0);
        }
    }

    public void calculateVolume15SecondAtTrigger(int trigger) {
        this.triggerVolume15Second = volume15Second.get(trigger);
    }

    public void calculateTick15Second() {
        //System.out.println("calculatingTick15Second");
        for (int i = 0; i < tickL.size() - 15; i++) {
            int currentTick = tickL.get(i);
            int tick15SecondsAgo = tickL.get(i + 14);
            tick15Second.add(currentTick - tick15SecondsAgo);
        }
        for (int i = 0; i <= 15; i++) {
            tick15Second.add(0);
        }
    }

    public void calculateTick15SecondAtTrigger(int trigger) {
        this.triggerTick15Second = tick15Second.get(trigger);
    }

    public void calculateWin4To2FluctuationSum() {
        // if it hasn't already been calculated, we need to initiate the trade strategy.
        if (tradeStrategy4To2.size() == 0) calculate4To2WinStrategy();

//        for (int i = tradeStrategy4To2.size() - 2; i >= 0; i--) {
//            String currentValue = tradeStrategy4To2.get(i);
//            String nextValue = tradeStrategy4To2.get(i + 1);
//            if (!currentValue.equals(nextValue)) {
//                win4To2Fluctuation.add(1);
//            } else {
//                win4To2Fluctuation.add(0);
//            }
//        }

        for (int i = 0; i < tradeStrategy4To2.size() - 1; i++) {
            String currentValue = tradeStrategy4To2.get(i);
            String nextValue = tradeStrategy4To2.get(i + 1);
            if (!currentValue.equals(nextValue)) {
                win4To2Fluctuation.add(1);
            } else {
                win4To2Fluctuation.add(0);
            }
        }

        win4To2Fluctuation.add(0);

        for (int i = 0; i < win4To2Fluctuation.size() - 1; i++) {
            int sumUpToIndex = win4To2Fluctuation.subList(i, win4To2Fluctuation.size() -1 ).stream().mapToInt(a -> a).sum();
            win4To2FluctuationSum.add(sumUpToIndex);
        }
    }

    public void calculateWin4To2FluctuationSumAtTrigger(int trigger) {
        triggerWin4To2FluctuationSum = win4To2FluctuationSum.get(trigger);
    }

    public void calculate4To2WinStrategy() {
        this.tradeStrategy4To2 = calculateWinStrategy(0.04, -0.02);
    }

    public void calculateMovingAverage5By5() {
        for (int i = 0; i < priceL.size() - 299; i++) {
            // now we have to sub loop for each of these.
            ArrayList<Double> last5MinValues = new ArrayList<>();
            for (int j = i; j < (i + 299); j = j + 5) { // skip 5 seconds each time
                last5MinValues.add(priceL.get(j));
            }
            // now we add the average to the main array
            double average5Min = last5MinValues.stream().mapToDouble(a -> a).sum() / last5MinValues.size();
            movingAverage5By5.add(average5Min);
        }

        for (int i = 0; i < 299; i++) {
            movingAverage5By5.add(0.0);
        }
    }

    public void calculateMovingAverage5By5AtTrigger(int trigger) {
        triggerMovingAverage5By5 = movingAverage5By5.get(trigger);
    }

    public void calculatePriceSpread30MinutePCIPre5(){
        // for each line minus the last 35 minutes
        for (int i = 0; i < priceL.size() - 2099; i++) {
            // find the max and the min for the 30 minutes before that point
            double min = Collections.min(priceL.subList(i + 299, i + 2099));
            double max = Collections.max(priceL.subList(i + 299, i + 2099));
            priceSpread30MinutePCIPre5.add(calculatePCIBetween2Points(max, min));
        }
        for (int i = 0; i < 2099; i++) {
            this.priceSpread30MinutePCIPre5.add(0.0);
        }
    }

    public void calculatePriceSpread30MinutePCIPre5AtTrigger(int trigger) {
        double min = Collections.min(priceL.subList(trigger + 299, trigger + 2099));
        double max = Collections.max(priceL.subList(trigger + 299, trigger + 2099));
        triggerPriceSpread30MinutePCIPre5 = calculatePCIBetween2Points(max, min);
    }

    public void calculateTick30MinutesBefore5() {
        for (int i = 0; i < tickL.size() - 2099; i++) {
            int tick5MinutesAgo = tickL.get(i + 299);
            int tick35MinutesAgo = tickL.get(i + 2099);
            this.tick30MinutesBefore5.add(tick5MinutesAgo - tick35MinutesAgo);
        }
        for (int i = 0; i < 2099; i++) {
            this.tick30MinutesBefore5.add(0);
        }
    }

    public void calculateTick30MinutesBefore5AtTrigger(int trigger) {
        int returnTick = -1;
        try {
            int tick5MinutesAgo = tickL.get(trigger + 299);
            int tick35MinutesAgo = tickL.get(trigger + 2099);
            returnTick = tick5MinutesAgo-tick35MinutesAgo;
        } catch (Exception e){
            e.printStackTrace();
        }
        this.triggerTick30MinutesBefore5 = returnTick;
    }

    // this is technically 30 minutes and 1 second.
    public void calculateVolume30MinutesBefore5(){
        for (int i = 0; i < volumeL.size() - 2099; i++) { // 35 minutes - 1 second
            double volume5MinutesAgo = volumeL.get(i + 299);
            double volume35MinutesAgo = volumeL.get(i + 2099);
            this.volume30MinutesBefore5.add((int)(volume5MinutesAgo - volume35MinutesAgo));
        }
        for (int i = 0; i < 2099; i++) {
            this.volume30MinutesBefore5.add(0);
        }
    }

    public void calculateVolume30MinutesBefore5AtTrigger(int trigger){
        int returnVol = -1;
        try {
            double volume5MinutesAgo = volumeL.get(trigger + 299);
            double volume35MinutesAgo = volumeL.get(trigger + 2099);
            returnVol = (int)(volume5MinutesAgo - volume35MinutesAgo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error with volume calculation");
            this.triggerVolume30MinutesBefore5 = -1;
        }
        this.triggerVolume30MinutesBefore5 = returnVol;
    }

    public void calculatePCIMinute(){
        for (int i = 0; i < this.priceL.size() - 59; i++) {
            double currentPrice = priceL.get(i);
            double price59SecondsAgo = priceL.get(i + 59);
            double PCI = calculatePCIBetween2Points(currentPrice, price59SecondsAgo);
            PCIMinute.add(PCI);
        }
        for (int i = 0; i < 59; i++) {
            this.PCIMinute.add(0.0);
        }
    }

    public void calculatePCIMinuteAtTrigger(int trigger) {
        double returnPCI = -9999.9;
        try {
            double currentPrice = priceL.get(trigger);
            double price59SecondsAgo = priceL.get(trigger + 59);
            returnPCI = calculatePCIBetween2Points(currentPrice, price59SecondsAgo);
        } catch (Exception e) {
            e.printStackTrace();
            this.triggerPCIMinute = -9999.9;
        }
        this.triggerPCIMinute = returnPCI;
    }

    public void calculateMinuteTicks() {
        for (int i = 0; i < this.tickL.size() - 59; i++) {
            int currentTick = tickL.get(i);
            int tick59SecondsAgo = tickL.get(i + 59);
            tickMinute.add(currentTick - tick59SecondsAgo);
        }
        for (int i = 0; i < 59; i++) {
            this.tickMinute.add(0);
        }
    }

    public void calculateMinuteTicksAtTrigger(int trigger) {
        int returnInt = -1;
        try {
            int currentTick = tickL.get(trigger);
            int tick59SecondsAgo = tickL.get(trigger + 59);
            returnInt = currentTick - tick59SecondsAgo;
        } catch(Exception e) {
            e.printStackTrace();
            this.triggerTickMinute = -2;
        }
        this.triggerTickMinute = returnInt;
    }

    public void calculateSortIndicator(){ //initiate everything to zero
        for (int i = 0; i < this.priceL.size(); i++) {
            this.sortIndicator.add(false);
        }
    }

    public void emptySortIndicator(){
        for (int i = 0; i < this.sortIndicator.size(); i++) {
            sortIndicator.set(i, false);
        }
    }

    public int calculateWinStrategyByTypeAndStart(double winPercentage, double lossPercentage, int startingLine) {
        double currentPrice = priceL.get(startingLine);
        int success = 0;
        for (int i = startingLine; i > 0; i--) {
            double pciAtThisLine = calculatePCIBetween2Points(priceL.get(i), currentPrice);
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

    public ArrayList<String> calculateWinStrategy(double winPercentage, double lossPercentage) {
        ArrayList<String> strategyL = new ArrayList<>();
        for (int i = 0; i < this.priceL.size(); i++) {
            double iPrice = priceL.get(i);
            for (int j = i; j >=0; j--) {
                double jPrice = priceL.get(j);
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
        //System.out.println("Finished Calculating Win Strategy for stock");
        System.out.println(System.currentTimeMillis());
        return strategyL;
    }

    public void calculatePointHighSumArray() {
        // if it hasn't already been calculated, we need to initiate the trade strategy.
        if (pointHighArray.size() == 0) calculatePointHighArray();

        for (int i = 0; i < pointHighArray.size() - 1; i++) {
            int sumUpToIndex = pointHighArray.subList(i, pointHighArray.size() - 1).stream().mapToInt(a -> a).sum();
            pointHighSumArray.add(sumUpToIndex);
        }


        pointHighSumArray.add(0);
    }

    public void calculatePointHighSumArrayAtTrigger(int trigger) {
        triggerPointHighSumArray = pointHighSumArray.get(trigger);
    }

    public void calculatePointHighArray() {
        //System.out.println("calculating point high array...");
        for (int i = 0; i < 150; i++) {
            this.pointHighArray.add(0/*null*/);
        }
        for (int i = 150; i < this.priceL.size() - 150; i++) {
            // get max value in array before
            ArrayList<Double> beforeArray = new ArrayList<>(priceL.subList((i-150), (i)));
            double beforeMax = Collections.max(beforeArray);
            // get max value in array after wards
            ArrayList<Double> afterArray = new ArrayList<>(priceL.subList((i+1), (i+150)));
            double afterMax = Collections.max(afterArray);
            // if it fits, add it.
            double currentPrice = priceL.get(i);
            if ((currentPrice > beforeMax) && (currentPrice > afterMax)) {
                pointHighArray.add(1);
                System.out.println("found a point");
            } else {
                pointHighArray.add(0);
            }
        }
        for (int i = 0; i < 150; i++) {
            this.pointHighArray.add(0/*null*/);
        }
        //System.out.println("finished calculating.");
    }

    public void calculateVolumeMinute() {
        for (int i = 0; i < this.volumeL.size() - 59; i++) {
            double currentVolume = volumeL.get(i);
            double volume59SecondsAgo = volumeL.get(i + 59);
            volumeMinute.add((int)(currentVolume - volume59SecondsAgo));
        }
        for (int i = 0; i < 59; i++) { // add the dead values to the end.
            this.volumeMinute.add(0);
        }
    }

    public void calculateVolumeSeconds() {
        for (int i = 0; i < this.volumeL.size() - 1; i++) {
            double currentSecondVolume = volumeL.get(i);
            double previousSecondVolume = volumeL.get(i + 1);
            volumeSeconds.add((int) (currentSecondVolume - previousSecondVolume));
        }
        volumeSeconds.add(0);
    }

    public void calculateTickSeconds() {
        for (int i = 0; i < this.tickL.size() - 1; i++) {
            double currentSecondVolume = tickL.get(i);
            double previousSecondVolume = tickL.get(i + 1);
            tickSeconds.add((int) (currentSecondVolume - previousSecondVolume));
        }
        tickSeconds.add(0);
    }

    public void calculatePCI() {
        for (int i = 0; i <= this.priceL.size() - 2; i++) {
            double currentSecondVolume = priceL.get(i);
            double previousSecondVolume = priceL.get(i + 1);
            PCI.add(((currentSecondVolume - previousSecondVolume)/ previousSecondVolume));
        }
        PCI.add(0.0);
    }

    public void calculateFirstHourLow() {
        //System.out.println(this.priceL.size());
        for (int i = TimeCalculation.lineOfTime930(); i > TimeCalculation.lineOfTime1030(); i--) {
            if (this.priceL.get(i) < this.firstHourLow) firstHourLow = this.priceL.get(i);
        }
    }

    public ArrayList<Integer> getVolumeSeconds() {
        return volumeSeconds;
    }

    public ArrayList<Integer> getTickSeconds() {
        return tickSeconds;
    }

    public ArrayList<Double> getPCI() {
        return PCI;
    }

    public void addMarkDL(double inDouble) {
        this.markDL.add(inDouble);
    }

    public void addPriceL(double inDouble) {
        this.priceL.add(inDouble);
    }

    public void addVolumeL(double inDouble) {
        this.volumeL.add(inDouble);
    }

    public void addTickL(int inInt) {
        tickL.add(inInt);
    }

    public float getStockFloat() {
        return stockFloat;
    }

    public String getStockName() {
        return stockName;
    }

    public ArrayList<Double> getMarkDL() {
        return markDL;
    }

    public ArrayList<Double> getPriceL() {
        return priceL;
    }

    public ArrayList<Double> getVolumeL() {
        return volumeL;
    }

    public ArrayList<Integer> getTickL() {
        return tickL;
    }

    public double calculatePCIBetween2Points(double newPrice, double oldPrice) {
        return ((newPrice - oldPrice)/ oldPrice);
    }

    public String getStockDay() {
        return stockDay;
    }

    public void setStockDay(String stockDay) {
        this.stockDay = stockDay;
    }
}