package Tests;

import Backtesting.TriggerModler;
import Objects.Constraint;
import Objects.DayData;
import Objects.IndicatorDetailsArray;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ExcelCommunication.ExcelCommunicator.getCSV2;
import static ExcelCommunication.ExcelCommunicator.getStockByNumber;

public class FunctionTests {

    public String day;
    public String mHomeDirectory;
    private String mProgramSettingsURL;
    private DayData dayData;
    private IndicatorDetailsArray indicatorDetailsArray;
    private ArrayList<String> headerArray;
    // function arrays


    public FunctionTests(String day, String mHomeDirectory, String mProgramSettingsURL) {
        this.day = day;
        this.mHomeDirectory = mHomeDirectory;
        this.mProgramSettingsURL = mProgramSettingsURL;
        String subFileUrl = mHomeDirectory + "\\DataAsCSV\\" + day + "\\D" + 1 + day + ".csv";
        List<List<String>> records = getCSV2(subFileUrl);
        dayData = getStockByNumber(records, 1);
        headerArray = new ArrayList<>();

        TriggerModler triggerModler = new TriggerModler(mHomeDirectory, "output1.csv", "parameters.txt");
        indicatorDetailsArray = triggerModler.indicatorDetailsArray;
    }

    public void initiateAllIndicators() {
        ArrayList<Constraint> constraintList = new ArrayList<>();

        headerArray.add("volume5Second");
        headerArray.add("tick5Second");
        headerArray.add("tick2Second");
        headerArray.add("volume2Second");
        headerArray.add("PCI1Second");
        headerArray.add("volume10SecondBefore10Second");
        headerArray.add("firstHourVolumeSum");
        headerArray.add("movingAverage5MinBy1Sec");
        headerArray.add("movingAverage10MinBy1Sec");
        headerArray.add("movingAverage60MinBy1Sec");
        headerArray.add("FirstHourHigh");
        headerArray.add("AboveFirstHourPrice");
        headerArray.add("MovingAverageChangeBinary");
        headerArray.add("DifFrom60MinMovingAverage");
        headerArray.add("60-10CrossOver");
        headerArray.add("AveragePCI");

        for (String head: headerArray) {
            constraintList.add(new Constraint(head, 1, 1));
        }

        dayData.initiateSecondaryIndicators(constraintList, indicatorDetailsArray);

        System.out.println("finisehd initiating indicators");

    }

    public void printAllIndicators() {
        StringBuilder sb = new StringBuilder();

        // print the headers
        sb.append("\n\n\n");
        sb.append("time," + "markD," + "price," + "volume," + "ticks,");
        headerArray.forEach(x -> sb.append(x + ",,"));
        sb.append("\n");

        // now we print all the lines.
        for (int i = 0; i < dayData.markDL.size(); i++) {
            // add the stupid header values
            sb.append(i + ",");
            sb.append(dayData.markDL.get(i) + ",");
            sb.append(dayData.priceL.get(i) + ",");
            sb.append(dayData.volumeL.get(i) + ",");
            sb.append(dayData.tickL.get(i) + ",");
            // how add all the written indicators
            final int j = i;
            dayData.indicatorColumnArray.forEach(x -> sb.append(x.valueColumn.get(j) + ",,"));

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
