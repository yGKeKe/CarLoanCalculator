package com.example.carloancalculator;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekbarMonths;
    private TextView tvNumMonths;
    private EditText editCost, editPayment, editAPR, editMonthlyPayment;
    private double dblCarCost;
    private double dblDownPayment;
    private double dblAPR;
    private double dblMonths;
    private boolean boolLease;
    Toast toastError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFields();
        initListeners();
    }

    //parses and returns double from EditText
    private double getDouble(EditText editText){
        return Double.parseDouble(editText.getText().toString().trim());
    }

    //parses and returns double from TextView
    private double getDouble(TextView TextView){
        return Double.parseDouble(TextView.getText().toString().trim());
    }

    //method to format decimal numbers to two places after the decimal
    double roundTwoDecimal(double d){
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    //initiate fields.
    private void initFields(){
        seekbarMonths = findViewById(R.id.seekBarMonths);
        tvNumMonths = findViewById(R.id.textNumMonth);
        editAPR = findViewById(R.id.editAPR);
        editCost = findViewById(R.id.editCostOfCar);
        editPayment = findViewById(R.id.editDownPayment);
        editMonthlyPayment = findViewById(R.id.editMonthlyPayment);
    }

    //initiate SeekBar listener
    private void initListeners(){
        seekbarMonths.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvNumMonths.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    //detect radio button selection
    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.radLease:
                if(checked)
                    boolLease = true;
                break;
            case R.id.radBuy:
                if(checked)
                    boolLease = false;
                break;
        }
    }

    //calculate monthly payments on button click
    public void butCalcMonthlyPayment(View view){
        if(!TextUtils.isEmpty(editCost.getText())){
            dblCarCost = getDouble(editCost);
        }else{
            toastError = Toast.makeText(this, "Please enter a valid number.", Toast.LENGTH_LONG);
            toastError.show();
        }

        if(!TextUtils.isEmpty(editPayment.getText())){
            dblDownPayment = getDouble(editPayment);
        }else{
            toastError = Toast.makeText(this, "Please enter a valid number.", Toast.LENGTH_LONG);
            toastError.show();
        }

        if(!TextUtils.isEmpty(editAPR.getText())){
            dblAPR = getDouble(editAPR);
            dblAPR = dblAPR / 12;
        }else{
            toastError = Toast.makeText(this, "Please enter a valid number.", Toast.LENGTH_LONG);
            toastError.show();
        }

        dblMonths = getDouble(tvNumMonths);

        if(!boolLease){
            double dblLeasedCar = (dblCarCost - dblDownPayment) / 3;
            double dblNegMonths = -36;
            double dblMonthlyPayments = dblAPR * (dblLeasedCar / (1 - Math.pow(1 + dblAPR, dblNegMonths)));
            dblMonthlyPayments = roundTwoDecimal(dblMonthlyPayments);
            editMonthlyPayment.setText(dblMonthlyPayments+"");
        }else if(boolLease){
            double dblBoughtCar = dblCarCost - dblDownPayment;
            double dblNegMonths = -dblMonths;
            double dblMonthlyPayments = dblAPR * (dblBoughtCar / (1 - Math.pow(1 + dblAPR, dblNegMonths)));
            dblMonthlyPayments = roundTwoDecimal(dblMonthlyPayments);
            editMonthlyPayment.setText(dblMonthlyPayments+"");
        }
    }
}
