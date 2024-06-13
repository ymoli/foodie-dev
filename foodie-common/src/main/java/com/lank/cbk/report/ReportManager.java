package com.lank.cbk.report;

import java.util.ArrayList;

public class ReportManager {
    public ArrayList<ReportLine> columns;
    public ReportManager(){
        columns = new ArrayList<>();
    }
    public void setColumns(ReportLine line){
        columns.add(line);
    }
    private void setColumns(ArrayList lines){
        columns = lines;
    }
}
