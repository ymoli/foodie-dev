package com.lank.cbk.report;

import com.lank.cbk.report.exception.IncorrectReportFieldException;
import org.apache.commons.lang3.StringUtils;

public class ReportField {
    public int seq;
    public int startPosition;
    public int length = -1;
    public String value;
    public String type;

    public static String ALIGNED_RIGHT = "right-aligned";
    public static String ALIGNED_LEFT = "left-aligned";

    //this method is to get field and save it
    public ReportField(int seq,int start,int len,String val) throws Exception{
        this.seq = seq;
        this.startPosition = start;
        this.length = len;
        StringBuffer sb = new StringBuffer(val);

        if (val.length() < len){
            int length = len - val.length();
            for (int x = 0; x < length; x++) {
                sb.insert(0," ");
            }
        }
        this.value = sb.toString();
        if (val.length() > len){
            throw new IncorrectReportFieldException("The number of field:" +seq+"[len:"+val.length()+"] is exceed the length[" +len+"]");
        }
    }

    public ReportField(int seq,int start,int len,String val,String type) throws Exception{
        this.seq = seq;
        this.startPosition = start;
        this.length = len;
        this.type = type;
        if (StringUtils.isBlank(val)){
            this.value = "";
            return;
        }
        StringBuffer sb = new StringBuffer(val.trim());
        if (val.length() < len){
            int length = len - val.length();
            if (type.equals(ALIGNED_RIGHT)){
                for (int x = 0; x < length; x++) {
                    sb.insert(0," ");
                }
            } else if (type.equals(ALIGNED_LEFT)){
                for (int x = 0; x < length; x++) {
                    sb.append(" ");
                }
            }

        }
        this.value = sb.toString();
        if (val.length() > len){
            throw new IncorrectReportFieldException("The number of field:" +seq+"[len:"+val.length()+"] is exceed the length[" +len+"]");
        }
    }

    public String toString(){
        return value;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
