package com.example.car_app;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Validation {

    public interface MatchSuccess {
        void MatchSuccess();

    }

    public interface MatchFail {
        void MatchFail();
    }

    static boolean MatchString (String regex1, String text) {
        final String regex = regex1; //"((\\((\\+|00)?\\d+\\))|(\\+|00))? ?\\d+([\\/ -]?(\\(\\d+\\)|\\d+))+(\\:\\d+)?";
        final String string = text;
        /*"+35955555555\n"
                + "032/23-23-23\n"
                + "02/555555555\n"
                + "+359 (32) 55555555\n"
                + "23 23 23\n"
                + "00359 (32) 555555:232\n"
                + "(+359) 33333333";*/

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            return true;
        }

        return false;
    }

    static void Validate(EditText editName,
                         EditText editRegistrationNumber,
                         EditText editBrandAndModel,
                         MatchSuccess success,
                         MatchFail fail
    ){
        if(
                MatchString(
                        "^((\\((\\+|00)?\\d+\\))|(\\+|00))? ?\\d+([\\/ -]?(\\(\\d+\\)|\\d+))+(\\:\\d+)?$",
                        editRegistrationNumber.getText().toString()
                )

        ){
            success.MatchSuccess();
        }else{
            fail.MatchFail();
        }

    }

}
