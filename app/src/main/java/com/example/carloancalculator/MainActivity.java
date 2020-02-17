package com.example.carloancalculator;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.math.MathUtils;

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
        if(!TextUtils.isEmpty(editCost.getText()) && getDouble(editCost) != 0) {
            double dblCarCost = getDouble(editCost);
            if (!TextUtils.isEmpty(editPayment.getText())) {
                double dblDownPayment = getDouble(editPayment);
                if (!TextUtils.isEmpty(editAPR.getText()) && getDouble(editAPR) != 0) {
                    double dblAPR = getDouble(editAPR) / 100;
                    double dblMPR = dblAPR / 12;
                    double dblMonths = getDouble(tvNumMonths);

                    if (boolLease) {
                        double dblLeasedCar = (dblCarCost / 3) - dblDownPayment;
                        double dblNegMonths = -36;
                        double dblMonthlyPayments = (dblMPR * dblLeasedCar) / (1 - Math.pow((1 + dblMPR), dblNegMonths));
                        dblMonthlyPayments = roundTwoDecimal(dblMonthlyPayments);
                        editMonthlyPayment.setText("$" + dblMonthlyPayments + "");
                    } else if (!boolLease) {
                        double dblBoughtCar = dblCarCost - dblDownPayment;
                        double dblNegMonths = -dblMonths;
                        double dblMonthlyPayments = (dblMPR * (dblBoughtCar) / (1 - Math.pow((1 + dblMPR), dblNegMonths)));
                        dblMonthlyPayments = roundTwoDecimal(dblMonthlyPayments);
                        editMonthlyPayment.setText("$" + dblMonthlyPayments + "");
                    }
                }else{
                    toastError = Toast.makeText(this, "Please enter a valid non-zero decimal number into APR field.", Toast.LENGTH_LONG);
                    toastError.show();
                }
            }else{
                toastError = Toast.makeText(this, "Please enter a valid number.", Toast.LENGTH_LONG);
                toastError.show();
            }
        }else{
            toastError = Toast.makeText(this, "Please enter a valid non-zero number into Cost Of Car field..", Toast.LENGTH_LONG);
            toastError.show();
        }
    }
}

