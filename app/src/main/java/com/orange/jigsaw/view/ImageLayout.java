package com.orange.jigsaw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.orange.jigsaw.R;
import com.orange.jigsaw.utils.ImagePiece;
import com.orange.jigsaw.utils.ImageSpliter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An RelativeLayout which shows the Jigsaw Puzzle.
 * Created by dongyuxi on 2015/4/1.
 */
public class ImageLayout extends RelativeLayout implements View.OnClickListener {
    private static final String CONNECTOR = "-";
    private int piece = 3;
    private int padding;
    /* 3dp to make sure it is the same on all devices. */
    private static final int MARGIN = 3;
    private int margin;

    /** source picture. */
    private Bitmap image;
    private List<ImagePiece> imagePieces;
    private ImageView[] imageItems;
    private int length;
    private int itemLength;

    private boolean first = true;

    private ImageView firstSelectedView;
    private ImageView secondSelectedView;

    private RelativeLayout animationLayout;
    private boolean isInAnimation = false;

    public ImageLayout(Context context) {
        this(context, null);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN, getResources().getDisplayMetrics());
        padding = Math.min(Math.min(getPaddingBottom(), getPaddingTop()), Math.min(getPaddingLeft(), getPaddingRight()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        length = Math.min(getMeasuredWidth(), getMeasuredHeight());

        if (first) {
            initBitmap();
            initItem();
            first = false;
        }

        setMeasuredDimension(length, length);
    }

    private void initBitmap() {
        if (null == image) {
            image = BitmapFactory.decodeResource(getResources(), R.drawable.arilia);
        }
        imagePieces = ImageSpliter.splitImage(image, piece);
        Collections.sort(imagePieces, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece imagePiece1, ImagePiece imagePiece2) {
                if (imagePiece1 == imagePiece2) {
                    return 0;
                }
                return Math.random() > 0.5 ? -1 : 1;
            }
        });
    }


    private void initItem() {
        itemLength = (length - 2 * padding - (piece - 1) * margin) / piece;

        imageItems = new ImageView[piece * piece];
        for (int i = 0; i < imageItems.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(imagePieces.get(i).getBitmap());
            item.setId(i + 1);
            item.setTag(i + CONNECTOR + imagePieces.get(i).getIndex());
            imageItems[i] = item;

            LayoutParams layoutParams = new LayoutParams(itemLength, itemLength);

            if (0 != i % piece) {
                layoutParams.leftMargin = margin;
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imageItems[i - 1].getId());
            }

            if (i + 1 > piece) {
                layoutParams.topMargin = margin;
                layoutParams.addRule(RelativeLayout.BELOW, imageItems[i - piece].getId());
            }

            addView(item, layoutParams);
        }
    }

    @Override
    public void onClick(View view) {
        if (isInAnimation) {
            return;
        }

        if (null == firstSelectedView) {
            // set color to gray
            firstSelectedView = (ImageView)view;
            firstSelectedView.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        } else {
            secondSelectedView = (ImageView)view;
            exchangeSelectImages();
        }
    }

    private void exchangeSelectImages() {
        firstSelectedView.setColorFilter(null);
        setupAnimationLayout();

        ImageView firstAnimationView = createAnimationView(firstSelectedView);
        ImageView secondAnimationView = createAnimationView(secondSelectedView);
        animationLayout.addView(firstAnimationView);
        animationLayout.addView(secondAnimationView);

        Animation firstAnimation = createAnimation(firstSelectedView, secondSelectedView);
        final Animation secondAnimation = createAnimation(secondSelectedView, firstSelectedView);
        firstAnimationView.setAnimation(firstAnimation);
        secondAnimationView.setAnimation(secondAnimation);

        firstAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isInAnimation = true;
                firstSelectedView.setVisibility(View.INVISIBLE);
                secondSelectedView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // get ImageView tag
                String firstSelectedTag = (String) firstSelectedView.getTag();
                String secondSelectedTag = (String) secondSelectedView.getTag();
                // get ImageView index by tag
                Bitmap firstSelectBitmap = getBitmapByTag(firstSelectedTag);
                Bitmap secondSelectBitmap = getBitmapByTag(secondSelectedTag);

                // exchange firstSelectedImageView and secondSelectedImageView
                firstSelectedView.setImageBitmap(secondSelectBitmap);
                secondSelectedView.setImageBitmap(firstSelectBitmap);
                // modify firstSelectedImageView and secondSelectedImageView tag
                firstSelectedView.setTag(secondSelectedTag);
                secondSelectedView.setTag(firstSelectedTag);

                // change color back to normal
                firstSelectedView.setVisibility(View.VISIBLE);
                secondSelectedView.setVisibility(View.VISIBLE);
                firstSelectedView = null;
                secondSelectedView = null;
                animationLayout.removeAllViews();
                isInAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private int getIndexInViewByTag(String tag) {
        return Integer.parseInt(tag.split(CONNECTOR)[0]);
    }

    private int getIndexInImageByTag(String tag) {
        return Integer.parseInt(tag.split(CONNECTOR)[1]);
    }

    private Bitmap getBitmapByTag(String tag) {
        int indexInView = getIndexInViewByTag(tag);
        return imagePieces.get(indexInView).getBitmap();
    }

    private void setupAnimationLayout() {
        if (null == animationLayout) {
            animationLayout = new RelativeLayout(getContext());
            addView(animationLayout);
        }
    }

    private ImageView createAnimationView(ImageView selectedView) {
        ImageView animationView = new ImageView(getContext());
        animationView.setImageBitmap(getBitmapByTag((String) selectedView.getTag()));
        LayoutParams params = new LayoutParams(itemLength, itemLength);
        params.leftMargin = selectedView.getLeft() - padding;
        params.topMargin = selectedView.getTop() - padding;
        animationView.setLayoutParams(params);
        return animationView;
    }

    private Animation createAnimation(ImageView first, ImageView second) {
        TranslateAnimation animation = new TranslateAnimation(0, second.getLeft() - first.getLeft(),
                0, second.getTop() - first.getTop());
        animation.setDuration(500);
        animation.setFillAfter(true);
        return animation;
    }

}
