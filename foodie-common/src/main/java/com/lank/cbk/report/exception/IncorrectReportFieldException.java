package com.lank.cbk.report.exception;

public class IncorrectReportFieldException extends CBKJavaException {
    public IncorrectReportFieldException(String reason){
        super("The report field is incorrect" +reason+"). Please check.");
    }
}
