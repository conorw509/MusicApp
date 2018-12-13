/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encrypted.hash;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author conor
 */
public class PasswordEncrypt {

private final static String SALT = "your_salt_string_here";

//passing in password
public static String encryptPassword(String password) {
    String result = null;
    MessageDigest md;

    try {
        // usng SHA-1 algorithm passing using the message digest to digest byte size data 
        md = MessageDigest.getInstance("SHA-1");
        byte[] hash1 = md.digest(password.getBytes()); //first password

        md.reset();
        byte[] hash2 = md.digest(hash1); //2nd password

        md.reset();
        md.update(SALT.getBytes());
        md.update(hash2);

        byte[] digest = md.digest();
        for (int i = 0; i < digest.length; i++) {
            digest[i] = (byte) (digest[i] ^ hash1[i]);
        }   

        result = String.format("%0" + (digest.length << 1) + "x",new BigInteger(1,digest));     

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    return result;
}
    
    }
