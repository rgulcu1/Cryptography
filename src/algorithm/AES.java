package algorithm;

import key.SymmetricKey;
import util.Constants;
import util.Helper;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class AES {

    private String[][] block = new String[4][4];

    private static String[][] IV = new String[4][4];

    private static String[][][] expandedKey;

    private String[] cipherText;

    public static String[] encrypt(String[] plainText, SymmetricKey key, Constants.Method streamMethod){

        generateInitializationVector();
        keyExpand(key);
        return null;

    }


    private static void generateInitializationVector() {


        for (int i = 0; i <4 ; i++) {
            byte[] randomByte = new byte[4];
            new SecureRandom().nextBytes(randomByte);

            for (int j = 0; j < 4; j++) {

                IV[i][j]= String.format("%02x", randomByte[j]).toUpperCase();
            }
        }
    }

    private static void keyExpand(SymmetricKey key) {

        String keyAsHex = key.getSymetricKeyAsHex();

        ArrayList<String> expandedKeyAsLine = new ArrayList<>();
        keyAsHex = keyAsHex.replaceAll("(.{" + 2 + "})", "$1 ").trim();

        expandedKeyAsLine.addAll(Arrays.asList(keyAsHex.split(" ")));

        expandKeyLoop(expandedKeyAsLine);

        generateKeys
    }


    private static void generateKeys(ArrayList<String> expandedKeyAsLine) {

        int totalKeyNumber=11;

        switch (expandedKeyAsLine.size()) {
            case 176: totalKeyNumber = 11;
                break;
            case 208: totalKeyNumber = 13;
                break;
            case 240: totalKeyNumber=15;
                break;

        }

        expandedKey = new String[totalKeyNumber][4][4];

        for (int i = 0; i < totalKeyNumber; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {

                    expandedKey[i][k][j] = expandedKeyAsLine.get(i<<4 + j << 2 + k);

                }
            }
        }
    }

    private static void expandKeyLoop(ArrayList<String> expandedKey) {

        int keyByte = expandedKey.size();
        int c = keyByte;
        int i = 0;
        int indexCtr = 0;

        int totalByte;

        switch (keyByte){
            case 16: totalByte=176;
                break;
            case 24: totalByte=208;
                break;
            case 32: totalByte=240;
                break;
            default: totalByte=176;
        }


        while(c < totalByte) {

            if (c % keyByte == 0) {
                List<String> rootWord = new ArrayList<>(expandedKey.subList(c - 4, c));
                Collections.rotate(rootWord,-1);
                rootWord.forEach(s -> rootWord.set(rootWord.indexOf(s),substitueByte(s)));
                String[] rcon = Constants.RCON[i];
                List<String> firstWord = new ArrayList<>(expandedKey.subList(indexCtr, indexCtr+4));


                for (int j = 0; j < 4; j++) {

                    String newHex = Helper.hexXOR(rootWord.get(j), rcon[j]);
                    newHex = Helper.hexXOR(newHex, firstWord.get(j));
                    rootWord.set(j, newHex);
                }
                expandedKey.addAll(rootWord);
                c+=4;
                i++;
                indexCtr+=4;
                continue;
            }

            List<String> firstWord = new ArrayList<>(expandedKey.subList(indexCtr, indexCtr+4));
            List<String> secondWord = new ArrayList<>(expandedKey.subList(indexCtr+ keyByte - 4, indexCtr+keyByte));

            for (int j = 0; j < 4; j++) {

                String newHex = Helper.hexXOR(firstWord.get(j), secondWord.get(j));
                expandedKey.add(newHex);
            }
            c+=4;
            indexCtr+=4;
        }
    }

    private static String substitueByte(String hex) {

        int decimalValue = Integer.parseInt(hex, 16);
        int changedValue = Constants.SBOX[decimalValue];

        return Integer.toHexString(changedValue).toUpperCase();
    }


}
