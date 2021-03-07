package com.phr.cpabe.DataOwner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.mitu.cpabe.Cpabe.enc;

// Abdullah Arif
// Handle the server logic for the patient that will be encrypting their file

@RestController
public class DataOwner {
    static final String pubfile = "src/main/resources/cpabe_files/publickey.txt"; // Path to public key

/*
    @PostMapping(value = "/updatePublicKey")
     public static void updatePublicKey();
     // ** Need to way to make sure public key is up to date - get from TA or wherever it was dumped **
*/
    @Autowired
    DataOwner (){

    }
    // The path variable is the userID cause each user will be making a call to their own page
    @PostMapping(value = "/encryptFile/{userID}")
    public @ResponseBody boolean encryptFile(@PathVariable final String userID,
                @RequestParam(value = "policy") String policy,
                @RequestParam(value = "inputFile") MultipartFile inputFile ) {
        try {
            if (!inputFile.isEmpty()) {
                try {
                    // Get the file and send it as bytes and get the name to save
                   enc(pubfile, policy, inputFile.getBytes(), userID, inputFile.getOriginalFilename());
                   return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch ( NoSuchAlgorithmException
                | InvalidKeyException
                | IllegalBlockSizeException
                | NoSuchPaddingException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        return false; // failed to encrypt file
    }

}
