package com.orange.jigsaw.utils;

import com.orange.jigsaw.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Class which stores map from piece to difficulty string.
 * Created by Orange on 2015/4/12.
 */
public class PieceDifficultyUtils {
    private static final Map<Integer, Integer> PIECE_DIFFICULTY_STRING_MAP = new HashMap<Integer, Integer>();
    static {
        PIECE_DIFFICULTY_STRING_MAP.put(3, R.string.easy);
        PIECE_DIFFICULTY_STRING_MAP.put(4, R.string.normal);
        PIECE_DIFFICULTY_STRING_MAP.put(5, R.string.hard);
    }

    /**
     * Get difficulty string resource id by piece.
     * @param piece
     * @return
     */
    public static int getDifficultyStringResourceId(int piece) {
        return PIECE_DIFFICULTY_STRING_MAP.get(piece);
    }
}
