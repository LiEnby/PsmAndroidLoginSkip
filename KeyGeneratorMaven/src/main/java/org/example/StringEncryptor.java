package org.example;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class StringEncryptor {
    private static final byte[] iv = {-126, -30, -6, -75, -99, -117, -66, 117, 39, -65, -126, -27, -12, 38, -99, 86};
    private static final byte[] salt = {-92, -102, -105, -123, 71, -33, 69, -39, -27, -32, 21, 33, 126, -81, 69, 59, 57, 29, -83, -15};

    private String ANDROID_ID = "";
    private int PSM_UID = 0;

    StringEncryptor(String androidId, int psmUid){
        ANDROID_ID = androidId;
        psmUid = psmUid;
    }

    private String getAndroidId(){
        return ANDROID_ID;
        //return Settings.Secure.getString(this.ctx.getContentResolver(), "android_id");;
    }

    private int getPsmUid() {
        return PSM_UID;
    }
    private String base64(byte[] data){
        byte[] base64 = Base64.getEncoder().encode(data);
        return new String(base64);
    }

    public String encryptString(String str) {
        byte[] data = str.getBytes();
        byte[] encryptedData = encrypt(data);
        if(encryptedData != null){
            byte[] encodedData = Arrays.copyOf(encryptedData, encryptedData.length + 2);
            encodedData[encodedData.length - 2] = 1;
            encodedData[encodedData.length - 1] = 1;

            return base64(encodedData);
        }
        return "";
    }

    private byte[] encrypt(byte[] input){
        try {
            Cipher cipher =generateKeyCipher(StringEncryptor.salt, StringEncryptor.iv, Cipher.ENCRYPT_MODE);
            if (cipher != null) {
                return cipher.doFinal(input);
            }
        } catch (BadPaddingException | IllegalBlockSizeException e) { }
        return null;

    }

    private Cipher generateKeyCipher(final byte[] salt, final byte[] iv, final int opmode) {
        try {
            final String androidId = getAndroidId();
            final String psmUid = String.valueOf(getPsmUid());
            if (androidId == null || psmUid == null) {
                throw new InvalidParameterException();
            }
            final SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("PBEWITHSHAAND256BITAES-CBC-BC");
            final char[] charArray = (androidId + psmUid + "     com.playstation.psstore    ").toCharArray();
            final PBEKeySpec keySpec = new PBEKeySpec(charArray, salt, 16, 256);
            Arrays.fill(charArray, '\0');
            final SecretKeySpec key = new SecretKeySpec(skeyFactory.generateSecret(keySpec).getEncoded(), "AES");
            final Cipher newCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            newCipher.init(opmode, key, new IvParameterSpec(iv), new SecureRandom());
            return newCipher;
        }
        catch (NoSuchPaddingException ex) {
            return null;
        }
        catch (InvalidAlgorithmParameterException ex2) {
            return null;
        }
        catch (InvalidKeyException ex3) {
            return null;
        }
        catch (InvalidKeySpecException ex4) {
            return null;
        }
        catch (NoSuchAlgorithmException ex5) {
            return null;
        }
    }
}
