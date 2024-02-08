package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMaskRGB implements PixelFilter, Interactive {
    private short tRed, tGreen, tBlue;
    private double threshold = 55;

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if (distance(tRed, tGreen, tBlue, red[i][j], green[i][j], blue[i][j]) < threshold) {
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

    public double distance(short x, short y, short z, short x1, short y1, short z1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1) + (z - z1) * (z - z1));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        tRed = img.getRedChannel()[mouseY][mouseX];
        tGreen = img.getGreenChannel()[mouseY][mouseX];
        tBlue = img.getBlueChannel()[mouseY][mouseX];
    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') threshold -= 15;
        else if (key == '-') threshold += 15;
        System.out.println(threshold);
    }
}

class Color {
    short r, g, b;
    public Color(short r, short g, short b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public short getR() {
        return r;
    }

    public short getG() {
        return g;
    }

    public short getB() {
        return b;
    }
}