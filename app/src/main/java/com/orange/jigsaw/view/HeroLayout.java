package com.orange.jigsaw.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
    private ImageView heroImageViews[];
    /** Length of layout. */
    private int length;
    /** Length for each ImageView. */
    private int itemLength;

    /** Hero Index. */
    private int selectedHero = 0;

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

        initHeroImages();

        setMeasuredDimension(length, length);
    }

    /**
     * Initialize level background images.
     */
    private void initHeroImages() {
        heroImageViews = new ImageView[piece * piece];
        final int[] heroBackgroundImagesIds = HeroSelectionBackgroundImages.getHeroBackgroundImages(this.levelBackgroundImagesStyle);
        for (int i = 0; i < heroImageViews.length; i++) {
            final ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(heroBackgroundImagesIds[i]);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setId(i + 1);
            if (i == selectedHero) {
                imageView.setBackgroundColor(Color.RED);
            }
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedHero != view.getId() - 1) {
                        heroImageViews[selectedHero].setBackgroundColor(Color.BLACK);
                        selectedHero = view.getId() - 1;
                    }
                    imageView.setBackgroundColor(Color.RED);
                }
            });

            heroImageViews[i] = imageView;

            LayoutParams layoutParams = new LayoutParams(itemLength, itemLength);
            if (0 != i % piece) {
                layoutParams.leftMargin = margin;
                layoutParams.addRule(RelativeLayout.RIGHT_OF, heroImageViews[i - 1].getId());
            }

            if (i + 1 > piece) {
                layoutParams.topMargin = margin;
                layoutParams.addRule(RelativeLayout.BELOW, heroImageViews[i - piece].getId());
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
            initHeroImages();
        }
    }

    public void startGame() {
        Intent intent = new Intent();
        intent.setClass(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }
}
