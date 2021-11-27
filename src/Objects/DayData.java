package Objects;

import java.util.ArrayList;
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
    public ArrayList<Double> tickL;

    // Universal Stuff
    //public ArrayList<ArrayList> universalIndicatorsArray;
    public ArrayList<IndicatorColumn> indicatorColumnArray;
    public ArrayList<TriggerPair> triggerPairArray;



    public DayData(float stockFloat, String stockName) {
        this.stockFloat = stockFloat;
        this.stockName = stockName;
        markDL = new ArrayList<>();
        priceL = new ArrayList<>();
        volumeL = new ArrayList<>();
        tickL = new ArrayList<>();
        indicatorColumnArray = new ArrayList<>();
        triggerPairArray = new ArrayList<>();
    }

    public void calculateTriggerValues(int trigger, List<String> headers, IndicatorDetailsArray indicatorDetailsArray) {

        for (String head: headers) {
            Indicators.UniversalIndicator.ProcessTriggers(this, head, trigger, indicatorDetailsArray);
        }

        System.out.println("triggers calculated");
    }

    public void initiateSecondaryIndicators(List<Constraint> constraintList, IndicatorDetailsArray indicatorDetailsArray) {




        // ------------- UNIVERSAL INDICATOR ---------------
        for (Constraint constraint: constraintList) {
            Indicators.UniversalIndicator.ProcessIndicators(this, indicatorDetailsArray, constraint.headerName);
        }
        //System.out.println("hopefully this worked :)");
        // -------------------------------------------------




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

    public void addTickL(double inDouble) {
        tickL.add(inDouble);
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

    public ArrayList<Double> getTickL() {
        return tickL;
    }

    public static double calculatePCIBetween2Points(double newPrice, double oldPrice) {
        return ((newPrice - oldPrice)/ oldPrice);
    }

    public String getStockDay() {
        return stockDay;
    }

    public void setStockDay(String stockDay) {
        this.stockDay = stockDay;
    }

    public IndicatorColumn getIndicatorColumnByName(String name) {
        if (name.equals("markD")) {
            return new IndicatorColumn("markD", this.markDL);
        }
        if (name.equals("price")) {
            return new IndicatorColumn("price", this.priceL);
        }
        if (name.equals("tickDay")) {
            return new IndicatorColumn("tick", this.tickL);
        }
        if (name.equals("volumeDay")) {
            return new IndicatorColumn("volume", this.volumeL);
        }
        for (IndicatorColumn indicatorColumn: indicatorColumnArray) {
            if (indicatorColumn.indicatorHeadName.equals(name)) {
                return indicatorColumn;
            }
        }
        return null;
    }



}
