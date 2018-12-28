package com.vip.realtime.io.algorithem.logprocess;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LogProcessor {
    Map<String, String> file2Process = new HashMap<>();

    public void listFiles(String inputPath){
        File fileInput = new File(inputPath);
        if (fileInput.isDirectory()){
            File[] files = fileInput.listFiles();
            for(File file : files){
                if(file.isDirectory()){
                    listFiles(file.getAbsolutePath());
                }
                else{
                    file2Process.put(file.getAbsolutePath(), file.getName());
                    System.out.println("add file : " + file.getAbsolutePath());
                }
            }
        }
    }

    public void processFile(String fileName, String inputBase,  String outBase) throws Exception{
        System.out.println("process file : " + fileName);

        File file = new File(fileName);
        InputStreamReader read = new InputStreamReader(
                new FileInputStream(file), "UTF-8");

        String inputPath = file.getAbsolutePath();
        String outPath = inputPath.replace(inputBase, outBase);


        String outPathDir = outPath.substring(0, outPath.lastIndexOf("/"));
        File outFileDir = new File(outPathDir);
        if(!outFileDir.exists()){
            outFileDir.mkdirs();
        }

        FileWriter writer = new FileWriter(outPath,false);

        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;

        while ((lineTxt = bufferedReader.readLine()) != null) {
            if (lineTxt.contains("{\"appName\"")){
                lineTxt = lineTxt.substring(lineTxt.indexOf("{\"appName\""));
                writer.write(lineTxt);
                writer.write("\n");
            }
        }

        bufferedReader.close();
        read.close();
        writer.close();
    }

    public static void main(String[] args){
        String input = "/downloads/input";
        String output = "/downloads/output";
        if(args.length > 0 && args.length == 2){
            input = args[0];
            output = args[1];
        }
        LogProcessor processor = new LogProcessor();
        try {
            processor.listFiles(input);
            for(Map.Entry<String, String> fileEntry : processor.file2Process.entrySet()){
                processor.processFile(fileEntry.getKey(), input, output);
            }
//            processor.process(input, output);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
