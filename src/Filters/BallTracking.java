package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BallTracking implements PixelFilter {
    private PixelFilter identity = new Identity();
    private PixelFilter blur = new Blur();
    @Override
    public DImage processImage(DImage img) {
        img = blur.processImage(img);
        return img;
    }
}
