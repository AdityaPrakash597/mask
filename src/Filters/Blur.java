package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Blur implements PixelFilter {
    private final double[][] gaussianBlurKernel = {
            {0, 0, 0, 5, 0, 0, 0},
            {0, 5, 18, 32, 18, 5, 0},
            {0, 18, 64, 100, 64, 18, 0},
            {5, 32, 100, 100, 100, 32, 5},
            {0, 18, 64, 100, 64, 18, 0},
            {0, 5, 18, 32, 18, 5, 0},
            {0, 0, 0, 5, 0, 0, 0},
    };
    @Override
    public DImage processImage(DImage img) {
        short[][] red = applyKernel(img.getRedChannel(), gaussianBlurKernel);
        short[][] green = applyKernel(img.getGreenChannel(), gaussianBlurKernel);
        short[][] blue = applyKernel(img.getBlueChannel(), gaussianBlurKernel);
        img.setColorChannels(red, green, blue);

        return img;
    }

    public short[][] applyKernel(short[][] img, double[][] kernel) {
        short[][] copy = img.clone();
        for (int r = kernel.length/2; r < img.length-kernel.length/2; r++) {
            for (int c = kernel[0].length/2; c < img[0].length-kernel[0].length/2; c++) {
                // r, c is the current cell
                double output, kernelSum;
                output = kernelSum = 0;
                for (int i = -kernel.length/2; i <= kernel.length/2; i++) {
                    for (int j = -kernel[0].length/2; j <= kernel[0].length/2; j++) {
                        output += copy[r+i][c+i] * kernel[i+kernel.length/2][j+kernel.length/2];
                        kernelSum += kernel[i+kernel.length/2][j+kernel.length/2];
                    }
                }
                if (kernelSum != 0) output /= kernelSum;
                if (output < 0) output = 0;
                else if (output > 255) output = 255;
                img[r][c] = (short) output;
            }
        } return img;
    }
}
