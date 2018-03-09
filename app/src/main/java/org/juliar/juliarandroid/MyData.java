package org.juliar.juliarandroid;

/**
 * Created by Andrey Makhanov on 12/4/2016.
 */
public class MyData {
    private String currdate,output,original;



    public MyData(String currdate, String output, String original) {
        this.currdate = currdate;
        this.output = output;
        this.original = original;
    }

    public String getCurrdate() {
        return currdate;
    }

    public void setCurrdate(String currdate) {
        this.currdate = currdate;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
