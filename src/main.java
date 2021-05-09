import algorithm.AES;
import key.KeyPair;
import key.PrivateKey;
import key.PublicKey;
import key.SymmetricKey;
import util.Constants;
import util.Helper;
import util.ImageInfo;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

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

       /* System.out.println("\n*******************************************************************************************************************\n");

        String cipherTextK1 = publicKey.encrypt(k1.getSymetricKeyAsHex());
        System.out.print("K1 is encrypted with Ka+ . The ciphertext is: ");
        System.out.println(cipherTextK1);

        String plainTextK1 = privateKey.decrypt(cipherTextK1);
        System.out.print("K1 is decrypted with Ka- . The plaintext is: ");
        System.out.println(plainTextK1);


        System.out.println("\n*******************************************************************************************************************\n");

        String cipherTextK2 = publicKey.encrypt(k2.getSymetricKeyAsHex());
        System.out.print("K2 is encrypted with Ka+ . The ciphertext is: ");
        System.out.println(cipherTextK2);

        String plainTextK2 = privateKey.decrypt(cipherTextK2);
        System.out.print("K2 is decrypted with Ka- . The plaintext is: ");
        System.out.println(plainTextK2);

        System.out.println("\n*******************************************************************************************************************\n");

        System.out.println("Message:");
        System.out.println(Constants.message);

        String hashedMessage = Helper.hashWithSHA256(Constants.message);
        System.out.print("\nApplied SHA256 to message. H(m): ");
        System.out.println(hashedMessage);

        String digitalSignature = privateKey.generateDigitalSignature(hashedMessage);
        System.out.print("\nH(m) encrypt with Ka-. Digital Signature: ");
        System.out.println(digitalSignature);

        publicKey.verifyDigitalSignature(digitalSignature, Constants.message);*/

        System.out.println("Image Reading...");
        ImageInfo imageInfo = Helper.getRGBvaluesOfImage(Constants.IMAGES_FOLDER_PATH+Constants.PLAIN_IMAGE_NAME);
        String[] pixelsAsByte = Helper.convertIntArrayToByteArray(imageInfo.getPixels());

        System.out.println("Image Encrypting with 128 Bit K1 key in CBC mode...");
        String[] cipherTextAsByte128CBC = AES.streamCipherEncryption(pixelsAsByte, k1, Constants.Method.CBC);
        ImageInfo cipherImage128CBC = new ImageInfo(imageInfo.getHeight(), imageInfo.getWidth(), Helper.convertByteArrayToIntArray(cipherTextAsByte128CBC));
        Helper.printImageWithRgbPixels(cipherImage128CBC, Constants.METHOD_CBC_KEY_128_PATH+Constants.ENCRYPTED_IMAGE_NAME+"_v1");
        System.out.println("Encrypted image is printed.");

        System.out.println("Initial Vector is changed and again image Encrypting with 128 Bit K1 key in CBC mode...");
        String[] cipherTextAsByte128CBCv2 = AES.streamCipherEncryption(pixelsAsByte, k1, Constants.Method.CBC);
        ImageInfo cipherImage128CBCv2 = new ImageInfo(imageInfo.getHeight(), imageInfo.getWidth(), Helper.convertByteArrayToIntArray(cipherTextAsByte128CBCv2));
        Helper.printImageWithRgbPixels(cipherImage128CBCv2, Constants.METHOD_CBC_KEY_128_PATH+Constants.ENCRYPTED_IMAGE_NAME+"_v2");
        System.out.println("Encrypted image version 2 is printed.");

        System.out.println("Image Decrypting with 128 Bit K1 key in CBC mode...");
        String[] decryptedImageAsByte128CBC = AES.streamCipherDecryption(cipherTextAsByte128CBC, k1, Constants.Method.CBC);
        ImageInfo decryptedImage128CBC = new ImageInfo(imageInfo.getHeight(), imageInfo.getWidth(), Helper.convertByteArrayToIntArray(decryptedImageAsByte128CBC));
        Helper.printImageWithRgbPixels(decryptedImage128CBC, Constants.METHOD_CBC_KEY_128_PATH+Constants.DECRYPTED_IMAGE_NAME);
        System.out.println("Decrypted image is printed.");

        System.out.println("Image Encrypting with 256 Bit K2 key in CBC mode...");
        String[] cipherTextAsByte256CBC = AES.streamCipherEncryption(pixelsAsByte, k2, Constants.Method.CBC);
        ImageInfo cipherImage256CBC = new ImageInfo(imageInfo.getHeight(), imageInfo.getWidth(), Helper.convertByteArrayToIntArray(cipherTextAsByte256CBC));
        Helper.printImageWithRgbPixels(cipherImage256CBC, Constants.METHOD_CBC_KEY_256_PATH+Constants.ENCRYPTED_IMAGE_NAME);
        System.out.println("Encrypted image is printed.");

        System.out.println("Image Decrypting with 256 Bit K2 key in CBC mode...");
        String[] decryptedImageAsByte256CBC = AES.streamCipherDecryption(cipherTextAsByte256CBC, k2, Constants.Method.CBC);
        ImageInfo decryptedImage256CBC = new ImageInfo(imageInfo.getHeight(), imageInfo.getWidth(), Helper.convertByteArrayToIntArray(decryptedImageAsByte256CBC));
        Helper.printImageWithRgbPixels(decryptedImage256CBC, Constants.METHOD_CBC_KEY_256_PATH+Constants.DECRYPTED_IMAGE_NAME);
        System.out.println("Decrypted image is printed.");

        System.out.println("Image Encrypting with 256 Bit K2 key in CTR mode...");
        String[] cipherTextAsByte256CTR = AES.streamCipherEncryption(pixelsAsByte, k2, Constants.Method.CTR);
        ImageInfo cipherImage256CTR = new ImageInfo(imageInfo.getHeight(), imageInfo.getWidth(), Helper.convertByteArrayToIntArray(cipherTextAsByte256CTR));
        Helper.printImageWithRgbPixels(cipherImage256CTR, Constants.METHOD_CTR_PATH+Constants.ENCRYPTED_IMAGE_NAME);
        System.out.println("Encrypted image is printed.");

        System.out.println("Image Decrypting with 256 Bit K2 key in CTR mode...");
        String[] decryptedImageAsByte256CTR = AES.streamCipherDecryption(cipherTextAsByte256CTR, k2, Constants.Method.CTR);
        ImageInfo decryptedImage256CTR = new ImageInfo(imageInfo.getHeight(), imageInfo.getWidth(), Helper.convertByteArrayToIntArray(decryptedImageAsByte256CTR));
        Helper.printImageWithRgbPixels(decryptedImage256CTR, Constants.METHOD_CTR_PATH+Constants.DECRYPTED_IMAGE_NAME);
        System.out.println("Decrypted image is printed.");

    }

}




