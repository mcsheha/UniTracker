package com.mikeshehadeh.unitracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class TermListActivity extends AppCompatActivity {
    private ArrayList<TermItem> mTermList;
    private RecyclerView mRecyclerView;
    private TermListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        createTermList();
        buildRecyclerView();

        buttonInsert = findViewById(R.id.button_insert);
        buttonRemove = findViewById(R.id.button_remove);
        editTextInsert = findViewById(R.id.edittext_insert);
        editTextRemove = findViewById(R.id.edittext_remove);

        buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });
    }

    public void insertItem (int position) {
        mTermList.add(position, new TermItem(R.drawable.ic_android, "New Item at Position " + position, "This is Line 2"));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem (int position) {
        mTermList.remove(position);
        mAdapter.notifyItemRemoved(position);

    }

    public void changeItem (int position, String text) {
        mTermList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);

    }

    public void createTermList(){
        mTermList = new ArrayList<>();
        mTermList.add(new TermItem(R.drawable.ic_android, "Line 1", "Line 2"));
        mTermList.add(new TermItem(R.drawable.ic_audio, "Line 3", "Line 4"));
        mTermList.add(new TermItem(R.drawable.ic_sun, "Line 5", "Line 6"));

    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recycler_view_term_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new TermListAdapter(mTermList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TermListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Put code here for what happens when an item is clicked
                changeItem(position, "Clicked");
            }
        });

    }
}
