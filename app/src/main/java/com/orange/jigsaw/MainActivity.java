package com.orange.jigsaw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orange.jigsaw.utils.PieceDifficultyUtils;
import com.orange.jigsaw.view.ImageLayout;
import com.orange.jigsaw.view.ImageLayoutListener;

/**
 * Main game activity.
 */
public class MainActivity extends Activity {

    private ImageLayout imageLayout;
    private TextView levelTextView;
    private TextView stepTextView;
    private TextView timeTextView;
    private Button pauseResumeButton;
    private boolean pause = true;
    private Button restartButton;
    private Button resetButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageLayout = (ImageLayout)findViewById(R.id.jigsawLayout);
        levelTextView = (TextView)findViewById(R.id.level);
        stepTextView = (TextView)findViewById(R.id.step);
        timeTextView = (TextView)findViewById(R.id.time);

        int selectedIndex = getIntent().getIntExtra(getResources().getResourceName(R.string.selected_hero), 0);
        int selectedPiece = getIntent().getIntExtra(getResources().getResourceName(R.string.selected_piece), 3);
        imageLayout.setSelectedIndex(selectedIndex);
        imageLayout.setPiece(selectedPiece);
        levelTextView.setText(PieceDifficultyUtils.getDifficultyStringResourceId(selectedPiece));
        imageLayout.setImageListener(new ImageLayoutListener() {
            @Override
            public void nextLevel() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.game_info)
                        .setMessage(R.string.level_up)
                        .setCancelable(false)
                        .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                imageLayout.nextLevel();
                                levelTextView.setText(PieceDifficultyUtils.getDifficultyStringResourceId(imageLayout.getLevel() + 2));
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

            @Override
            public void gameOver() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.game_info)
                        .setMessage(R.string.game_over)
                        .setCancelable(false)
                        .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                imageLayout.restart();
                            }
                        }).setNeutralButton(R.string.reset, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                imageLayout.reset();
                            }
                        }).setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
            }
        });

        pauseResumeButton = (Button)findViewById(R.id.pauseResumeButton);
        pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pause) {
                    pause = false;
                    imageLayout.pause();
                    pauseResumeButton.setText(R.string.resume);
                } else {
                    pause = true;
                    imageLayout.resume();
                    pauseResumeButton.setText(R.string.pause);
                }
            }
        });
        restartButton = (Button)findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.restart();
            }
        });
        resetButton = (Button)findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageLayout.reset();
            }
        });
        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageLayout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageLayout.resume();
    }

}
