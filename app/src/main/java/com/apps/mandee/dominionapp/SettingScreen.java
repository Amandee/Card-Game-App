package com.apps.mandee.dominionapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by Mandee on 1/28/2015.
 */
public class SettingScreen extends Activity {

    private String players;
    private Spinner dropdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"2", "3","4","5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingScreen.this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);


    }

    public void goToGame(View view) {
        players = dropdown.getSelectedItem().toString();
        Intent goToGame = new Intent(this, GameScreen.class).putExtra("players", players);
        startActivity(goToGame);
    }
}
