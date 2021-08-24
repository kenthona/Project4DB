package com.vsga.project4db;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.vsga.project4db.databinding.ActivityMainBinding;
import com.vsga.project4db.helper.DbHelper;
import com.vsga.project4db.model.Data;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    AlertDialog.Builder dialog;
    List<Data> itemList = new ArrayList<Data>();
    Adapter adapter;
    DbHelper SQLite = new DbHelper(this);

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_ADDRESS = "address";
    private Object Adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SQLite = new DbHelper(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);

        listView = (ListView) findViewById(R.id.list_view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEdit.class);
                startActivity(intent);
            }
        });
        listView.setAdapter((ListAdapter) adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                final String idx = itemList.get(position).getId();
                final String name = itemList.get(position).getName();
                final String address = itemList.get(position).getAddress();

                final CharSequence[] dialogitem = {"Edit", "Delete"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(MainActivity.this, AddEdit.class);
                            intent.putExtra(TAG_ID, idx);
                            intent.putExtra(TAG_NAME, name);
                            intent.putExtra(TAG_ADDRESS, address);
                            startActivity(intent);
                            break;
                        case 1:
                            SQLite.delete(Integer.parseInt(idx));
                            itemList.clear();
                            getAllData();
                            break;
                    }
                    }
                }).show();
                return false;
            }
        });
        getAllData();
    }

    public void setSupportActionBar(Toolbar toolbar) {
    }

    private void getAllData() {
        ArrayList<HashMap<String, String>> row = SQLite.getAllData();

        for (int i = 0; i < row.size(); i++) {
            String id = row.get(i) .get(TAG_ID);
            String poster = row.get(i) .get(TAG_NAME);
            String title = row.get(i) .get(TAG_ADDRESS);

            Data data = new Data();

            data.setId(id);
            data.setName(poster);
            data.setAddress(title);

            itemList.add(data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        getAllData();
    }
}