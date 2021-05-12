import algorithm.AES;
import key.KeyPair;
import key.PrivateKey;
import key.PublicKey;
import key.SymmetricKey;
import util.Helper;
import util.ImageInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static util.Constants.*;
import static util.Constants.Method.CBC;
import static util.Constants.Method.CTR;


public class main {

    public static void main(String[] args) {
        Helper.calculateFirstFewPrime();
        KeyPair keyPair = algorithm.RSA.generateKeyset();
        PrivateKey privateKey = (PrivateKey) keyPair.getPrivateKey();
        PublicKey publicKey = (PublicKey) keyPair.getPublicKey();

        System.out.println("Public and Private Key generated with algorithm.RSA\n");

        SymmetricKey k1 = new SymmetricKey(128);
        System.out.print("Symmetric key 1 (K1): ");
        System.out.println(k1.getSymetricKeyAsHex());

        SymmetricKey k2 = new SymmetricKey(256);
        System.out.print("Symmetric key 2 (K2): ");
        System.out.println(k2.getSymetricKeyAsHex());

        System.out.println("\n*******************************************************************************************************************\n");
        encryptAndDecryptSymmetricKey(k1,publicKey,privateKey);
        System.out.println("\n*******************************************************************************************************************\n");
        encryptAndDecryptSymmetricKey(k2,publicKey,privateKey);
        System.out.println("\n*******************************************************************************************************************\n");
        applyAndVerifyDigitalSignature(publicKey, privateKey);
        System.out.println("\n*******************************************************************************************************************\n");

        System.out.println("Image Reading...");
        ImageInfo imageInfo = Helper.getRGBvaluesOfImage(IMAGES_FOLDER_PATH+PLAIN_IMAGE_NAME);
        int height = imageInfo.getHeight();
        int width = imageInfo.getWidth();
        String[] pixelsAsByte = Helper.convertIntArrayToByteArray(imageInfo.getPixels());

        String pathEnc128BitCBCv1 = METHOD_CBC_KEY_128_PATH + ENCRYPTED_IMAGE_NAME + "_v1";
        String pathDec128BitCBC = METHOD_CBC_KEY_128_PATH + DECRYPTED_IMAGE_NAME;
        String[] encryptedImage128CBCv1 = encryptImageAndPrint(pixelsAsByte, CBC, pathEnc128BitCBCv1, k1, width, height);
        decryptImageAndPrint(encryptedImage128CBCv1, CBC,pathDec128BitCBC, k1, width, height);

        System.out.println("Initial Vector is changed and again apply encryption.");
        String pathEnc128BitCBCv2 = METHOD_CBC_KEY_128_PATH + ENCRYPTED_IMAGE_NAME + "_v2";
        encryptImageAndPrint(pixelsAsByte, CBC, pathEnc128BitCBCv2, k1, width, height);

        System.out.println("\n*******************************************************************************************************************\n");

        String pathEnc256BitCBC = METHOD_CBC_KEY_256_PATH + ENCRYPTED_IMAGE_NAME;
        String pathDec256BitCBC = METHOD_CBC_KEY_256_PATH + DECRYPTED_IMAGE_NAME;
        String[] encryptedImage256CBC = encryptImageAndPrint(pixelsAsByte, CBC, pathEnc256BitCBC, k2, width, height);
        decryptImageAndPrint(encryptedImage256CBC, CBC,pathDec256BitCBC, k2, width, height);

        System.out.println("\n*******************************************************************************************************************\n");

        String pathEnc256BitCTR = METHOD_CTR_PATH + ENCRYPTED_IMAGE_NAME;
        String pathDec256BitCTR = METHOD_CTR_PATH + DECRYPTED_IMAGE_NAME;
        String[] encryptedImage256CTR = encryptImageAndPrint(pixelsAsByte, CTR, pathEnc256BitCTR, k2, width, height);
        decryptImageAndPrint(encryptedImage256CTR, CTR,pathDec256BitCTR, k2, width, height);

    }
    private static void encryptAndDecryptSymmetricKey(SymmetricKey symmetricKey, PublicKey publicKey, PrivateKey privateKey) {

        String cipherTextK1 = publicKey.encrypt(symmetricKey.getSymetricKeyAsHex());
        System.out.print("K1 is encrypted with Ka+ . The ciphertext is: ");
        System.out.println(cipherTextK1.replaceAll("(.{" + 100 + "})", "$1 \n\t\t\t\t\t\t\t\t\t\t\t  "));

        String plainTextK1 = privateKey.decrypt(cipherTextK1);
        System.out.print("K1 is decrypted with Ka- . The plaintext is: ");
        System.out.println(plainTextK1);
    }

    private static void applyAndVerifyDigitalSignature(PublicKey publicKey, PrivateKey privateKey) {

        System.out.println("Message:");
        System.out.println(message);

        String hashedMessage = Helper.hashWithSHA256(message);
        System.out.print("\nApplied SHA256 to message. H(m): ");
        System.out.println(hashedMessage);

        String digitalSignature = privateKey.generateDigitalSignature(hashedMessage);
        System.out.print("\nH(m) encrypt with Ka-. Digital Signature: ");
        System.out.println(digitalSignature.replaceAll("(.{" + 100 + "})", "$1 \n\t\t\t\t\t\t\t\t\t\t  "));

        publicKey.verifyDigitalSignature(digitalSignature, message);
    }

    private static String[] encryptImageAndPrint(String[] imagePixels, Method method, String imagePath, SymmetricKey key, int width, int height) {

        NumberFormat formatter = new DecimalFormat("#0.00000");

        long start = System.currentTimeMillis();
        System.out.println("Image Encrypting with "+key.getBitSize()+" Bit key in "+method.name()+" mode...");
        String[] cipherImageText = AES.streamCipherEncryption(imagePixels, key, method);
        long end = System.currentTimeMillis();
        ImageInfo cipherImage = new ImageInfo(height, width, Helper.convertByteArrayToIntArray(cipherImageText));
        Helper.printImageWithRgbPixels(cipherImage, imagePath);
        System.out.println("Image is encrypted in "+formatter.format((end - start) / 1000d)+" seconds.");

        return cipherImageText;
    }

    private static void decryptImageAndPrint(String[] cipherImage, Method method, String imagePath, SymmetricKey key, int width, int height) {

        NumberFormat formatter = new DecimalFormat("#0.00000");

        long start = System.currentTimeMillis();
        System.out.println("Image Decrypting with "+key.getBitSize()+" Bit key in "+method.name()+" mode...");
        String[] decryptedImageText= AES.streamCipherDecryption(cipherImage, key, method);
        long end = System.currentTimeMillis();
        ImageInfo decryptedImage = new ImageInfo(height, width, Helper.convertByteArrayToIntArray(decryptedImageText));
        Helper.printImageWithRgbPixels(decryptedImage, imagePath);
        System.out.println("Image is decrypted in "+formatter.format((end - start) / 1000d)+" seconds.\n");
    }

}




