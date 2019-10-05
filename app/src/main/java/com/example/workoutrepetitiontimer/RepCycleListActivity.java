package com.example.workoutrepetitiontimer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RepCycleListActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mAddRepCycleListItemEditText;
    private Button mAddButton;
    private LinearLayout mRepCycleListLinearLayout;
    private ScrollView mRepCycleListScrollView;

    private ArrayList<String> mRepCycleList = new ArrayList<>();
    private String dialogId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_cycle_list);

        mAddRepCycleListItemEditText = findViewById(R.id.add_rep_cycle_list_item_et);
        mAddButton = findViewById(R.id.add_rep_cycle_list_item_button);
        mRepCycleListLinearLayout = findViewById(R.id.rep_cycle_list_ll);
        mRepCycleListScrollView = findViewById(R.id.rep_cycle_list_sv);

        mAddButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String tag;

        try {

            tag = view.getTag().toString();

            if(tag.substring(0, getString(R.string.rep_cycles_item_tag_prefix).length()).equals(getString(R.string.rep_cycles_item_tag_prefix))){

                String repCycleItemId = tag.substring(getString(R.string.rep_cycles_item_tag_prefix).length());
                Toast.makeText(this, repCycleItemId, Toast.LENGTH_SHORT).show();
                int repCycleItemLength = Integer.parseInt(mRepCycleList.get(Integer.parseInt(repCycleItemId)));
                String repCycleItemDisplay = new TimeUtils().secondsToDisplay(repCycleItemLength);

                dialogId = repCycleItemId;

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(String.format(getString(R.string.edit_rep_cycle_item_message), repCycleItemDisplay));
                builder.setTitle(String.format(getString(R.string.edit_rep_cycle_item_title), repCycleItemDisplay));
                builder.setCancelable(true);

                builder.setPositiveButton(getString(R.string.edit_rep_cycle_item_change_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        String repCycleItemDisplay = new TimeUtils().secondsToDisplay(Integer.parseInt(mRepCycleList.get(Integer.parseInt(dialogId))));

                        final EditText changeEditText = (EditText) getLayoutInflater().inflate(R.layout.rep_cycle_list_dialoginterface_edittext_layout, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(RepCycleListActivity.this);
                        builder.setMessage(String.format(getString(R.string.change_rep_cycle_item_message), repCycleItemDisplay));
                        builder.setTitle(String.format(getString(R.string.change_rep_cycle_item_title), repCycleItemDisplay));
                        builder.setView(changeEditText);
                        builder.setCancelable(true);

                        builder.setPositiveButton(getString(R.string.change_rep_cycle_item_confirm_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                                Toast.makeText(RepCycleListActivity.this, changeEditText.getText(), Toast.LENGTH_SHORT).show();

                                try {
                                    mRepCycleList.set(Integer.parseInt(dialogId), Integer.toString(Integer.parseInt(changeEditText.getText().toString())));

                                    TextView repCycleListTextView = (TextView) mRepCycleListLinearLayout.getChildAt(Integer.parseInt(dialogId));
                                    String newDisplay = new TimeUtils().secondsToDisplay(Integer.parseInt(mRepCycleList.get(Integer.parseInt(dialogId))));
                                    repCycleListTextView.setText(newDisplay);

                                } catch (NumberFormatException e){

                                    Toast.makeText(RepCycleListActivity.this, getApplicationContext().getString(R.string.empty_add_list_item_edit_text_message), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();


                                }

                            }
                        });

                        builder.setNegativeButton(getString(R.string.change_rep_cycle_item_cancel_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        });

                        builder.create().show();
                    }
                });

                builder.setNegativeButton(getString(R.string.edit_rep_cycle_item_delete_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mRepCycleList.remove(Integer.parseInt(dialogId));
                        mRepCycleListLinearLayout.removeView(mRepCycleListLinearLayout.findViewWithTag(getString(R.string.rep_cycles_item_tag_prefix) + Integer.parseInt(dialogId)));

                        for(int i = 0; i < mRepCycleListLinearLayout.getChildCount(); i++){

                            View repCycleListTextView = mRepCycleListLinearLayout.getChildAt(i);
                            repCycleListTextView.setTag(getString(R.string.rep_cycles_item_tag_prefix) + i);

                        }
                    }
                });

                builder.create().show();

            }

        } catch (Exception e){
            e.printStackTrace();
        }

        if (id == R.id.add_rep_cycle_list_item_button) {

            int repCycleListItem;
            try {

                repCycleListItem = Integer.parseInt(mAddRepCycleListItemEditText.getText().toString());
                addRep(repCycleListItem);
                mAddRepCycleListItemEditText.setText("", TextView.BufferType.EDITABLE);

            } catch (NumberFormatException e) {

                e.printStackTrace();

                Toast.makeText(this, getString(R.string.empty_add_list_item_edit_text_message), Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rep_cycle_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.next_button) {

            if (mRepCycleList.isEmpty()) {

                Toast.makeText(this, R.string.empty_list_message, Toast.LENGTH_SHORT).show();

            } else {
                String repCycleListString = mRepCycleList.stream().collect(Collectors.joining("-"));

                //TODO: Delete toast, only there for debugging purposes
                Toast.makeText(this, repCycleListString, Toast.LENGTH_SHORT).show();

                Intent next = new Intent(RepCycleListActivity.this, RepCycleRepetitionsActivity.class);
                next.putExtra(getString(R.string.rep_cycle_list_key), repCycleListString);
                startActivity(next);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void addRep(int repLength){

        TextView repCycleListItemTextView = (TextView) getLayoutInflater().inflate(R.layout.rep_cycle_list_textview_layout, null);
        repCycleListItemTextView.setText(new TimeUtils().secondsToDisplay(repLength));
        repCycleListItemTextView.setTag(getString(R.string.rep_cycles_item_tag_prefix) + mRepCycleList.size());
        repCycleListItemTextView.setOnClickListener(this);
        mRepCycleListLinearLayout.addView(repCycleListItemTextView);

        mRepCycleListScrollView.post(new Runnable() {
            public void run() {
                mRepCycleListScrollView.fullScroll(mRepCycleListScrollView.FOCUS_DOWN);
            }
        });

        mRepCycleList.add(Integer.toString(repLength));
    }

}
