package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

//public class BallTracking implements PixelFilter, Interactive {
public class BallTracking implements PixelFilter {
    private PixelFilter identity = new Identity();
    private PixelFilter blur = new Blur();
    private PixelFilter colorMaskRGB = new ColorMaskRGB();
    private PixelFilter colorMaskHSV = new ColorMaskHSV();
    private PixelFilter findCenters = new FindCenters();
    @Override
    public DImage processImage(DImage img) {
        img = blur.processImage(img);
        img = colorMaskRGB.processImage(img);
        //for (int i = 0; i < 5; i++) img = blur.processImage(img);
        img = findCenters.processImage(img);

        return img;
    }
    /*
    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        colorMaskRGB.mouseClicked(mouseX, mouseY, img);
    }

    @Override
    public void keyPressed(char key) {
        colorMaskRGB.keyPressed(key);
    }
    */
}
