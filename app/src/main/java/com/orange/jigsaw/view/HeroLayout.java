package com.orange.jigsaw.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.orange.jigsaw.MainActivity;
import com.orange.jigsaw.utils.HeroSelectionBackgroundImages;

/**
 * HeroLayout to show hero to be selected
 * Created by Orange on 2015/4/11.
 */
public class HeroLayout extends RelativeLayout {
    /** Image has been split into piece * piece. */
    private int piece = 3;

    /** 3dp to make sure it is the same on all devices. */
    private static final int MARGIN = 3;
    private int margin;
    /** Padding. */
    private int padding;

    /** Hero ImageView list. */
    private ImageView levelImageViews[];
    /** Length of layout. */
    private int length;
    /** Length for each ImageView. */
    private int itemLength;

    /** Hero background image type style. */
    private HeroSelectionBackgroundImages.Style levelBackgroundImagesStyle = HeroSelectionBackgroundImages.Style.CUTE;

    public HeroLayout(Context context) {
        this(context, null);
    }

    public HeroLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeroLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN, getResources().getDisplayMetrics());
        padding = Math.min(Math.min(getPaddingBottom(), getPaddingTop()), Math.min(getPaddingLeft(), getPaddingRight()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        length = Math.min(getMeasuredWidth(), getMeasuredHeight());
        itemLength = (length - 2 * padding - (piece - 1) * margin) / piece;

        initLevelImages();

        setMeasuredDimension(length, length);
    }

    /**
     * Initialize level background images.
     */
    private void initLevelImages() {
        levelImageViews = new ImageView[piece * piece];
        int[] levelBackgroundImagesIds = HeroSelectionBackgroundImages.getLevelBackgroundImages(this.levelBackgroundImagesStyle);
        for (int i = 0; i < levelImageViews.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), MainActivity.class);
                    getContext().startActivity(intent);
                }
            });
            imageView.setBackgroundResource(levelBackgroundImagesIds[i]);
            imageView.setId(i + 1);
            levelImageViews[i] = imageView;

            LayoutParams layoutParams = new LayoutParams(itemLength, itemLength);
            if (0 != i % piece) {
                layoutParams.leftMargin = margin;
                layoutParams.addRule(RelativeLayout.RIGHT_OF, levelImageViews[i - 1].getId());
            }

            if (i + 1 > piece) {
                layoutParams.topMargin = margin;
                layoutParams.addRule(RelativeLayout.BELOW, levelImageViews[i - piece].getId());
            }

            addView(imageView, layoutParams);
        }
    }

    /**
     * Set level background image style.
     * @param levelBackgroundImagesStyle
     */
    public void setLevelBackgroundImagesStyle(HeroSelectionBackgroundImages.Style levelBackgroundImagesStyle) {
        this.removeAllViews();
        if (this.levelBackgroundImagesStyle != levelBackgroundImagesStyle) {
            this.levelBackgroundImagesStyle = levelBackgroundImagesStyle;
            initLevelImages();
        }
    }
}
