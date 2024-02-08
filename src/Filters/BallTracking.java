package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BallTracking implements PixelFilter {
    private PixelFilter identity = new Identity();
    private PixelFilter blur = new Blur();
    private PixelFilter colorMaskRGB = new ColorMaskRGB();
    private PixelFilter colorMaskHSV = new ColorMaskHSV();
    @Override
    public DImage processImage(DImage img) {
        img = blur.processImage(img);
        img = colorMaskRGB.processImage(img);
        return img;
    }
}
