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

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.OffersWallDialogListener;
import net.youmi.android.offers.PointsManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

/**
 * Launch activity.
 * Created by Orange on 2015/4/9.
 */
public class LaunchActivity extends Activity {
    private Button selectGameButton;
    private Button contactMeButton;
    private Button quiteGameButton;

    private Button wallAdButton;
    private Button insertAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Initialize Youmi Ad arguments
        // appId, appSecret, debug
        AdManager.getInstance(this).init("d4bbc82141ef6ec4", "30b788848f394db7", false);
        // Initialize Youmi Ad resources
        SpotManager.getInstance(this).loadSpotAds();
        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIM_ADVANCE);
        SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
        OffersManager.getInstance(this).onAppLaunch();

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

        // Add two Youmi Ad buttons
        wallAdButton = (Button)findViewById(R.id.wallAdButton);
        wallAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LaunchActivity.this, R.string.show_wall_ad, Toast.LENGTH_SHORT).show();
                OffersManager.getInstance(LaunchActivity.this).showOffersWallDialog(LaunchActivity.this,
                        new OffersWallDialogListener() {
                            @Override
                            public void onDialogClose() {
                                Toast.makeText(LaunchActivity.this, R.string.quit_wall_ad, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        insertAdButton = (Button)findViewById(R.id.insertAdButton);
        insertAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show Youmi Ad
                SpotManager.getInstance(LaunchActivity.this).showSpotAds(
                        LaunchActivity.this, new SpotDialogListener() {
                            @Override
                            public void onShowSuccess() {
                            }
                            @Override
                            public void onShowFailed() {
                            }
                            @Override
                            public void onSpotClosed() {
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        SpotManager.getInstance(this).onDestroy();
        OffersManager.getInstance(this).onAppExit();

        super.onDestroy();
    }
}
