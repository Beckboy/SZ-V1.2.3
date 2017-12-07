package com.junhsue.ksee.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hunter_J on 17/3/30.
 */

public class SHA256Encrypt {

    private static byte [] getHash(String password) {
      MessageDigest digest = null ;
      try {
        digest = MessageDigest. getInstance( "SHA-256");
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
      digest.reset();
      return digest.digest(password.getBytes());
    }
    public static String bin2hex(String strForEncrypt) {
      byte [] data = getHash(strForEncrypt);
      return String.format( "%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }
}
