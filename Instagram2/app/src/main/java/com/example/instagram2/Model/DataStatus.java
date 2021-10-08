package com.example.instagram2.Model;

public class DataStatus {
    private String message = "";
    private String returnCode = "";
    private boolean success = false;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }

    public void setReturnCode(String returnCode){
        this.returnCode = returnCode;
    }
    public String getReturnCode(){
        return returnCode;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return success;
    }
}
