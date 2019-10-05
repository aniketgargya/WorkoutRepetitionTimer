package com.example.workoutrepetitiontimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public ArrayList<RepCycleModel> repCycles;

    private String dialogId = null;

    SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHelper = new SQLiteHelper(MainActivity.this, getString(R.string.rep_table_name), getString(R.string.rep_cycle_name_key), getString(R.string.rep_cycle_id_key), getString(R.string.rep_cycle_list_key), getString(R.string.rep_cycle_repetitions_key));

    }

    @Override
    protected void onResume() {
        super.onResume();

        new retrieveRepCycles().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.add_rep_cycle_item){
            Intent add = new Intent(MainActivity.this, RepCycleListActivity.class);
            startActivity(add);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        String tag;

        try {

            tag = view.getTag().toString();

            if(tag.substring(0, getString(R.string.rep_cycles_tag_prefix).length()).equals(getString(R.string.rep_cycles_tag_prefix))){

                String repCycleId = tag.substring(getString(R.string.rep_cycles_tag_prefix).length());
                String repCycleName;

                for(int i = 0; i < repCycles.size(); i++){

                    RepCycleModel repCycleModel = repCycles.get(i);

                    if(repCycleModel.getId().equals(repCycleId)){
                        dialogId = repCycleModel.getId();
                        repCycleName = repCycleModel.getName();

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(String.format(getString(R.string.delete_rep_cycle_message), repCycleName));
                        builder.setTitle(getString(R.string.delete_rep_cycle_title));
                        builder.setCancelable(true);

                        builder.setPositiveButton(getString(R.string.delete_rep_cycle_start_button), DialogInterfaceOnClickListener);
                        builder.setNegativeButton(getString(R.string.delete_rep_cycle_delete_button), DialogInterfaceOnClickListener);

                        builder.create().show();
                    }

                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    DialogInterface.OnClickListener DialogInterfaceOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {

            if(which == DialogInterface.BUTTON_POSITIVE){

                for(int i = 0; i < repCycles.size(); i++){

                    RepCycleModel repCycleModel = repCycles.get(i);

                    if(repCycleModel.getId() == dialogId){

                        Intent startTimer = new Intent(MainActivity.this, RepCycleTimerActivity.class);

                        startTimer.putExtra(getString(R.string.rep_cycle_name_key), repCycleModel.getName());
                        startTimer.putExtra(getString(R.string.rep_cycle_list_key), repCycleModel.getList());
                        startTimer.putExtra(getString(R.string.rep_cycle_repetitions_key), repCycleModel.getRepetitions());
                        startTimer.putExtra(getString(R.string.rep_cycle_id_key), repCycleModel.getId());

                        startActivity(startTimer);

                    }

                }

            }

            if(which == DialogInterface.BUTTON_NEGATIVE){

                RepCycleModel repCycleModel = new RepCycleModel(null, null, null, dialogId);
                sqLiteHelper.deleteRepCycle(repCycleModel);
                new retrieveRepCycles().execute();

            }

        }
    };

    public void setRepCycles(ArrayList<RepCycleModel> repCycleModels){
        repCycles = repCycleModels;
    }

    public void addRepCyclesToLinearLayout(){

        if(repCycles.size() > 0) {

            for (int i = 0; i < repCycles.size(); i++) {

                RepCycleModel repCycleModel = repCycles.get(i);

                LinearLayout repCyclesLinearLayout = findViewById(R.id.rep_cycles_ll);
                TextView repCycleTextView = (TextView) getLayoutInflater().inflate(R.layout.rep_cycle_main_textview_layout, null);
                repCycleTextView.setText(repCycleModel.getName());

                repCycleTextView.setTag(getString(R.string.rep_cycles_tag_prefix) + repCycleModel.getId());
                repCycleTextView.setOnClickListener(this);

                repCyclesLinearLayout.addView(repCycleTextView);

            }

        } else {

            LinearLayout repCyclesLinearLayout = findViewById(R.id.rep_cycles_ll);

            TextView emptyTextView = (TextView) getLayoutInflater().inflate(R.layout.empty_main_textview_layout, null);

            repCyclesLinearLayout.addView(emptyTextView);

        }
    }

    public class retrieveRepCycles extends AsyncTask<Void, Void, ArrayList<RepCycleModel>>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<RepCycleModel> doInBackground(Void... voids) {
            return sqLiteHelper.getRepCycles();
        }

        @Override
        protected void onPostExecute(ArrayList<RepCycleModel> repCycleModels) {

            ((LinearLayout) findViewById(R.id.rep_cycles_ll)).removeAllViews();
            setRepCycles(repCycleModels);
            addRepCyclesToLinearLayout();

        }

    }
}