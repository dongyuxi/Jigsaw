package com.orange.jigsaw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.orange.jigsaw.view.ImageLayout;
import com.orange.jigsaw.view.ImageLayoutListener;

public class MainActivity extends Activity {

    private ImageLayout imageLayout;
    private TextView levelTextView;
    private TextView stepTextView;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageLayout = (ImageLayout)findViewById(R.id.jigsawLayout);
        levelTextView = (TextView)findViewById(R.id.level);
        stepTextView = (TextView)findViewById(R.id.step);
        timeTextView = (TextView)findViewById(R.id.time);
        imageLayout.setImageListener(new ImageLayoutListener() {
            @Override
            public void nextLevel() {
                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.game_info).setMessage(R.string.level_up).setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imageLayout.nextLevel();
                        levelTextView.setText("" + imageLayout.getLevel());
                    }
                }).show();
            }

            @Override
            public void stepChange(int currentStep) {
                stepTextView.setText("" + currentStep);
            }

            @Override
            public void timeChange(int currentTime) {
                timeTextView.setText("" + currentTime);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}
