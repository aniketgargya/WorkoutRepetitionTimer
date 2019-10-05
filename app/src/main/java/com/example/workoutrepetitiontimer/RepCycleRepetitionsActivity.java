package com.example.workoutrepetitiontimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RepCycleRepetitionsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mRepCycleRepetitionsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_cycle_repetitions);

        mRepCycleRepetitionsEditText = findViewById(R.id.rep_cycle_repetitions_et);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rep_cycle_repetitions_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.next_button) {

            String repNumberContents = mRepCycleRepetitionsEditText.getText().toString();
            int repCycleRepetitions;

            try {
                repCycleRepetitions = Integer.parseInt(repNumberContents);
                //TODO: Delete debugging toast
                new Toast(this).makeText(this, repCycleRepetitions + " rep cycles", Toast.LENGTH_SHORT).show();

                //TODO: Extra ifs?
                Bundle bundle = getIntent().getExtras();
                String repCycleListData = null;

                if (bundle != null) {
                    repCycleListData = bundle.getString(getString(R.string.rep_cycle_list_key));
                }

                Intent next = new Intent(RepCycleRepetitionsActivity.this, RepCycleNameActivity.class);

                next.putExtra(getString(R.string.rep_cycle_list_key), repCycleListData);
                next.putExtra(getString(R.string.rep_cycle_repetitions_key), Integer.toString(repCycleRepetitions));

                startActivity(next);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                new Toast(this).makeText(this, "Enter a number, you moron", Toast.LENGTH_SHORT).show();
            }

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

    }
}