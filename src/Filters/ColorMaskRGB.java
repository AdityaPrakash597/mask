package Filters;
import Interfaces.PixelFilter;
import core.DImage;

import java.util.Objects;

public class ColorMaskRGB implements PixelFilter {
    private final Color targetGreen = new Color((short) 75, (short) 172, (short) 33);
    private final Color targetYellow = new Color((short) 240, (short) 201, (short) 2);
    private final Color targetBlue = new Color((short) 8, (short) 45, (short) 175);
    private final Color targetOrange =  new Color((short) 195, (short) 74, (short) 10);
    private final Color targetWhite = new Color((short) 200, (short) 178, (short) 165);
    private final double threshold = 50; //55

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                if (distance(targetGreen.r(), targetGreen.g(), targetGreen.b(), red[i][j], green[i][j], blue[i][j]) < threshold
                    || distance(targetYellow.r(), targetYellow.g(), targetYellow.b(), red[i][j], green[i][j], blue[i][j]) < threshold
                    || distance(targetBlue.r(), targetBlue.g(), targetBlue.b(), red[i][j], green[i][j], blue[i][j]) < threshold) {
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
}

final class Color {
    private final short r;
    private final short g;
    private final short b;

    Color(short r, short g, short b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public short r() {
        return r;
    }

    public short g() {
        return g;
    }

    public short b() {
        return b;
    }

    @Override
    public String toString() {
        return "Color[" +
                "r=" + r + ", " +
                "g=" + g + ", " +
                "b=" + b + ']';
    }


}

