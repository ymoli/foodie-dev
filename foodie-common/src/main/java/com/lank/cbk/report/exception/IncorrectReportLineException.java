package com.lank.cbk.report.exception;

public class IncorrectReportLineException extends CBKJavaException {
    public IncorrectReportLineException(String reason){
        super("The report line is incorrect" +reason+". Please check.");
    }
}
