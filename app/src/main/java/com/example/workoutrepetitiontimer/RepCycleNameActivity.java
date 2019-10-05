package com.example.workoutrepetitiontimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RepCycleNameActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mRepCycleNameEditText;
    private TextView mSaveTextView;
    private TextView mDontSaveTextView;
    private int mOptionSelected = -1;
    private Menu menu;

    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_cycle_name);

        sqLiteHelper = new SQLiteHelper(RepCycleNameActivity.this, getString(R.string.rep_table_name), getString(R.string.rep_cycle_name_key), getString(R.string.rep_cycle_id_key), getString(R.string.rep_cycle_list_key), getString(R.string.rep_cycle_repetitions_key));

        mRepCycleNameEditText = findViewById(R.id.rep_cycle_name_et);
        mSaveTextView = findViewById(R.id.save_rep_cycle_tv);
        mDontSaveTextView = findViewById(R.id.dont_save_rep_cycle_tv);

        mSaveTextView.setOnClickListener(this);
        mDontSaveTextView.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rep_cycle_name_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.start_button) {

            Bundle bundle = getIntent().getExtras();
            String repCycleListData = null;
            String repCycleRepetitionsData = null;

            if (bundle != null) {
                repCycleListData = bundle.getString(getString(R.string.rep_cycle_list_key));
                repCycleRepetitionsData = bundle.getString(getString(R.string.rep_cycle_repetitions_key));
            }

            if(mOptionSelected == -1){

                new Toast(this).makeText(this, getString(R.string.no_selected_save_rep_cycle_radio_group), Toast.LENGTH_SHORT).show();

            }

            else if(mOptionSelected == 0){

                String repCycleNameData = getString(R.string.default_rep_cycle_name);

                RepCycleModel repCycleModel = new RepCycleModel(repCycleNameData, repCycleListData, repCycleRepetitionsData, null);

                Intent startTimer = new Intent(RepCycleNameActivity.this, RepCycleTimerActivity.class);

                startTimer.putExtra(getString(R.string.rep_cycle_name_key), repCycleModel.getName());
                startTimer.putExtra(getString(R.string.rep_cycle_list_key), repCycleModel.getList());
                startTimer.putExtra(getString(R.string.rep_cycle_repetitions_key), repCycleModel.getRepetitions());
                startTimer.putExtra(getString(R.string.rep_cycle_id_key), repCycleModel.getId());

                startActivity(startTimer);

            }

            else if (mOptionSelected == 1){

                String repCycleName = mRepCycleNameEditText.getText().toString();

                if (!repCycleName.isEmpty()) {

                    String repCycleNameData = repCycleName;

                    RepCycleModel repCycleModel = new RepCycleModel(repCycleNameData, repCycleListData, repCycleRepetitionsData, null);

                    sqLiteHelper.insertRepCycle(repCycleModel);

                    new Toast(this).makeText(this, "Saving", Toast.LENGTH_SHORT).show();

                    Intent homeScreen = new Intent(RepCycleNameActivity.this, MainActivity.class);
                    startActivity(homeScreen);

                } else {

                    new Toast(this).makeText(this, getString(R.string.empty_name_message), Toast.LENGTH_SHORT).show();

                }

            }

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.save_rep_cycle_tv){
            mSaveTextView.setTextColor(getColor(R.color.black));
            mDontSaveTextView.setTextColor(getColor(R.color.gray));
            mOptionSelected = 1;
            MenuItem item = menu.findItem(R.id.start_button);
            item.setTitle("Save");
        }

        if(id == R.id.dont_save_rep_cycle_tv){
            mDontSaveTextView.setTextColor(getColor(R.color.black));
            mSaveTextView.setTextColor(getColor(R.color.gray));
            mOptionSelected = 0;
            MenuItem item = menu.findItem(R.id.start_button);
            item.setTitle("Start");
        }
    }

}
