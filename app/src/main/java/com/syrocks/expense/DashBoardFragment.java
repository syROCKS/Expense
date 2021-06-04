package com.syrocks.expense;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.syrocks.expense.Model.Data;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

public class DashBoardFragment extends Fragment {

    private FloatingActionButton fab_main;
    private FloatingActionButton fab_income;
    private FloatingActionButton fab_expense;
    private TextView fab_income_txt;
    private TextView fab_expense_txt;
    private boolean isOpen = false;
    private Animation FadeOpen, FadeClose;
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dash_board, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        fab_main = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income = myView.findViewById(R.id.income_ft_btn);
        fab_expense = myView.findViewById(R.id.expense_ft_btn);
        fab_expense_txt = myView.findViewById(R.id.expense_ft_text);
        fab_income_txt = myView.findViewById(R.id.income_ft_text);

        FadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main.setOnClickListener(v -> {

            addData();
            ft_animation();

        });
        return myView;
    }

    private void ft_animation() {
        if (isOpen) {

            fab_income.setAnimation(FadeClose);
            fab_expense.setAnimation(FadeClose);
            fab_expense.setClickable(false);
            fab_income.setClickable(false);

            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen = false;

        } else {

            fab_income.setAnimation(FadeOpen);
            fab_expense.setAnimation(FadeOpen);
            fab_expense.setClickable(true);
            fab_income.setClickable(true);

            fab_income_txt.startAnimation(FadeOpen);
            fab_expense_txt.startAnimation(FadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen = true;

        }
    }

    private void addData() {

        fab_income.setOnClickListener(v -> {

            incomeDataInsert();

        });

        fab_expense.setOnClickListener(v -> {


        });

    }

    public void incomeDataInsert() {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myView);
        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        EditText editAmount = myView.findViewById(R.id.amount_edt);
        EditText editType = myView.findViewById(R.id.type_edt);
        EditText editNote = myView.findViewById(R.id.note_edt);

        Button btnSave = myView.findViewById(R.id.btnSave);
        Button btnCancel = myView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {

            String type = editType.getText().toString().trim();
            String amount = editAmount.getText().toString().trim();
            String note = editNote.getText().toString().trim();

            if (TextUtils.isEmpty(type)) {
                editType.setError("Required Field..");
                return;
            }
            if (TextUtils.isEmpty(amount)) {
                editAmount.setError("Required Field..");
                return;
            }
            if (TextUtils.isEmpty(note)) {
                editNote.setError("Required Field..");
                return;
            }

            int ourAmount = Integer.parseInt(amount);

            String id = mIncomeDatabase.push().getKey();
            String mDate = DateFormat.getDateInstance().format(new Date());
            Data data = new Data(ourAmount, type, note, id, mDate);
            mIncomeDatabase.child(Objects.requireNonNull(id)).setValue(data);
            Toast.makeText(getActivity(), "Data ADDED", Toast.LENGTH_SHORT).show();
            ft_animation();
            dialog.dismiss();

        });

        btnCancel.setOnClickListener(v -> {

            dialog.dismiss();

        });

        dialog.show();

    }

}