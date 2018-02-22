package com.lasagneman.jlynch.rpechartcalculator;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lasagneman.jlynch.rpechartcalculator.R;
import com.lasagneman.jlynch.rpechartcalculator.Constants;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    EditText _lastSetWeight;
    Spinner _lastSetReps;
    Spinner _lastSetRpe;

    Spinner _nextSetReps;
    Spinner _nextSetRpe;
    Spinner _nextSetPercentageDrop;

    TextView _nextWorkingWeight;
    TextView _nextBackoffSetWeight;
    TextView _estimated1rm;

    private static DecimalFormat df2;

    static {
        df2 = new DecimalFormat(".##");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setBindings();

        setListeners();
    }

    private void setListeners() {


        _lastSetWeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateNextWorkingSet();
            }
        });

        _lastSetReps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateNextWorkingSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _lastSetRpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateNextWorkingSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _nextSetReps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateNextWorkingSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _nextSetRpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateNextWorkingSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _nextSetPercentageDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateNextWorkingSet();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setBindings() {
        _lastSetWeight = findViewById(R.id.etLastSetWeight);
        _lastSetReps = findViewById(R.id.spLastSetReps);
        _lastSetRpe = findViewById(R.id.spLastSetRpe);

        _nextSetReps = findViewById(R.id.spNextSetReps);
        _nextSetRpe = findViewById(R.id.spNextSetRpe);
        _nextSetPercentageDrop = findViewById(R.id.spNextSetPercentageDrop);

        _nextWorkingWeight = findViewById(R.id.tvNextWorkingWeight);
        _nextBackoffSetWeight = findViewById(R.id.tvBackoffSetWeight);
        _estimated1rm = findViewById(R.id.tvEstimatedOneRepMax);

//        _lastSetWeight.setText("0.00");
    }

    private boolean isSufficientOptionsSet() {
        String doubleValueString = _lastSetWeight.getText().toString();
        if (doubleValueString.equals("")) {
            return false;
        }

//        double doubleValue = Double.parseDouble(doubleValueString);
//        if (doubleValue == 0.0) {
//            return false;
//        }

        if (_lastSetReps.getSelectedItem() == null) return false;
        if (_lastSetRpe.getSelectedItem() == null) return false;
        if (_nextSetReps.getSelectedItem() == null) return false;
        if (_nextSetRpe.getSelectedItem() == null) return false;
        return true;
    }

    private void updateNextWorkingSet() {
        if (isSufficientOptionsSet()) {
            String str = _lastSetWeight.getText().toString();
            double lastSetWeight = Double.parseDouble(str);

            int chartRepIndexLast = _lastSetReps.getSelectedItemPosition();
            int chartRpeIndexLast = _lastSetRpe.getSelectedItemPosition();

            int chartRepIndexNext = _nextSetReps.getSelectedItemPosition();
            int chartRpeIndexNext = _nextSetRpe.getSelectedItemPosition();

            double lastModifier = 1.00;

            try {
                lastModifier = Constants.defaultRpeChart[chartRpeIndexLast][chartRepIndexLast];
            }
            catch (Exception e) {
                Toast.makeText(this, "x = " + chartRepIndexLast + ", y = " + chartRpeIndexLast, Toast.LENGTH_SHORT).show();
            }

            double nextModifier = 1.00;

            try {
                nextModifier = Constants.defaultRpeChart[chartRpeIndexNext][chartRepIndexNext];
            }
            catch (Exception e) {
                Toast.makeText(this, "x = " + chartRepIndexNext + ", y = " + chartRpeIndexNext, Toast.LENGTH_SHORT).show();
            }

            double estimated1rm = lastSetWeight / lastModifier;
            double nextWorkingSet = estimated1rm * nextModifier;

            int percentageListIndex = _nextSetPercentageDrop.getSelectedItemPosition();
            double nextBackoffSetModifier = Constants.percentageList[percentageListIndex];
            double nextBackoffSetWeight = nextWorkingSet * nextBackoffSetModifier;

            _nextWorkingWeight.setText(df2.format(nextWorkingSet));
            _nextBackoffSetWeight.setText(df2.format(nextBackoffSetWeight));
            _estimated1rm.setText(df2.format(estimated1rm));
        }

    }
}
