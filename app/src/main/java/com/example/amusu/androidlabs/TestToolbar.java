package com.example.amusu.androidlabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class TestToolbar extends AppCompatActivity {
    private String currentMessage = null;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar lab8_toolbar = findViewById(R.id.lab8_toolbar);
        setSupportActionBar(  lab8_toolbar) ;


        final Button toolbarButton = findViewById(R.id.toolbar_button);
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.toolbar_button),"Message to show",Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi){
        builder = new AlertDialog.Builder(TestToolbar.this);
        switch(mi.getItemId()) {
            case R.id.action_one:
                Log.d("Toolbar", "add option is selected");
                if(currentMessage==null) {
                    currentMessage = "You selected item 1";

                }
                Snackbar.make(findViewById(R.id.toolbar_button), currentMessage, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", "delete option is selected");
                builder.setTitle(R.string.dialog_title);
// Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "ByeBye");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                dialog = builder.create();
                dialog.show();


                break;
            case R.id.action_three:
                Log.d("Toolbar", "refresh option is selected");
                @SuppressLint("InflateParams") final View result = getLayoutInflater().inflate(R.layout.new_message,null);
                builder.setView(result);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText newMessage = (EditText)result.findViewById(R.id.new_message_content);
                        currentMessage = newMessage.getText().toString();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.about:
                Log.d("Toolbar", "about option is selected");
                Toast t = Toast.makeText(TestToolbar.this,"Version 1.0 made by: Roger Li", Toast.LENGTH_LONG);
                t.show();
                break;
            default:
                break;
        }
        return true;
    }
}
