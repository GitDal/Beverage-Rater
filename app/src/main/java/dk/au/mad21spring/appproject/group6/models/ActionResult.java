package dk.au.mad21spring.appproject.group6.models;

import android.content.Context;
import android.widget.Toast;

public class ActionResult {
    public boolean IsSuccess;
    public String Message;
    public String Error;

    public ActionResult(boolean isSuccess, String message, String error) {
        IsSuccess = isSuccess;
        Message = message;
        Error = error;
    }

    public static ActionResult Success(String message) {
        return new ActionResult(true, message, "");
    }

    public static ActionResult Error(String message, String error) {
        return new ActionResult(false, message, error);
    }

    public void ShowToastMessage(Context context) {
        if(IsSuccess) {
            Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String getErrorMessage() {
        return Message + "\n(" + Error + ")";
    }
}
