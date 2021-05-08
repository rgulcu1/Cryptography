package util;

public class ImageInfo {

    private int height;

    private int width;

    private int[] pixels;

    public ImageInfo(final int height, final int width, final int[] pixels) {
        this.height = height;
        this.width = width;
        this.pixels = pixels;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[] getPixels() {
        return pixels;
    }
}
