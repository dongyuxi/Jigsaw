package com.orange.jigsaw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
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
    private static final int DURATION_TIME = 500;

    /** Image has been splited into piece * piece. */
    private int piece = 3;
    /** Padding. */
    private int padding;
    /** 3dp to make sure it is the same on all devices. */
    private static final int MARGIN = 3;
    private int margin;

    /** Source picture. */
    private Bitmap image;
    /** Random pieces list. */
    private List<ImagePiece> imagePieces;
    /** ImageView list shown on the layout. */
    private ImageView[] imageViews;
    /** Length of layout. */
    private int length;
    /** Length for each ImageView. */
    private int itemLength;

    /** Flag to mark if it is first set. */
    private boolean first = true;

    /** First selected ImageView. */
    private ImageView firstSelectedView;
    /** Second selected ImageView. */
    private ImageView secondSelectedView;

    /** Animation layout. */
    private RelativeLayout animationLayout;
    /** Animation flag, block user clicks when it is animating. */
    private boolean isInAnimation = false;

    private static final int NEXT_LEVEL = 0x0000;

    /** Callback listener instance. */
    private ImageLayoutListener imageListener;

    /** Setter for imageListener. */
    public void setImageListener(ImageLayoutListener imageListener) {
        this.imageListener = imageListener;
    }

    /** Handler which will refresh ImageLayout. */
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEXT_LEVEL:
                    if (null == imageListener) {
                        nextLevel();
                    } else {
                       imageListener.nextLevel();
                    }
                    break;
                default:
                    break;
            }
        }
    };

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
            initViews();
            first = false;
        }

        setMeasuredDimension(length, length);
    }

    /**
     * Initialize Bitmap image and make ImagePieces random.
     */
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

    /**
     * Put ImagePieces into corresponding ImageViews.
     */
    private void initViews() {
        itemLength = (length - 2 * padding - (piece - 1) * margin) / piece;

        imageViews = new ImageView[piece * piece];
        for (int i = 0; i < imageViews.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(imagePieces.get(i).getBitmap());
            item.setId(i + 1);
            item.setTag(i + CONNECTOR + imagePieces.get(i).getIndex());
            imageViews[i] = item;

            LayoutParams layoutParams = new LayoutParams(itemLength, itemLength);

            if (0 != i % piece) {
                layoutParams.leftMargin = margin;
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imageViews[i - 1].getId());
            }

            if (i + 1 > piece) {
                layoutParams.topMargin = margin;
                layoutParams.addRule(RelativeLayout.BELOW, imageViews[i - piece].getId());
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

    /**
     * Exchange two selected ImageViews.
     * Using TranslateAnimation.
     */
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

                checkSuccess();
                isInAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * Get index in view by tag information.
     * Tag information is "IndexInView-IndexInImage".
     * @param tag
     * @return
     */
    private int getIndexInViewByTag(String tag) {
        return Integer.parseInt(tag.split(CONNECTOR)[0]);
    }

    /**
     * Get index in image by tag information.
     * Tag information is "IndexInView-IndexInImage".
     * @param tag
     * @return
     */
    private int getIndexInImageByTag(String tag) {
        return Integer.parseInt(tag.split(CONNECTOR)[1]);
    }

    /**
     * Get Bitmap by tag information.
     * Tag information is "IndexInView-IndexInImage".
     * @param tag
     * @return
     */
    private Bitmap getBitmapByTag(String tag) {
        int indexInView = getIndexInViewByTag(tag);
        return imagePieces.get(indexInView).getBitmap();
    }

    /**
     * Create animation layout and add to view.
     */
    private void setupAnimationLayout() {
        if (null == animationLayout) {
            animationLayout = new RelativeLayout(getContext());
            addView(animationLayout);
        }
    }

    /**
     * Create animation view using existing view.
     * @param selectedView
     * @return
     */
    private ImageView createAnimationView(ImageView selectedView) {
        ImageView animationView = new ImageView(getContext());
        animationView.setImageBitmap(getBitmapByTag((String) selectedView.getTag()));
        LayoutParams params = new LayoutParams(itemLength, itemLength);
        params.leftMargin = selectedView.getLeft() - padding;
        params.topMargin = selectedView.getTop() - padding;
        animationView.setLayoutParams(params);
        return animationView;
    }

    /**
     * Create animation using first and second view.
     * @param first
     * @param second
     * @return
     */
    private Animation createAnimation(ImageView first, ImageView second) {
        TranslateAnimation animation = new TranslateAnimation(0, second.getLeft() - first.getLeft(),
                0, second.getTop() - first.getTop());
        animation.setDuration(DURATION_TIME);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * Check if the game is successful for this level.
     */
    private void checkSuccess() {
        boolean success = true;
        for (int i = 0; i < imageViews.length; i++) {
            String tag = (String) imageViews[i].getTag();
            if (i != getIndexInImageByTag(tag)) {
                success = false;
                break;
            }
        }
        if (success) {
            handler.sendEmptyMessage(NEXT_LEVEL);
        }
    }

    /**
     * Initialize for next level.
     */
    public void nextLevel() {
        this.removeAllViews();
        animationLayout = null;
        piece++;
        initBitmap();
        initViews();
    }

}
