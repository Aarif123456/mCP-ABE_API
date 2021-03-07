package com.phr.cpabe.Users;
// Abdullah Arif
// Handle the logic of the health care professional

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.mitu.cpabe.Cpabe.dec;


@RestController
public class Users {
    static final String pubfile = "src/main/resources/cpabe_files/publickey.txt"; // Path to public key
    // The path may change but it will essentially be hardcoded for user - It can updated upon request
    static final String share2 = "src/main/resources/cpabe_files/share2.txt";
    /* ** change so that public request is a request data owner make to revocation server then it automatically makes
        another request to fully decrypt file
     */

/*    @PostMapping(value = "/downloadDecrypted/{userID}",
                  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public static byte[] downloadDecrypted(@PathVariable final String userID,
                                     @RequestParam(value = "patientID") String patientID,
                                     @RequestParam(value = "encryptedFile") String encryptedFileName){
        File file = restTemplate.execute(FILE_URL, HttpMethod.POST, null, clientHttpResponse -> {
            File ret = File.createTempFile("download", "tmp");
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            return ret;
        });
         
        Assert.assertNotNull(file);
        Assertions
          .assertThat(file.length())
          .isEqualTo(contentLength);
    }*/
    // ** Create the real function data owner will use here ** the one to automatically make the two calls
    @PostMapping(value = "/decryptKey/{userID}",
                 produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public static ResponseEntity<byte[]> decryptKey(@PathVariable final String userID,
                                                    @RequestParam(value = "patientID") String patientID,
                                                    @RequestParam(value = "encryptedFile") String encryptedFileName,
                                                    @RequestParam("mDecryptedFile") MultipartFile mDecryptedFile

    ){


        System.err.println("User id is " +userID);
        System.err.println("mDecryptedFile " +mDecryptedFile.getName());
        System.err.println("patient " +patientID);
        System.err.println("encrypted file name " +encryptedFileName);
        try {
           
            var fileBytes = dec(pubfile, share2, encryptedFileName, mDecryptedFile.getBytes(), patientID);
            return ResponseEntity.ok()
                                 .header("Content-Disposition", "attachment; filename=" + encryptedFileName)
                                 .contentLength(fileBytes.length)
                                 .contentType(MediaType.valueOf(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                                 .body(fileBytes);
            
        } catch (NoSuchAlgorithmException |
                 IOException |
                 IllegalBlockSizeException |
                 InvalidKeyException |
                 BadPaddingException |
                 NoSuchPaddingException e) {
            e.printStackTrace();
           
//            return "Unable to create/update user Attribute";
            return ResponseEntity.badRequest().body(e.toString().getBytes(StandardCharsets.UTF_8));
        }


    }

   
}
