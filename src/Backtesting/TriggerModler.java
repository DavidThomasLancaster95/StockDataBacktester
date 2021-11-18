package Backtesting;

import Calculation.TimeCalculation;
import Objects.*;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ExcelCommunication.ExcelCommunicator.getCSV2;
import static ExcelCommunication.ExcelCommunicator.getStockByNumber;

public class TriggerModler {
    // Get days to model
    // write main loop
    private ArrayList<String> daysToBackTest;
    private String mProgramSettingsURL;
    private String MMMFolder;
    private List<Constraint> constraintList;
    private List<String> headers;
    private String outputFile;
    private String parameterFile;
    private String configFilePath;
    private String sourceFilePath;

    public IndicatorDetailsArray indicatorDetailsArray;


    private String killSwitchName;
    private double killSwitchMin;
    private double killSwitchMax;


    public TriggerModler(String homeDirectory, String outputFileX, String parameterFileX) {
        configFilePath = "src/Configurations/";
        sourceFilePath = "src/";

        //configFilePath = "Configurations/";
        //sourceFilePath = "";
        indicatorDetailsArray = new IndicatorDetailsArray();
        daysToBackTest = new ArrayList<>();
        MMMFolder = homeDirectory;
        outputFile = outputFileX;
        parameterFile = parameterFileX;
        loadSimpleParameterList();
        killSwitchName = "UNDEFINED";
        killSwitchMin = -1.0;
        killSwitchMax = -1.0;
        loadKillswitch();
        printCSVHeaders();
        loadIndicatorDetailsArray();

    }

    public void runBackTest() throws IOException {
        getDaysToBackTest();
        for (String day: daysToBackTest) {
            backTestDay(day);
        }

    }

    public void backTestDay(String day) throws IOException {
        System.out.println(day);
        for (int i = 1; i <= 14; i++) {
            String subFileUrl = MMMFolder + "\\DataAsCSV\\" + day + "\\D" + i + day + ".csv";
            processSubFile(subFileUrl, day);
        }
        // add the buffer to results
    }

    public void processSubFile(String subFile, String day) throws IOException {
        System.out.println("Started processing subfile");
        // get raw data from file
        List<List<String>> records = getCSV2(subFile);
        // go through each of the 200 stocks in the subfile
        for (int i = 1; i <= 200; i++) {
            DayData dayData = getStockByNumber(records, i);
            dayData.setStockDay(day);
            if (dayData.priceL != null && dayData.priceL.size() > 1) { // if there was an NA it is null //
                                                                       // update all NA's should be 0s

                processSingleStock(dayData);
            }
            System.out.println(i);
        }

        System.out.println("finished processing subfile");
    }

    public void processSingleStock(DayData dayData) throws IOException {
        dayData.initiateSecondaryIndicators(constraintList, indicatorDetailsArray);
        //System.out.println("asdf");
        //Search for trigger
        int triggerInt = -1;

        // --------- KILLSWITCH ---------
        //if (!testKillSwitch(dayData)) return;
        // ------------------------------

        for (int i = dayData.priceL.size() - 2; i >= 0; i--) {
            //int curVol = dayData.getVolumeSeconds().get(i);
            // write the trigger criteria
            if (meetsCriteria(dayData, i)){
                System.out.println("trigger found - " + dayData.getStockName());
                triggerInt = i;
                break;
            }
        }
        if (triggerInt != -1) {
            // this means it was triggered
            //System.out.println("trigger found");
            dayData.calculateTriggerValues(triggerInt, headers, indicatorDetailsArray);
            printDayDataValues(dayData, triggerInt);
        }
    }

    public void printDayDataValues(DayData dayData, int index) throws IOException {

        // printing to csv
        StringBuilder sb = new StringBuilder();
        // append the date no matter what
        sb.append(dayData.getStockDay() + ",");
        sb.append(dayData.getStockName() + ",");
        sb.append(TimeCalculation.ConvertTimeIntToString(index) + ",");
        sb.append(dayData.priceL.get(index) + ",");
        for (String head: this.headers) {
            for (TriggerPair triggerPair: dayData.triggerPairArray) {
                if (triggerPair.getHeaderName().equals(head)) {
                    sb.append(triggerPair.getTriggerValue() + ",");
                }
            }
        }

        printSBToOutput(sb);

    }

    public void printSBToOutput(StringBuilder sb) {
        try {
            File file = new File(sourceFilePath + outputFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(sb.toString());
            //pw.flush();
            pw.close();
            bw.close();
            fw.close();

            System.out.println("Data successfully appended at the end of file");
        } catch (IOException ioe) {
            System.out.println("exception occurred:");
            ioe.printStackTrace();
        }
    }

    public void printCSVHeaders() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Date" + ",");
            sb.append("Symbol" + ",");
            sb.append("time" + ",");
            sb.append("price" + ",");
            for (String head: this.headers) {
                sb.append(head + ",");
            }


            File file = new File(sourceFilePath + outputFile);
            if (!file.exists()){
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(sb.toString());
            //pw.flush();
            pw.close();
            bw.close();
            fw.close();

            System.out.println("Data successfully appended at the end of file");

        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
    }

    public boolean meetsCriteria(DayData dayData, int index){

        // for every parameter
        for (Constraint constraint : constraintList) {
            //System.out.println(constraint.headerName + " " + index);
            IndicatorColumn indicatorColumn = dayData.getIndicatorColumnByName(constraint.headerName);

            boolean wasPrime = false;
            if (indicatorColumn == null) {
                switch (constraint.headerName) {
                    case "time":
                        if (index > constraint.max) return false;
                        if (index < constraint.min) return false;
                        wasPrime = true;
                        break;
                    case "volumeDay":
                        if (dayData.volumeL.get(index) > constraint.max) return false;
                        if (dayData.volumeL.get(index) < constraint.min) return false;
                        wasPrime = true;
                        break;
                    case "markD":
                        if (dayData.markDL.get(index) > constraint.max) return false;
                        if (dayData.markDL.get(index) < constraint.min) return false;
                        wasPrime = true;
                        break;
                    case "tickDay":
                        if (dayData.tickL.get(index) > constraint.max) return false;
                        if (dayData.tickL.get(index) < constraint.min) return false;
                        wasPrime = true;
                        break;
                    case "price":
                        if (dayData.priceL.get(index) > constraint.max) return false;
                        if (dayData.priceL.get(index) < constraint.min) return false;
                        wasPrime = true;
                        break;
                    default:
                        try {
                            throw new Exception("the indicatorColumn referencing the constraint header name is null. You did something wrong and need to solve this problem. ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            if (!wasPrime) {
                if (indicatorColumn.getValueColumn().get(index) > constraint.max) return false;
                if (indicatorColumn.getValueColumn().get(index) < constraint.min) return false;
            }
        }
        // if the head matches a type of parameter we check for
        // compare the day data of that value. if false return false
        // after all of them return true if there have been no failures
        return true;
    }

    public void loadSimpleParameterList() {
        List<List<String>> parameterRecords = getCSV2(configFilePath + parameterFile);
        // get the data from parameters.txt
        // load them into a parameter object -> field : min : max
        // load it into an array.
        constraintList = new ArrayList<>();
        for (int i = 1; i < parameterRecords.size(); i++) { // starts on 1 because 0 is the headers.
            List<String> paramLine = parameterRecords.get(i);
            String testString = paramLine.get(0);
            Constraint constraint = new Constraint(paramLine.get(0),
                    Double.parseDouble(paramLine.get(1)) , Double.parseDouble(paramLine.get(2)));
            constraintList.add(constraint);
        }

        // this loads the headers in the parameter
        this.headers = parameterRecords.get(0);
    }

    public void loadKillswitch() {
        try {
            List<List<String>> parameterRecords = getCSV2(configFilePath + "killswitch.txt");
            List<String> paramLine = parameterRecords.get(0);
            killSwitchName = paramLine.get(0);
            killSwitchMin = Double.parseDouble(paramLine.get(1));
            killSwitchMax = Double.parseDouble(paramLine.get(2));
        } catch (Exception e) {
            System.out.println("There was a problem loading the kill switch data");
        }
    }

    public void loadIndicatorDetailsArray() {

        File indexFile = new File(sourceFilePath + "/Indicators/Indicators.json");
        String json = "UNDEFINED";

        try {
            json = Files.readString(Path.of(sourceFilePath + "/Indicators/Indicators.json"), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndicatorDetailsArray indicatorDetailsArrayX = new Gson().fromJson(json, IndicatorDetailsArray.class);
        this.indicatorDetailsArray = indicatorDetailsArrayX;
    }


    public void getDaysToBackTest() throws FileNotFoundException {
        File file = new File(configFilePath + "backtestdays.txt");
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()) {
            daysToBackTest.add(scanner.next());
        }
    }

}
