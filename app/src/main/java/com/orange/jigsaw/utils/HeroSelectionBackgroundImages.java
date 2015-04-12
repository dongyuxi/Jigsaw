package com.orange.jigsaw.utils;

import com.orange.jigsaw.R;

/**
 * Class which holds the images for selecting heroes.
 * Created by Orange on 2015/4/11.
 */
public class HeroSelectionBackgroundImages {
    private static final int[][] HERO_BACKGROUND_IMAGES = new int[][]
            {
                    {
                            R.drawable.level1ashe0,
                            R.drawable.level2caitlyn0,
                            R.drawable.level3evelynn0,
                            R.drawable.level4leblanc0,
                            R.drawable.level5lux0,
                            R.drawable.level6missfortune0,
                            R.drawable.level7morgana0,
                            R.drawable.level8sona0,
                            R.drawable.level9vayne0,
                    },
                    {
                            R.drawable.level1ashe1,
                            R.drawable.level2caitlyn1,
                            R.drawable.level3evelynn1,
                            R.drawable.level4leblanc1,
                            R.drawable.level5lux1,
                            R.drawable.level6missfortune1,
                            R.drawable.level7morgana1,
                            R.drawable.level8sona1,
                            R.drawable.level9vayne1,
                    },
                    {
                            R.drawable.level1ashe2,
                            R.drawable.level2caitlyn2,
                            R.drawable.level3evelynn2,
                            R.drawable.level4leblanc2,
                            R.drawable.level5lux2,
                            R.drawable.level6missfortune2,
                            R.drawable.level7morgana2,
                            R.drawable.level8sona2,
                            R.drawable.level9vayne2,
                    }
            };

    public enum Style {
        CLASSIC(0),
        CUTE(1),
        JIGSAW(2);

        private int index;

        Style(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

    }

    /**
     * Return hero background images resources id array according type.
     * @param style
     * @return
     */
    public static int[] getHeroBackgroundImages(Style style) {
        return HERO_BACKGROUND_IMAGES[style.getIndex()];
    }

}
