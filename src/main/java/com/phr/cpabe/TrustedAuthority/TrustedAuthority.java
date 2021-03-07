package com.phr.cpabe.TrustedAuthority;
// Abdullah Arif
// Spring class that will be used in the trusted authority server to create and handle creation of master and private key
// The server will access the Neo4j server with the list of all possible attributes

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.mitu.cpabe.Cpabe.keygen;
import static com.mitu.cpabe.Cpabe.setup;


@RestController
public class TrustedAuthority {
    static final String pubfile = "src/main/resources/cpabe_files/publickey.txt"; // Path to public key
    static final String mskfile = "src/main/resources/cpabe_files/masterkey.txt"; // Path to master key
    static final String parameterPath = "src/main/resources/a.properties"; // key to parameter used for the setup


    // At start-up set up the server
//    public void main(String[] args) {
//        updateSystemKeys();
//    }

    // ** Will get all possible attributes from the neo4j server when completed **
    private String[] getAttributeUniverse(){
        return new String[] { "doctor", "windsor", "detroit", "heart_surgeon", "emergency"};
    }

    // Function to get update the master key and public parameter as
    @PostMapping(value = "/updateSystemKeys")
    public String updateSystemKeys(){
        String[] attributeUniverse = getAttributeUniverse();
        try {
            setup(pubfile, mskfile, attributeUniverse, parameterPath);
            return "Congrats you set up the trusted authority server";

        } catch (IOException ex) {
            ex.printStackTrace();
        }
         return "Failed to set up trusted authority";
//        return new ResponseEntity<>("Failed to setup key", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/upsertKey/{userID}")
    public String upsertKey( @PathVariable final String userID,
                                    @RequestParam(value = "userAttribute") String userAttribute) {
        try {
            keygen(pubfile, mskfile, userAttribute);
//            System.err.println("TA works!"); // ** debug code **
            return "Successfully created/updated user attributes";

        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.err.println("TA failed:(");
        return "Unable to create/update user Attribute";
    }
    // ** Add the first share to the database that the revocation server will use to partially decrypt user file **
    /* ** If we are using the same kind of database to store the database- (prolly SQL) I might use the same function
    and just specify what server the share goes
    Not doing it over instead GOING to do it where shares are made - why risk getting the message intercepted
     */
}
