package com.orange.jigsaw.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orange on 2015/4/1.
 */
public class ImageSpliter {
    /**
     * Split bitmap into piece * piece blocks
     * @param bitmap
     * @param piece
     * @return
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece) {
        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / piece;
        int pieceHeight = height / piece;

        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(i * piece + j);
                int x = j * pieceWidth;
                int y = i * pieceHeight;
                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceHeight));
                imagePieces.add(imagePiece);
            }
        }

        return imagePieces;
    }

}
