package com.orange.jigsaw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.orange.jigsaw.R;
import com.orange.jigsaw.utils.HeroSelectionBackgroundImages;
import com.orange.jigsaw.utils.PieceDifficultyUtils;
import com.orange.jigsaw.view.HeroLayout;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;

/**
 * Hero and difficulty select activity.
 * Created by Orange on 2015/4/9.
 */
public class SelectionActivity extends Activity {
    private HeroLayout heroLayout;
    private Switch heroBackgroundImageStyleSwitch;
    private SeekBar difficultySeekBar;
    private TextView pieceTextView;
    private TextView difficultyTextView;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        heroLayout = (HeroLayout)findViewById(R.id.jigsawLevelLayout);
        heroBackgroundImageStyleSwitch = (Switch)findViewById(R.id.levelImageStyleSwitch);
        heroBackgroundImageStyleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if (on) {
                    heroLayout.setLevelBackgroundImagesStyle(HeroSelectionBackgroundImages.Style.CUTE);
                } else {
                    heroLayout.setLevelBackgroundImagesStyle(HeroSelectionBackgroundImages.Style.CLASSIC);
                }
            }
        });

        difficultySeekBar = (SeekBar)findViewById(R.id.difficultySeekBar);
        pieceTextView = (TextView) findViewById(R.id.pieceTextView);
        difficultyTextView = (TextView) findViewById(R.id.difficultyTextView);
        difficultySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int piece = progress + 3;
                pieceTextView.setText(piece + " * " + piece);
                difficultyTextView.setText(PieceDifficultyUtils.getDifficultyStringResourceId(piece));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startGameButton = (Button) findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heroLayout.startGame(difficultySeekBar.getProgress() + 3);
            }
        });
        showBanner();
    }

    /**
     * Show Youmi Banner Ad.
     */
    private void showBanner() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
        adView.setAdListener(new AdViewListener() {
            @Override
            public void onSwitchedAd(AdView arg0) {
            }
            @Override
            public void onReceivedAd(AdView arg0) {
            }
            @Override
            public void onFailedToReceivedAd(AdView arg0) {
            }
        });
        this.addContentView(adView, layoutParams);
    }

}
