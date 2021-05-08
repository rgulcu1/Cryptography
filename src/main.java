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

        String[] abc = {"was"};
        AES.encrypt(abc, k2, Constants.Method.CTR);


       /* ImageInfo imageInfo = Helper.getRGBvaluesOfImage(Constants.PLAIN_IMAGE_NAME);

        String[] pixelsAsByte = Helper.convertIntArrayToByteArray(imageInfo.getPixels());

        Helper.printImageWithRgbPixels(imageInfo, "ridoNew.jpg");*/


    }

}




