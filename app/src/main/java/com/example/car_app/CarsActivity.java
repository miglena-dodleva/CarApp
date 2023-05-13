package com.example.car_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarsActivity extends DbActivity implements Validation {


    protected EditText editName, editRegistrationNumber, editBrandAndModel;
    protected Button btnUpdate, btnDelete;
    protected Button btnSendMessage;
    protected String ID;


    protected boolean MatchString (String regex1, String text) {
        final String regex = regex1; //;
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

    protected void BackToMain(){
        finishActivity(200);
        Intent i = new Intent(CarsActivity.this,
                MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editName=findViewById(R.id.editName);
        editRegistrationNumber=findViewById(R.id.editRegistrationNumber);
        editBrandAndModel=findViewById(R.id.editBrandAndModel);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        btnSendMessage=findViewById(R.id.btnSendMessage);
        Bundle b = getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            editName.setText(b.getString("Name"));
            editRegistrationNumber.setText(b.getString("RegistrationNumber"));
            editBrandAndModel.setText(b.getString("BrandAndModel"));
        }

        Validation.Validate(editName,
                editRegistrationNumber,
                editBrandAndModel,
                () -> btnUpdate.setEnabled(true),
                () -> btnUpdate.setEnabled(false));

        btnDelete.setOnClickListener(view->{
            try{
                ExecSQL(
                        "DELETE FROM car " +
                                "WHERE ID = ?",
                        new Object[]{ID},
                        ()-> Toast.makeText(getApplicationContext(),
                                "DELETE SUCCEEDED",
                                Toast.LENGTH_LONG
                        ).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                                e.getLocalizedMessage(), Toast.LENGTH_LONG)
                        .show();
            }finally {
                BackToMain();
            }

        });
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selected="";

                Intent intent = new Intent(CarsActivity.this, SMSActivity.class);
                Bundle currentBundle = new Bundle();
                currentBundle.putString("number", editRegistrationNumber.getText().toString());
                intent.putExtras(currentBundle);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(view->{
            try{
                ExecSQL(
                        "UPDATE car SET " +
                                "Name = ?, " +
                                "RegistrationNumber = ?, " +
                                "BrandAndModel = ? " +
                                "WHERE ID = ? ",
                        new Object[]{
                                editName.getText().toString(),
                                editRegistrationNumber.getText().toString(),
                                editBrandAndModel.getText().toString(),
                                ID
                        },
                        ()->Toast.makeText(getApplicationContext(),
                                "UPDATE SUCCEEDED",
                                Toast.LENGTH_LONG
                        ).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),
                                e.getLocalizedMessage(), Toast.LENGTH_LONG)
                        .show();
            }finally {
                BackToMain();
            }

        });

    }
}
