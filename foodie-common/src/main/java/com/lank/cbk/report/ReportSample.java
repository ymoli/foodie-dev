package com.lank.cbk.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class ReportSample{
    static Logger logger = LoggerFactory.getLogger(ReportSample.class);

    public void write(){
        try {
            ReportManager reportManager = new ReportManager();
            setHeader(reportManager);
            writeFile(reportManager);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }
    private void setHeader(ReportManager reportManager) throws Exception{
        ReportField column11 = new ReportField(1,1,108,"Bank     : 78          CITI NA",ReportField.ALIGNED_LEFT);
        ReportField column12 = new ReportField(2,110,23,"PAGE NO:1",ReportField.ALIGNED_LEFT);
        ReportField column21 = new ReportField(1,1,20,"TXN Status History",ReportField.ALIGNED_LEFT);
        ReportField column22 = new ReportField(2,42,18,"Reject reason",ReportField.ALIGNED_LEFT);
        ReportLine line1 = new ReportLine();
        line1.addField(column11);
        line1.addField(column12);
        ReportLine line2 = new ReportLine();
        line2.addField(column21);
        line2.addField(column22);
        reportManager.setColumns(line1);
        reportManager.setColumns(line2);
    }
    private void writeFile(ReportManager reportManager){
        String folderPath = "C:/Folder";
        Path path = Paths.get(folderPath);
        String name = "report.txt";
        Path reportFilePath = Paths.get(path.toString(),name);
        try {
            Iterator it1 = reportManager.columns.iterator();
            while (it1.hasNext()){
                ReportLine reportLine = (ReportLine)it1.next();
                Files.write(reportFilePath,reportLine.toString().getBytes("gbk"));
            }
        } catch (Exception e){
            logger.error(e.getMessage());
        }
    }

}
