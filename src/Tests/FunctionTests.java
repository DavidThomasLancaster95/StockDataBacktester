package Tests;

import Objects.DayData;

import java.io.*;
import java.util.List;

import static ExcelCommunication.ExcelCommunicator.getCSV2;
import static ExcelCommunication.ExcelCommunicator.getStockByNumber;

public class FunctionTests {

    public String day;
    public String mHomeDirectory;
    private String mProgramSettingsURL;
    private DayData dayData;

    // function arrays


    public FunctionTests(String day, String mHomeDirectory, String mProgramSettingsURL) {
        this.day = day;
        this.mHomeDirectory = mHomeDirectory;
        this.mProgramSettingsURL = mProgramSettingsURL;
        String subFileUrl = mHomeDirectory + "\\DataAsCSV\\" + day + "\\D" + 1 + day + ".csv";
        List<List<String>> records = getCSV2(subFileUrl);
        dayData = getStockByNumber(records, 1);



    }



    public void testFunctions() {

    }

    public void printArrays() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n\n\n");
        sb.append("MarkD," + "Price," + "Volume," + "Ticks," + "PCI,," + "VolumeSeconds,," + "4% - 2%,," + "MinuteTicks,," + "TickSeconds,,"
                + "PCI Minute,," + "Volume30MinutesBefore5,," + "PriceSpread30MinutePCIPre5,," + "MovingAverage5By5,," + "4To2FluctuationSum,,"
                + "PointHighSumArray,," + "tick15Second,," + "volume15Second,," + "PCI15Second,," + "win4To2Fluctuation,," +
                "pointHighArray,," + "volumeMinute,," + "\n");

        //dayData.calculatePCI();
        Indicators.PCI.calculatePCI(dayData);
        //dayData.calculateVolumeSeconds();
        Indicators.Volume.calculateVolumeSeconds(dayData);
        //dayData.calculate4To2WinStrategy();
        Indicators.WinStrategies.calculate4To2WinStrategy(dayData);
        //dayData.calculateMinuteTicks();
        Indicators.Tick.calculateMinuteTicks(dayData);
        //dayData.calculateTickSeconds();
        Indicators.Tick.calculateTickSeconds(dayData);
        //dayData.calculatePCIMinute();
        Indicators.PCI.calculatePCIMinute(dayData);
        //dayData.calculateVolume30MinutesBefore5();
        Indicators.Volume.calculateVolume30MinutesBefore5(dayData);
        //dayData.calculatePriceSpread30MinutePCIPre5();
        Indicators.PriceSpreads.calculatePriceSpread30MinutePCIPre5(dayData);
        //dayData.calculateMovingAverage5By5();
        Indicators.MovingAverages.calculateMovingAverage5By5(dayData);
        //dayData.calculateWin4To2FluctuationSum();
        Indicators.FluctuationSum.calculateWin4To2FluctuationSum(dayData);
        //dayData.calculatePointHighSumArray();
        Indicators.PointHigh.calculatePointHighSumArray(dayData);
        //dayData.calculateTick15Second();
        Indicators.Tick.calculateTick15Second(dayData);
        //dayData.calculateVolume15Second();
        Indicators.Volume.calculateVolume15Second(dayData);
        //dayData.calculatePCI15Second();
        Indicators.PCI.calculatePCI15Second(dayData);
        //dayData.calculateWin4To2FluctuationSum();
        Indicators.FluctuationSum.calculateWin4To2FluctuationSum(dayData);
        //dayData.calculatePointHighArray();
        Indicators.PointHigh.calculatePointHighArray(dayData);
        //dayData.calculateVolumeMinute();
        Indicators.Volume.calculateVolumeMinute(dayData);
        //dayData.calculatePCI15Second();
        Indicators.PCI.calculatePCI15Second(dayData);



        for (int i = 0; i < dayData.markDL.size(); i++) {
            sb.append(dayData.markDL.get(i) + ",");
            sb.append(dayData.priceL.get(i) + ",");
            sb.append(dayData.volumeL.get(i) + ",");
            sb.append(dayData.tickL.get(i) + ",");
            sb.append(dayData.PCI.get(i) + ",,");
            sb.append(dayData.volumeSeconds.get(i) + ",,");
            sb.append(dayData.tradeStrategy4To2.get(i) + ",,");
            sb.append(dayData.tickMinute.get(i) + ",,");
            sb.append(dayData.tickSeconds.get(i) + ",,");
            sb.append(dayData.PCIMinute.get(i) + ",,");
            sb.append(dayData.volume30MinutesBefore5.get(i) + ",,");
            sb.append(dayData.priceSpread30MinutePCIPre5.get(i) + ",,");
            sb.append(dayData.movingAverage5By5.get(i) + ",,");
            sb.append(dayData.win4To2FluctuationSum.get(i) + ",,");
            sb.append(dayData.pointHighSumArray.get(i) + ",,");
            sb.append(dayData.tick15Second.get(i) + ",,");
            sb.append(dayData.volume15Second.get(i) + ",,");
            sb.append(dayData.PCI15Second.get(i) + ",,");
            sb.append(dayData.win4To2Fluctuation.get(i) + ",,");
            sb.append(dayData.pointHighArray.get(i) + ",,");
            sb.append(dayData.volumeMinute.get(i) + ",,");


            sb.append("\n");
        }
        printSBToOutput(sb);

    }

    public void printSBToOutput(StringBuilder sb) {
        System.out.println("trying to print results to -> " + mProgramSettingsURL);
        try {
            File file = new File(mProgramSettingsURL + "functionTests.csv");
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
}
