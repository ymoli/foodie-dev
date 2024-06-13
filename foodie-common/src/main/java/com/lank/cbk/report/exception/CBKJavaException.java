package com.lank.cbk.report.exception;

public class CBKJavaException extends Exception{
    private Exception cbkJavaException = null;
    private String cbkJavaMessage = null;

    public CBKJavaException(){
        super();
    }

    public CBKJavaException(String msg){
        super(msg);
        cbkJavaMessage = msg;
    }

    public CBKJavaException(Exception e){
        cbkJavaException = e;
    }

    public CBKJavaException(String msg,Exception e){
        super(msg);
        cbkJavaMessage = msg;
        cbkJavaException = e;
    }

    public Exception getContainedException() {
        return cbkJavaException;
    }

    public String getContainedExceptionMessage() {
        if (cbkJavaMessage != null){
            return cbkJavaException.getMessage();
        } else {
            return null;
        }
    }

    public String getCbkJavaMessage() {
        return cbkJavaMessage;
    }

    public void setCbkJavaMessage(String cbkJavaMessage) {
        this.cbkJavaMessage = cbkJavaMessage;
    }
}
