package com.phr.cpabe.RevocationServer;

import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.NoSuchDecryptionTokenFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.mitu.cpabe.Cpabe.mdecrypt;
//import static org.springframework.web.bind.annotation.RequestMethod.POST;

// Abdullah Arif
// Handle the logic for the server that will hold the attributes, users revocation of file for certain user
@RestController
public class RevocationServer {
    static final String pubfile = "src/main/resources/cpabe_files/publickey.txt"; // Path to public key

    // The decrypt share is based on the user not the file being decrypted
    @PostMapping(value = "/serverDecryptKey/{userID}",
                 produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public static byte[] decryptKey( @PathVariable final String userID,
                                     @RequestParam(value = "patientID") String patientID,
                                     @RequestParam(value = "encryptedFile") String encryptedFile){
        String share1 = getSharePath(userID); // Get the path to the share for the file
        // ** First, we will check the revocation server and make sure the user and his attributes arn't revoked **
        /* ** if revoked send a message stating what was revoked e.g. Users has been revoked from system all request
        will denied **  */
        try {
            return mdecrypt(pubfile, share1, encryptedFile, patientID);
        }
        catch (AttributesNotSatisfiedException e){
            RevocationServer.onIllegalArgumentException(e);
            e.printStackTrace();
            return "User does not have the correct privileges to access file.".getBytes(StandardCharsets.UTF_8);
        }
        catch(NoSuchDecryptionTokenFoundException e){
            RevocationServer.onIllegalArgumentException(e);
            e.printStackTrace();
            return "Attributes have been revoked".getBytes(StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            RevocationServer.onIllegalArgumentException(e);
            e.printStackTrace();

        }
        return "Internal server error:(".getBytes(StandardCharsets.UTF_8);
    }
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    private static void onIllegalArgumentException(Exception e) {}

    // **** Need to change so that that the share is dependent on the user- retrieve from database ****
    private static String getSharePath(String encryptedFile) {
        return "src/main/resources/cpabe_files/share1.txt";
    }
}
