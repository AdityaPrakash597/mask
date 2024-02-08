package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class BallTracking implements PixelFilter, Interactive {
    private PixelFilter identity = new Identity();
    private PixelFilter blur = new Blur();
    private ColorMaskRGB colorMaskRGB = new ColorMaskRGB();
    private PixelFilter colorMaskHSV = new ColorMaskHSV();
    @Override
    public DImage processImage(DImage img) {
        img = blur.processImage(img);
        img = colorMaskRGB.processImage(img);

        return img;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        colorMaskRGB.mouseClicked(mouseX, mouseY, img);
    }

    @Override
    public void keyPressed(char key) {
        colorMaskRGB.keyPressed(key);
    }
}