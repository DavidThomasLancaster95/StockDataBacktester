package Backtesting;

import Objects.DayData;
import Objects.SimpleParameter;

import java.io.*;
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
    private List<SimpleParameter> simpleParameterList;
    private List<String> headers;
    private String outputFile;
    private String parameterFile;
    private String configFilePath;
    private String sourceFilePath;

    private String killSwitchName;
    private double killSwitchMin;
    private double killSwitchMax;


    public TriggerModler(String homeDirectory, String outputFileX, String parameterFileX) {
        configFilePath = "src/Configurations/";
        sourceFilePath = "src/";

        //configFilePath = "Configurations/";
        //sourceFilePath = "";

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
                dayData.initiateSecondaryIndicators(simpleParameterList);
                processSingleStock(dayData);
            }
            System.out.println(i);
        }

        System.out.println("finished processing subfile");
    }

    public void processSingleStock(DayData dayData) throws IOException {
        //System.out.println("asdf");
        //Search for trigger
        int triggerInt = -1;

        // --------- KILLSWITCH ---------
        if (!testKillSwitch(dayData)) return;
        // ------------------------------

        for (int i = dayData.priceL.size() - 1; i >= 0; i--) {
            //int curVol = dayData.getVolumeSeconds().get(i);
            // write the trigger criteria
            if (meetsCriteria(dayData, i/*dayData.getVolumeSeconds().get(i) > 10000 &&
                    i < TimeCalculation.lineOfTime1030() &&
                    dayData.priceL.get(i) <= dayData.firstHourLow*/)){
                System.out.println("trigger found - " + dayData.getStockName());
                triggerInt = i;
                break;
            }
        }
        if (triggerInt != -1) {
            // this means it was triggered
            //System.out.println("trigger found");
            dayData.calculateTriggerValues(triggerInt, headers);
            printDayDataValues(dayData, triggerInt);
        }
    }

    public void printDayDataValues(DayData dayData, int index) throws IOException {

        // printing to csv
        StringBuilder sb = new StringBuilder();
        // append the date no matter what
        sb.append(dayData.getStockDay() + ",");
        for (String head: this.headers) {

            if (head.equals("symbol")) sb.append(String.valueOf(dayData.stockName) + ",");
            if (head.equals("time")) sb.append(String.valueOf(dayData.triggerTimeAsString) + ",");
            if (head.equals("float")) sb.append(String.valueOf(dayData.stockFloat) + ",");

            if (head.equals("price")) sb.append(String.valueOf(dayData.getPriceL().get(index)) + ",");
            if (head.equals("PCI")) sb.append(String.valueOf(dayData.triggerPCI) + ",");
            if (head.equals("volumeDay")) sb.append(String.valueOf(dayData.volumeL.get(index)) + ",");
            if (head.equals("volumeSecond")) sb.append(String.valueOf(dayData.triggerVolumeSecond) + ",");
            if (head.equals("tickDay")) sb.append(String.valueOf(dayData.getTickL().get(index)) + ",");
            if (head.equals("tickSecond")) sb.append(String.valueOf(dayData.triggerTickSecond) + ",");
            if (head.equals("volumeMinute")) sb.append(String.valueOf(dayData.triggerVolumeMinute) + ",");

            if (head.equals("4% - 2%")) sb.append(String.valueOf(dayData.triggerStrategy4To2) + ",");
            if (head.equals("2% - 1%")) sb.append(String.valueOf(dayData.triggerStrategy2To1) + ",");
            if (head.equals("10% - 2%")) sb.append(String.valueOf(dayData.triggerStrategy10To2) + ",");
            if (head.equals("50% - 5%")) sb.append(String.valueOf(dayData.triggerStrategy50To5) + ",");
            if (head.equals("12% - 3%")) sb.append(String.valueOf(dayData.triggerStrategy12To3) + ",");

            if (head.equals("firstHourLow")) sb.append(String.valueOf(dayData.firstHourLow + ","));
            if (head.equals("tickMinute")) sb.append(String.valueOf(dayData.triggerTickMinute + ","));
            if (head.equals("PCIMinute")) sb.append(String.valueOf(dayData.triggerPCIMinute + ","));
            if (head.equals("volume30MinutesBefore5")) sb.append(String.valueOf(dayData.triggerVolume30MinutesBefore5) + ",");
            if (head.equals("tick30MinutesBefore5")) sb.append(String.valueOf(dayData.triggerTick30MinutesBefore5) + ",");
            if (head.equals("markD")) sb.append(String.valueOf(dayData.triggerMarkD) + ",");
            if (head.equals("priceSpread30MinutePCIPre5")) sb.append(String.valueOf(dayData.triggerPriceSpread30MinutePCIPre5) + ",");
            if (head.equals("movingAverage5By5")) sb.append(String.valueOf(dayData.triggerMovingAverage5By5) + ",");
            if (head.equals("win4To2FluctuationSum")) sb.append(String.valueOf(dayData.triggerWin4To2FluctuationSum) + ",");
            if (head.equals("pointHighSum")) sb.append(String.valueOf(dayData.triggerPointHighSumArray) + ",");

            if (head.equals("tick15Second")) sb.append(String.valueOf(dayData.triggerTick15Second) + ",");
            if (head.equals("tick10Second")) sb.append(String.valueOf(dayData.triggerTick10Second) + ",");
            if (head.equals("tick4Second")) sb.append(String.valueOf(dayData.triggerTick4Second) + ",");
            if (head.equals("tick10SecondBefore10Second")) sb.append(String.valueOf(dayData.triggerTick10SecondBefore10Second) + ",");

            if (head.equals("volume4Second")) sb.append(String.valueOf(dayData.triggerVolume4Second) + ",");
            if (head.equals("volume15Second")) sb.append(String.valueOf(dayData.triggerVolume15Second) + ",");
            if (head.equals("volume10Second")) sb.append(String.valueOf(dayData.triggerVolume10Second) + ",");
            if (head.equals("averageMinuteVolume30PreMarket")) sb.append(String.valueOf(dayData.averageMinuteVolume30PreMarket) + ",");
            if (head.equals("totalMorningVolume")) sb.append(String.valueOf(dayData.totalMorningVolume) + ",");
            if (head.equals("volume10SecondBefore10Second")) sb.append(String.valueOf(dayData.triggerVolume10SecondBefore10Second) + ",");

            if (head.equals("PCI4Second")) sb.append(String.valueOf(dayData.triggerPCI4Second) + ",");
            if (head.equals("PCI10Second")) sb.append(String.valueOf(dayData.triggerPCI10Second) + ",");
            if (head.equals("PCI15Second")) sb.append(String.valueOf(dayData.triggerPCI15Second) + ",");
            if (head.equals("previous1Hour4To2WinFluctuationSum")) sb.append(String.valueOf(dayData.triggerPrevious1Hour4To2WinFluctuationSum) + ",");

            if (head.equals("volumeToTickRatio10Second")) sb.append(String.valueOf(dayData.triggerVolumeToTickRatio10Second) + ",");

        }

        printSBToOutput(sb);

    }

    public boolean testKillSwitch(DayData dayData) {
        if (killSwitchName.equals("totalMorningVolume")) {
            if(dayData.totalMorningVolume > killSwitchMax || dayData.totalMorningVolume < killSwitchMin) return false;
        }
        return true;
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
        for (SimpleParameter simpleParameter: simpleParameterList) {
            if (simpleParameter.headerName.equals("volumeDay")) {
                if (dayData.volumeL.get(index) > simpleParameter.max || dayData.volumeL.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("price")) {
                if (dayData.priceL.get(index) > simpleParameter.max || dayData.priceL.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("time")) {
                if (index > simpleParameter.max || index < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("firstHourLow")) {
                if (simpleParameter.min == -1) {
                    if (dayData.priceL.get(index) > dayData.firstHourLow) return false;
                }
                if (simpleParameter.min == 1) {
                    if (dayData.priceL.get(index) < dayData.firstHourLow) return false;
                }
            }
            if (simpleParameter.headerName.equals("PCI")) {
                if (dayData.PCI.get(index) > simpleParameter.max || dayData.PCI.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("volumeSecond")) {
                if (dayData.getVolumeSeconds().get(index) > simpleParameter.max || dayData.getVolumeSeconds().get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tickDay")) {
                if (dayData.getTickL().get(index) > simpleParameter.max || dayData.getTickL().get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tickSecond")) {
                if (dayData.getTickSeconds().get(index) > simpleParameter.max || dayData.getTickSeconds().get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("volumeMinute")) {
                if (dayData.volumeMinute.get(index) > simpleParameter.max || dayData.volumeMinute.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tickMinute")) {
                if (dayData.tickMinute.get(index) > simpleParameter.max || dayData.tickMinute.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("PCIMinute")) {
                if (dayData.PCIMinute.get(index) > simpleParameter.max || dayData.PCIMinute.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("volume30MinutesBefore5")) {
                if (dayData.volume30MinutesBefore5.get(index) > simpleParameter.max || dayData.volume30MinutesBefore5.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tick30MinutesBefore5")) {
                if (dayData.tick30MinutesBefore5.get(index) > simpleParameter.max || dayData.tick30MinutesBefore5.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("markD")) {
                if (dayData.markDL.get(index) > simpleParameter.max || dayData.markDL.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("priceSpread30MinutePCIPre5")) {
                if (dayData.priceSpread30MinutePCIPre5.get(index) > simpleParameter.max || dayData.priceSpread30MinutePCIPre5.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("movingAverage5By5")) {
                if (dayData.movingAverage5By5.get(index) > simpleParameter.max || dayData.movingAverage5By5.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("win4To2FluctuationSum")) {
                if (dayData.win4To2FluctuationSum.get(index) > simpleParameter.max || dayData.win4To2FluctuationSum.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("pointHighSum")) {
                if (dayData.pointHighSumArray.get(index) > simpleParameter.max || dayData.pointHighSumArray.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tick15Second")) {
                if (dayData.tick15Second.get(index) > simpleParameter.max || dayData.tick15Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tick10Second")) {
                if (dayData.tick10Second.get(index) > simpleParameter.max || dayData.tick10Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tick4Second")) {
                if (dayData.tick4Second.get(index) > simpleParameter.max || dayData.tick4Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("tick10SecondBefore10Second")) {
                if (dayData.tick10SecondBefore10Second.get(index) > simpleParameter.max || dayData.tick10SecondBefore10Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("volume15Second")) {
                if (dayData.volume15Second.get(index) > simpleParameter.max || dayData.volume15Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("volume10Second")) {
                if (dayData.volume10Second.get(index) > simpleParameter.max || dayData.volume10Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("volume10SecondBefore10Second")) {
                if (dayData.volume10SecondBefore10Second.get(index) > simpleParameter.max || dayData.volume10SecondBefore10Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("PCI4Second")) {
                if (dayData.PCI4Second.get(index) > simpleParameter.max || dayData.PCI4Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("PCI15Second")) {
                if (dayData.PCI15Second.get(index) > simpleParameter.max || dayData.PCI15Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("PCI10Second")) {
                if (dayData.PCI10Second.get(index) > simpleParameter.max || dayData.PCI10Second.get(index) < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("averageMinuteVolume30PreMarket")) {
                if (dayData.averageMinuteVolume30PreMarket > simpleParameter.max || dayData.averageMinuteVolume30PreMarket < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("totalMorningVolume")) {
                if (dayData.totalMorningVolume > simpleParameter.max || dayData.totalMorningVolume < simpleParameter.min) return false;
            }
            if (simpleParameter.headerName.equals("volumeToTickRatio10Second")) {
                if (dayData.volumeToTickRatio10Second.get(index) > simpleParameter.max || dayData.volumeToTickRatio10Second.get(index) < simpleParameter.min) return false;
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
        simpleParameterList = new ArrayList<>();
        for (int i = 1; i < parameterRecords.size(); i++) { // starts on 1 because 0 is the headers.
            List<String> paramLine = parameterRecords.get(i);
            String testString = paramLine.get(0);
            SimpleParameter simpleParameter = new SimpleParameter(paramLine.get(0),
                    Double.parseDouble(paramLine.get(1)) , Double.parseDouble(paramLine.get(2)));
            simpleParameterList.add(simpleParameter);
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



    public void getDaysToBackTest() throws FileNotFoundException {
        File file = new File(configFilePath + "backtestdays.txt");
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()) {
            daysToBackTest.add(scanner.next());
        }
    }

}
