package com.orange.jigsaw.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.orange.jigsaw.R;

/**
 * Launch activity.
 * Created by Orange on 2015/4/9.
 */
public class LaunchActivity extends Activity {
    private Button selectGameButton;
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
    }
}
