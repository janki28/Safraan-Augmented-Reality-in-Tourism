package com.example.saad.hci;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Objects;

public class TrailDisplayActivity extends Activity implements AdapterView.OnItemClickListener {
    Class_MyApplication         app;
    Class_Trail                 trail;
    boolean                     displaySearch;
    Class_MyListAdapter         myListAdapter;
    ListView                    mainListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_display);
        displaySearch = false;

        // reference the MyApplication variable to access global variables.
        app = (Class_MyApplication) getApplication();

        // setup listview
        myListAdapter = new Class_MyListAdapter(this, getLayoutInflater());
        mainListView = findViewById(R.id.display_listview);
        mainListView.setAdapter(myListAdapter);
        mainListView.setOnItemClickListener(this);

        // check which Site to display
        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        trail = app.findTrail(name);

        // Search Bar
        setupSearch();

        // Title
        TextView textView = findViewById(R.id.main_title);
        textView.setText(name);

        // Data
        setupData();
    }

    public void setupData() {
        ArrayList<String> sites = trail.getSiteNames();
        ArrayList<Class_HeritageSite> heritageSites = new ArrayList<>();
        for (int i = sites.size()-1; i >= 0; i--) {
            Class_HeritageSite heritageSite = app.findHeritageSite(sites.get(i));
            heritageSites.add(heritageSite);
        }

        myListAdapter.updateData(heritageSites);
    }

    public void setupSearch() {
        AutoCompleteTextView searchbar = findViewById(R.id.main_search);
        String[] s = new String[app.all_HeritageSites.size()];

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, app.all_searchables.toArray(s));
        searchbar.setAdapter(adapter);
        searchbar.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(null);
                    return true;
                }
                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void performSearch(View v) {
        if (!displaySearch) {
            displaySearch = true;
            AutoCompleteTextView textView = findViewById(R.id.main_search);
            textView.setVisibility(View.VISIBLE);
            (findViewById(R.id.main_title)).setVisibility(View.INVISIBLE);
            if (textView.requestFocus()) {
                ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
            }
        } else {
            AutoCompleteTextView textView = findViewById(R.id.main_search);
            String text = textView.getText().toString();
            if (app.all_searchables.contains(text)) {
                Intent intent;
                if (app.isTag(text)) {
                    intent = new Intent(this, ListDisplayActivity.class);
                    intent.putExtra("MODE", "SEARCH");
                    intent.putExtra("NAME", text);
                    Toast.makeText(this, "Tag Found", Toast.LENGTH_SHORT).show();
                } else if (app.isTrail(text)) {
                    intent = new Intent(this, TrailDisplayActivity.class);
                    intent.putExtra("NAME", text);
                    Toast.makeText(this, "Trail Found", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, SiteDisplayActivity.class);
                    intent.putExtra("NAME", text);
                    Toast.makeText(this, "Site Found", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            } else {
                Toast.makeText(this, "Does Not Exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trail_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        hideSearchBar();
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void hideSearchBar() {
        displaySearch = false;
        AutoCompleteTextView textView = findViewById(R.id.main_search);
        ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        textView.setVisibility(View.GONE);
        textView.setText("");
        findViewById(R.id.main_title).setVisibility(View.VISIBLE);
    }

    public void performAugmented(View v) {
        Toast.makeText(this, "Augmented", Toast.LENGTH_SHORT).show();
        Intent i=new Intent(this,SampleCamActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class_HeritageSite heritageSite = (Class_HeritageSite) myListAdapter.getItem(position);
        Intent intent = new Intent(this, SiteDisplayActivity.class);
        intent.putExtra("NAME", heritageSite.getName());
        startActivity(intent);
    }
}
