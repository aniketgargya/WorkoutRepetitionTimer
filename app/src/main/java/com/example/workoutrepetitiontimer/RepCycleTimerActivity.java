package com.example.workoutrepetitiontimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RepCycleTimerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTimerDisplayTextView;
    private TextView mNameDisplayTextView;
    private TextView mRepetitionDisplayTextView;
    private LinearLayout mCycleDisplayLinearLayout;
    private HorizontalScrollView mCycleDisplayHorizontalScrollView;

    RepCycleModel repCycleModel;
    public ArrayList<Integer> repCycleList;
    public int currentRepCyclePart = 0;
    public int repCycleRepetitions;
    public int currentRepCycleIteration = 1;

    public int width;
    public int height;

    public GenericTimer genericTimer;
    public boolean done = false;
    public int timeLeft = 0;
    public boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_cycle_timer);

        repCycleModel = new RepCycleModel(getIntent().getStringExtra(getString(R.string.rep_cycle_name_key)), getIntent().getStringExtra(getString(R.string.rep_cycle_list_key)), getIntent().getStringExtra(getString(R.string.rep_cycle_repetitions_key)), getIntent().getStringExtra(getString(R.string.rep_cycle_id_key)));
        repCycleList = repCycleModel.getRepCycleList();
        repCycleRepetitions = Integer.parseInt(repCycleModel.getRepetitions());

        mTimerDisplayTextView = findViewById(R.id.timer_display_tv);
        mNameDisplayTextView = findViewById(R.id.name_display_tv);
        mRepetitionDisplayTextView = findViewById(R.id.repetition_display_tv);
        mCycleDisplayLinearLayout = findViewById(R.id.cycle_display_ll);
        mCycleDisplayHorizontalScrollView = findViewById(R.id.cycle_display_hsv);

        mTimerDisplayTextView.setOnClickListener(this);

        mNameDisplayTextView.setText(repCycleModel.getName());
        String fraction = "Round: " + currentRepCycleIteration + " / " + repCycleModel.getRepetitions();
        mRepetitionDisplayTextView.setText(fraction);

        setScreenDimensions();

        int textViewWidth = repCycleList.size() > 3 ?  3 : repCycleList.size();

        for(int i = 0; i < repCycleList.size(); i++){

            TextView repCycleItemTextView = (TextView) getLayoutInflater().inflate(R.layout.rep_cycle_timer_textview_layout, null);
            repCycleItemTextView.setText(new TimeUtils().secondsToDisplay(repCycleList.get(i)));
            repCycleItemTextView.setWidth(width / textViewWidth);
            mCycleDisplayLinearLayout.addView(repCycleItemTextView);

        }

        ((TextView) mCycleDisplayLinearLayout.getChildAt(currentRepCyclePart)).setTextColor(getResources().getColor(R.color.colorPrimary));
        genericTimer = new GenericTimer(repCycleList.get(currentRepCyclePart) * 1000);
    }

    public void setScreenDimensions(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.timer_display_tv){
            if(paused){
                resume();
            }
            else {
                pause();
            }
        }
    }

    class GenericTimer extends CountDownTimer {

        public GenericTimer(int seconds) {

            super(seconds, 1000);
            this.start();

        }

        @Override
        public void onTick(long millisUntilFinished) {

            String display = new TimeUtils().secondsToDisplay((int) (millisUntilFinished / (double) 1000));
            mTimerDisplayTextView.setText(display);
            timeLeft = (int) millisUntilFinished;

        }

        @Override
        public void onFinish() {

            timeLeft = 0;

            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(500);

            for(int i = 0; i < mCycleDisplayLinearLayout.getChildCount(); i++){
                ((TextView) mCycleDisplayLinearLayout.getChildAt(i)).setTextColor(getResources().getColor(R.color.black));
            }

            currentRepCyclePart += 1;
            if(currentRepCyclePart == repCycleList.size()){

                currentRepCycleIteration += 1;

                if(currentRepCycleIteration > repCycleRepetitions) {

                    mTimerDisplayTextView.setText("Done");
                    done = true;

                } else {

                    currentRepCyclePart = 0;
                    String fraction = "Round: " + currentRepCycleIteration + " / " + repCycleModel.getRepetitions();
                    mRepetitionDisplayTextView.setText(fraction);

                    genericTimer = new GenericTimer(repCycleList.get(currentRepCyclePart) * 1000);

                    ((TextView) mCycleDisplayLinearLayout.getChildAt(currentRepCyclePart)).setTextColor(getResources().getColor(R.color.colorPrimary));
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            int vLeft = mCycleDisplayLinearLayout.getChildAt(currentRepCyclePart).getLeft();
                            int vRight = mCycleDisplayLinearLayout.getChildAt(currentRepCyclePart).getRight();
                            int sWidth = mCycleDisplayHorizontalScrollView.getWidth();
                            mCycleDisplayHorizontalScrollView.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
                        }
                    });

                }


            } else {

                genericTimer = new GenericTimer(repCycleList.get(currentRepCyclePart) * 1000);

                ((TextView) mCycleDisplayLinearLayout.getChildAt(currentRepCyclePart)).setTextColor(getResources().getColor(R.color.colorPrimary));
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        int vLeft = mCycleDisplayLinearLayout.getChildAt(currentRepCyclePart).getLeft();
                        int vRight = mCycleDisplayLinearLayout.getChildAt(currentRepCyclePart).getRight();
                        int sWidth = mCycleDisplayHorizontalScrollView.getWidth();
                        mCycleDisplayHorizontalScrollView.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
                    }
                });

            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("Size", mNameDisplayTextView.getTextSize() +"");
        float px = mNameDisplayTextView.getTextSize();
        float sp = px / getResources().getDisplayMetrics().scaledDensity;
        Log.d("Size", sp+"");

        if(done){
            Intent intent = new Intent(RepCycleTimerActivity.this, MainActivity.class);
            startActivity(intent);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.exit_rep_cycle_timer_message));
        builder.setTitle(getString(R.string.exit_rep_cycle_timer_title));
        builder.setCancelable(true);

        builder.setPositiveButton(getString(R.string.exit_rep_cycle_timer_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

            }
        });

        builder.setNegativeButton(getString(R.string.exit_rep_cycle_timer_exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if(genericTimer != null){
                    genericTimer.cancel();
                }

                Intent intent = new Intent(RepCycleTimerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.create().show();

    }

    public void pause(){
        Animation blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        mTimerDisplayTextView.startAnimation(blink);
        paused = true;
        genericTimer.cancel();
    }

    public void resume(){
        mTimerDisplayTextView.clearAnimation();
        paused = false;
        genericTimer = new GenericTimer(timeLeft);
    }
}
