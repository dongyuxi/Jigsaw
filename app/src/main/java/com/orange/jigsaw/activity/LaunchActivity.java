package com.orange.jigsaw.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orange.jigsaw.R;

/**
 * Launch activity.
 * Created by Orange on 2015/4/9.
 */
public class LaunchActivity extends Activity {
    private Button selectGameButton;
    private Button contactMeButton;
    private Button quiteGameButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        selectGameButton = (Button)findViewById(R.id.selectGameButton);
        selectGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LaunchActivity.this, SelectionActivity.class);
                startActivity(intent);
            }
        });
        contactMeButton = (Button)findViewById(R.id.contactMeButton);
        contactMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(LaunchActivity.this)
                        .setTitle(R.string.game_info)
                        .setMessage(R.string.contact_me_info)
                        .setCancelable(false)
                        .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });
        quiteGameButton = (Button)findViewById(R.id.quitGameButton);
        quiteGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
