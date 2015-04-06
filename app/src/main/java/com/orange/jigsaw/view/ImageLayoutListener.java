package com.orange.jigsaw.view;

/**
 * Callback listener interface for ImageLayout.
 * Created by Orange on 2015/4/5.
 */
public interface ImageLayoutListener {
    void nextLevel();
    void stepChange(int currentStep);
    void timeChange(int currentTime);
    void gameOver();
}
