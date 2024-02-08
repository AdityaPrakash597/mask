package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMaskHSV implements PixelFilter, Interactive {
    private short[] targetColor = {0, 0, 0};
    private double threshold = 10;

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        rgb_to_hsv(0, 255, 0);
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                short[] color = rgb_to_hsv(red[i][j], green[i][j], blue[i][j]);
                if (color[0] > targetColor[0] - threshold && color[0] <= targetColor[0] + threshold) {
                    red[i][j] = 255;
                    green[i][j] = 255;
                    blue[i][j] = 255;
                } else {
                    red[i][j] = 0;
                    green[i][j] = 0;
                    blue[i][j] = 0;
                }
            }
        } img.setColorChannels(red, green, blue);
        return img;
    }

    public static short[] rgb_to_hsv(double r, double g, double b) {

        // R, G, B values are divided by 255
        // to change the range from 0..255 to 0..1
        r = r / 255.0;
        g = g / 255.0;
        b = b / 255.0;

        // h, s, v = hue, saturation, value
        double cmax = Math.max(r, Math.max(g, b)); // maximum of r, g, b
        double cmin = Math.min(r, Math.min(g, b)); // minimum of r, g, b
        double diff = cmax - cmin; // diff of cmax and cmin.
        double h = -1, s = -1;

        // if cmax and cmax are equal then h = 0
        if (cmax == cmin)
            h = 0;

            // if cmax equal r then compute h
        else if (cmax == r)
            h = (60 * ((g - b) / diff) + 360) % 360;

            // if cmax equal g then compute h
        else if (cmax == g)
            h = (60 * ((b - r) / diff) + 120) % 360;

            // if cmax equal b then compute h
        else if (cmax == b)
            h = (60 * ((r - g) / diff) + 240) % 360;

        // if cmax equal zero
        if (cmax == 0)
            s = 0;
        else
            s = (diff / cmax) * 100;

        // compute v
        double v = cmax * 100;
        return new short[]{(short) h, (short) s, (short) v};

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        double tRed = img.getRedChannel()[mouseY][mouseX];
        double tGreen = img.getGreenChannel()[mouseY][mouseX];
        double tBlue = img.getBlueChannel()[mouseY][mouseX];
        targetColor = rgb_to_hsv(tRed, tGreen, tBlue);
    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') threshold --;
        else if (key == '-') threshold ++;
    }
}