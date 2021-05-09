package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class Helper {



    public static void calculateFirstFewPrime(){ // use Sieve of Eratosthenes

        boolean[] isNotPrime = new boolean[Constants.SIEVE_OF_ERATOSTHENES_N + 1];

        for (int i = 2; i <= Math.sqrt(Constants.SIEVE_OF_ERATOSTHENES_N ) ; i++) {

            if (!isNotPrime[i]) {
                for (int j = i*i; j <= Constants.SIEVE_OF_ERATOSTHENES_N ; j+=i) {
                    isNotPrime[j] = true;
                }
            }
        }

        for (int i = 2; i <= Constants.SIEVE_OF_ERATOSTHENES_N ; i++) {

            if (!isNotPrime[i]) Constants.firstFewPrime.add(i);
        }

    }

    public static BigInteger nextRandomBigInteger(BigInteger n) {
        Random rand = new Random();
        BigInteger result = new BigInteger(rand.nextInt(n.bitLength()), rand);
        while( result.compareTo(n.subtract(BigInteger.valueOf(2))) >= 0 ) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result;
    }

    //it calculates x^y mod p
    public static BigInteger modForBigNumbers(BigInteger x, BigInteger y, BigInteger p) {

        BigInteger res = BigInteger.ONE; // Initialize result

        //Update x if it is more than or
        // equal to p
        x = x.mod(p);

        while (y.compareTo(BigInteger.ZERO) == 1) {

            // If y is odd, multiply x with result
            if ((y.mod(BigInteger.valueOf(2))).equals(BigInteger.ONE))
                res = res.multiply(x).mod(p);

            // y must be even now
            y = y.divide(BigInteger.TWO); // y = y/2
            x = x.pow(2).mod(p);
        }

        return res;
    }


    public static String hashWithSHA256(String data) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deepCopy2DArray(Object[][] destination, Object[][] source) {
        for (int i = 0; i <4 ; i++) {
            destination[i] = source[i].clone();
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }

    public static byte[] readImageAsByteArray (String imagePath){
        // open image
        Path source = Paths.get(imagePath);

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(source.toFile());

            // convert BufferedImage to byte[]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
           return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printByteArrayAsImage(byte[] bytes, String imageName) {

        InputStream is = new ByteArrayInputStream(bytes);
        Path path = Paths.get(imageName);

        BufferedImage newBi = null;
        try {
            newBi = ImageIO.read(is);
            // save it
            ImageIO.write(newBi, "png", path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static ImageInfo getRGBvaluesOfImage(String imagePath) {

        File f = new File(imagePath);
        try {
            BufferedImage img = ImageIO.read(f);
            int height = img.getHeight();
            int width = img.getWidth();

            int[][] rgbPixels = new int[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    rgbPixels[i][j] = img.getRGB(j,i);
                }

            }

            return new ImageInfo(height,width,unrollArray(rgbPixels));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int[] unrollArray(int[][] doubleArray) {

        int length = doubleArray.length;
        int length1 = doubleArray[0].length;

        int[] unrolledLoop = new int[length * length1];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length1; j++) {
                unrolledLoop[i*length1 + j] = doubleArray[i][j];
            }
        }

        return unrolledLoop;
    }

    public static void printImageWithRgbPixels(ImageInfo imageInfo, String imageName) {

        int height = imageInfo.getHeight();
        int width = imageInfo.getWidth();
        int[] rgbPixels = imageInfo.getPixels();

        BufferedImage img = new BufferedImage(width,height,1);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                img.setRGB(j,i,rgbPixels[i*width + j]);

            }
        }

        File file = new File(imageName+".jpg");
        try {
            ImageIO.write(img, "jpg", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] convertIntArrayToByteArray(int[] array) {

        int length = array.length;
        String[] byteArray = new String[length * 4];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int decimal = array[i];
            String hexString = Integer.toHexString(decimal);

            String formattedHex = ("00000000" + hexString).substring(hexString.length()).toUpperCase();
            sb.append(formattedHex);
        }

        String arrayAsHex = sb.toString();
        arrayAsHex = arrayAsHex.replaceAll("(.{" + 2 + "})", "$1 ").trim();

        return arrayAsHex.split(" ");
    }

    public static int[] convertByteArrayToIntArray(String[] array) {

        int length = array.length;
        int[] ints = new int[length / 4];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i+=4) {
            sb.append(array[i]);
            sb.append(array[i+1]);
            sb.append(array[i+2]);
            sb.append(array[i+3]);

            String integerAsHex = sb.toString();
            ints[i/4] = new BigInteger(integerAsHex, 16).intValue();
            sb.setLength(0);
        }

        return ints;
    }


    public static String hexXOR(String a, String b) {

        int n1 = Integer.parseInt(a, 16);
        int n2 = Integer.parseInt(b, 16);
        int n3 = n1 ^ n2;
        return String.format("%02x", n3).toUpperCase();
    }

}
