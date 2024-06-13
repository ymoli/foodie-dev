package com.lank.cbk.report;

import com.lank.cbk.report.exception.IncorrectReportLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class ReportLine {
    static Logger logger = LoggerFactory.getLogger(ReportLine.class);

    public Map<Integer, ReportField> fields = new HashMap<>();
    public int seq = 1;
    public static final int TOTAL_LENGTH = 2000;

    public ReportLine() throws Exception{
        fields = new HashMap<Integer, ReportField>();
    }
    public boolean addField(ReportField fd){
        fd.setSeq(seq);
        fields.put(seq,fd);
        seq++;
        return true;
    }
    public boolean checkFields() throws Exception{
        int fieldcount = 0;
        int totallen = 0;
        for (Map.Entry<Integer, ReportField> entry: fields.entrySet()){
            fieldcount++;
            totallen = totallen + entry.getValue().getLength();
        }
        if (fieldcount != (seq - 1)){
            throw new IncorrectReportLineException("File count not match");
        }
        if (totallen > TOTAL_LENGTH){
            throw new IncorrectReportLineException("Total Length is exceed than " + TOTAL_LENGTH);
        }
        return true;
    }
    public String toString(){
        String wsline = "";
        int wspoisition = 0;
        int wsActuralLen = 0;

        try {
            if (seq>1 && checkFields()){
                ReportField fd1 = fields.get(1);
                wspoisition = wspoisition + fd1.getStartPosition();
                if (wspoisition-1>0){
                    wsline = wsline+printSpaces(wspoisition-1);
                }
                wsline = wsline+fd1.getValue();
                wsActuralLen=wsline.length();
                for (int i = 2; i < seq; i++) {
                    fd1 = fields.get(i);
                    if (fd1.getStartPosition()-wsActuralLen-1>0){
                        wsline = wsline + printSpaces(fd1.getStartPosition()-wsActuralLen-1);
                    }
                    if (wsActuralLen>fd1.getStartPosition()){
                        wsline = wsline.substring(0,fd1.getStartPosition()-1);
                        System.out.println("Warning: The field seq"+i+"is overrided");
                    }
                    wsline = wsline + fd1.getValue();
                    wsActuralLen = wsline.length();
                }
            }
            return wsline + System.getProperty("line.separator");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return  "";
        }
    }
    public String printSpaces(int x){
        String spaces = "";
        for (int i = 1;i < x;i++){
            spaces = spaces+" ";
        }
        return spaces;
    }
}
