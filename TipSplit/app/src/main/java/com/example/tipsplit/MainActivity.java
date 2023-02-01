package com.example.tipsplit;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText value1;
    private EditText value2;
    private TextView tt;
    private TextView tipAmount;
    private TextView perPerson;
    private RadioGroup rg;
    private Double totWithTip=0.0;
    private Double tip=0.0;
    private Double res=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        value1= findViewById(R.id.TotalwithTaxTextview);
        value2= findViewById(R.id.NumberofPeopleTextViewLand);
        tt= findViewById(R.id.TotalwithTipTextView);
        tipAmount= findViewById(R.id.tipAmount);
        perPerson= findViewById(R.id.result);
        rg=findViewById(R.id.radioGroup);


    }

    public void doCalculate(View v){
     String v1= value1.getText().toString();
     String v2= value2.getText().toString();
     String v4= tipAmount.getText().toString();
    /*String v5= tt.getText().toString();
     Double totWithTip= Double.parseDouble(v5);*/
     int numOfPeople= Integer.parseInt(v2);

        res= totWithTip /numOfPeople;
     perPerson.setText(String.format("%,.2f",res));
    }

    public void tipCalc(View v){
        String v3=value1.getText().toString();

        //Double tip=0.0;
        //Double totWithTip=0.0;
        if( v3.isEmpty()){
            doClear(v);
        }
        else {
            Double totWithTax=Double.parseDouble(v3);

            if (v.getId() == R.id.radioButton12) {
                tip = 0.12 * totWithTax;

                tipAmount.setText(String.format("%,.2f", tip));
            } else if (v.getId() == R.id.radioButton15) {
                tip = 0.15 * totWithTax;
                tipAmount.setText(String.format("%,.2f", tip));
            } else if (v.getId() == R.id.radioButton18) {
                tip = 0.18 * totWithTax;
                tipAmount.setText(String.format("%,.2f", tip));
            } else if (v.getId() == R.id.radioButton20) {
                tip = 0.20 * totWithTax;
                tipAmount.setText(String.format("%,.2f", tip));
            } else {
                tip = 0.0;
                tipAmount.setText(String.format("%,.2f", tip));
                Log.d(TAG, "Please select the tip percentage ");
            }

            totWithTip = totWithTax + tip;
            tt.setText(String.format("%,.2f", totWithTip));
        }

    }

    public void doClear(View v){

        value1.setText("");
        value2.setText("");
        tt.setText("");
        perPerson.setText("");
        tipAmount.setText("");
        rg.clearCheck();
        totWithTip=0.0;


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putDouble("totWTip",totWithTip);
        outState.putDouble("tipMoney",tip);
        outState.putDouble("totPerPerson",res);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        totWithTip= savedInstanceState.getDouble("totWTip");
        tip= savedInstanceState.getDouble("tipMoney");
        res= savedInstanceState.getDouble("totPerPerson");
        tt.setText(String.format("%,.2f", totWithTip));
        tipAmount.setText(String.format("%,.2f", tip));
        perPerson.setText(String.format("%,.2f",res));

    }

}