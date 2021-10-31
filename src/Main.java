import Backtesting.TriggerModler;
import Calculation.TimeCalculation;
import ExcelCommunication.ExcelCommunicator;
import GeneticAlgorithm.PracticeGenetic;
import Objects.DayData;
import Tests.FunctionTests;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;


import static ExcelCommunication.ExcelCommunicator.*;

public class Main {
    public static void main(String[] args) throws IOException {

        //PracticeGenetic practiceGenetic = new PracticeGenetic();
        //practiceGenetic.mainMethod();

//        ExcelCommunicator excelCommunicator = new ExcelCommunicator();
//
//        //List<List<String>> records = getCSV2("D:\\Stocks\\MMM\\DataAsCSV\\FullVersion.csv");
//        List<List<String>> records = getCSV2("D:\\Stocks\\MMM\\DataAsCSV\\D4070221full.csv");
//
//        System.out.println("Downloaded from csv ");
//
//        ArrayList<DayData> fullList = getAllData(records);
//
//        fullList.forEach(DayData::initiateSecondaryIndicators);
//
//        for (DayData data: fullList) {
//            if(data.pointHighArray.stream().mapToDouble(a -> a).sum() >0) {
//                System.out.println(data.stockName);
//            }
//        }


//        ExcelCommunicator excelCommunicator = new ExcelCommunicator();
//        List<List<String>> records = getCSV2("D:\\Stocks\\MMM\\DataAsCSV\\FullVersion.csv");
//        DayData dayData = getStockByNumber(records, 20);
//        dayData.initiateSecondaryIndicators();

        //pushToCSV();


        //FunctionTests functionTests = new FunctionTests("070121", "D:\\Stocks\\MMM\\",
                //"C:\\Users\\white\\Documents\\School\\CS240\\ExcelTests\\src\\");

        //functionTests.printArrays();


        String programSettingsURL = "C:\\Users\\dazon\\IdeaProjects\\StockDataBacktester\\src\\";
        //String programSettingsURL = "C:\\Users\\white\\Documents\\School\\CS240\\ExcelTests\\src\\";
        String homeDirectory = "C:\\Users\\dazon\\Desktop\\MMM\\";
        //String homeDirectory = "D:\\Stocks\\MMM\\";

        System.out.println(TimeCalculation.ConvertTimeIntToString(27901));

        TriggerModler triggerModler = new TriggerModler(programSettingsURL, homeDirectory, "output1.csv", "parameters.txt");
        triggerModler.runBackTest();

        TriggerModler triggerModler2 = new TriggerModler(programSettingsURL, homeDirectory, "output2.csv", "parameters2.txt");
        triggerModler2.runBackTest();

        TriggerModler triggerModler3 = new TriggerModler(programSettingsURL, homeDirectory, "output3.csv", "parameters3.txt");
        triggerModler3.runBackTest();

        TriggerModler triggerModler4 = new TriggerModler(programSettingsURL, homeDirectory, "output4.csv", "parameters4.txt");
        triggerModler4.runBackTest();

        TriggerModler triggerModler5 = new TriggerModler(programSettingsURL, homeDirectory, "output5.csv", "parameters5.txt");
        triggerModler5.runBackTest();

        System.out.println("asdf");

    }







}
