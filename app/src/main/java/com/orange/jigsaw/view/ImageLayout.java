package com.orange.jigsaw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.orange.jigsaw.R;
import com.orange.jigsaw.utils.ImagePiece;
import com.orange.jigsaw.utils.ImageSpliter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Orange on 2015/4/1.
 */
public class ImageLayout extends RelativeLayout implements View.OnClickListener {
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
            public int compare(ImagePiece imagePiece, ImagePiece imagePiece2) {
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
            item.setTag(i + "_" + imagePieces.get(i).getIndex());
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

    }
}
