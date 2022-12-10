package com.example.sql_na_app;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEmployeeDialog extends DialogFragment {

    private Calendar myCalendar = Calendar.getInstance();
    private EditText etvDesignation;
    private EditText etvName;
    private EditText etvDOB;
    private Button btnSave;
    private Button btnCancel;
    private cs333fa22.hfad.demosqlite.DBHelper dbHelper;
    private cs333fa22.hfad.demosqlite.EmployeeListAdapter employeeListAdapter;

    public AddEmployeeDialog(cs333fa22.hfad.demosqlite.EmployeeListAdapter employeeListAdapter)
    {
        //dbHelper = new DBHelper();
        employeeListAdapter = employeeListAdapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =
                getActivity().getLayoutInflater();
        View dialogView =
                inflater.inflate(R.layout.activity_add, null);

        etvDesignation = dialogView.findViewById(R.id.etvDesignation);
        etvName = dialogView.findViewById(R.id.etvEmpName);
        etvDOB = dialogView.findViewById(R.id.etvDOB);
        btnSave = dialogView.findViewById(R.id.btnSave);
        btnCancel = dialogView.findViewById(R.id.btnCancel);

        // on clicking ok on the calender dialog

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();  //if they cancel, just finish activity
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();

            }
        });

        DatePickerDialog.OnDateSetListener  date =  new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etvDOB.setText(getFormattedDate(myCalendar.getTimeInMillis()));

            }

        };

        Activity activity = getActivity();
        etvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(activity,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        dbHelper = new cs333fa22.hfad.demosqlite.DBHelper(getContext());

        builder.setView(dialogView).setMessage("Add New Employee");
        return builder.create();
    }



    private void saveEmployee()
    {
        String toastString = null;
        String name = etvName.getText().toString();
        String desig = etvDesignation.getText().toString();
        long calInMS = myCalendar.getTimeInMillis();

        boolean isValid = (!name.equals("") && !desig.equals(""));

        if (!isValid)  //save a new employee
        {
            toastString = "Fill out all fields.";
        }
        else
        {
            ArrayList<Employee> emps = new ArrayList<Employee>();

            dbHelper.saveEmployee(name, desig, calInMS);
            //refresh UI

            empListAdapter.setEmployees(allEmps);
            empListAdapter.notifyDataSetChanged();
            empListAdapter.notifyItemRangeChanged(0, allEmps.size());


            // false code to prevent crash
            //employeeListAdapter.setEmployees(emps);
            //employeeListAdapter.notifyDataSetChanged();
            //employeeListAdapter.notifyItemRangeChanged(0, emps.size());


            //empListAdapter.setEmployees(allEmps);

            toastString = "Employee Added!";
        }

        Toast t = Toast.makeText(getContext(), toastString, Toast.LENGTH_SHORT);
        t.show();
    }



    private  String  getFormattedDate(long dobInMilis){

        SimpleDateFormat formatter = new  SimpleDateFormat("dd/MMM/yyyy",
                Locale.getDefault());

        Date dobDate =   new Date(dobInMilis);

        String s = formatter.format(dobDate);

        return s;
    }
}


