import Backtesting.TriggerModler;
import ExcelCommunication.ExcelCommunicator;
import Indicators.PCI;
import Objects.DayData;
import Tests.FunctionTests;

import java.io.*;
import java.util.List;

import static ExcelCommunication.ExcelCommunicator.getCSV2;
import static ExcelCommunication.ExcelCommunicator.getStockByNumber;


public class Main {
    //String homeDirectory = "C:\\Users\\dazon\\Desktop\\MMM\\";
    //String homeDirectory = "D:\\Stocks\\MMM\\";
    public static void main(String[] args) throws IOException {

        // playground crap delete later
//        ExcelCommunicator excelCommunicator = new ExcelCommunicator();
//        List<List<String>> records = getCSV2("D:\\Stocks\\MMM\\DataAsCSV\\FullVersion.csv");
//        DayData dayData = getStockByNumber(records, 20);
//        PCI.calculatePCI(dayData);
//        PCI.calculatePCI15Second(dayData);
        // ---------- END ----------

        String homeDirectory = args[0];
        int numberOfStrategies = Integer.parseInt(args[1]) ;
        System.out.println("StockDataBackTester. Version 1");
        System.out.println("homeDirectory:" + homeDirectory + " | numberOfStrategies: " + numberOfStrategies);

        //runFunctionTests();

        if (numberOfStrategies >= 1) {
            TriggerModler triggerModler = new TriggerModler(homeDirectory, "output1.csv", "parameters.txt");
            triggerModler.runBackTest();
        }
        if (numberOfStrategies >= 2) {
            TriggerModler triggerModler2 = new TriggerModler(homeDirectory, "output2.csv", "parameters2.txt");
            triggerModler2.runBackTest();
        }
        if (numberOfStrategies >= 3) {
            TriggerModler triggerModler3 = new TriggerModler(homeDirectory, "output3.csv", "parameters3.txt");
            triggerModler3.runBackTest();
        }
        if (numberOfStrategies >= 4) {
            TriggerModler triggerModler4 = new TriggerModler(homeDirectory, "output4.csv", "parameters4.txt");
            triggerModler4.runBackTest();
        }
        if (numberOfStrategies >= 5) {
            TriggerModler triggerModler5 = new TriggerModler(homeDirectory, "output5.csv", "parameters5.txt");
            triggerModler5.runBackTest();
        }

        System.out.println("end program");

    }

    public static void runFunctionTests() {
        // you're probably only running this in intellij, so that's why its has the src header.
        FunctionTests functionTests = new FunctionTests("070121", "D:\\Stocks\\MMM\\",
                "src/");

        functionTests.printArrays();
    }
}
