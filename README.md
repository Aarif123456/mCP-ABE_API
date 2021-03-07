m-CP-ABE - based on the paper by https://link.springer.com/chapter/10.1007/978-3-642-10838-9_23 and original code by 
Mitu kumar
The following is in API that implements the mCp-ABE protocol 
It support both get and post request
*Note: initialParameter is supported by the business logic but not by request*
#API
###Setup
Input: attributeUniverse, initialParameters(optional)
Output: publicKey, masterKey
Explanation: Takes the set of all attributes and creates and return a master key and public key

###Keygen
Input: publicKey, masterKey, userAttribute, initialParameters(optional)
Output: share1, share2
Explanation: Uses the public key and master key to create 2 private keys for the doctor to use when decrypting files. One key is should be in a database where the doctor will have access and the other is should be stored in a database accessible by the revocation server

###Encrypt
Input: publicKey,policy, inputFile, initialParameters(optional)
Output: encryptedFile
Explanation: Uses the public key and encryption policy to return the encrypted file in the storage server (represented in bytes)

###HalfDecrypt
Input: publicKey, share1, encryptedFile, professionalId, initialParameters(optional)
Output: mDecryptedFile
Explanation: If the user has not been revoked, then uses the userID to find the first half of the user's key and then uses that to half decrypt the file and return it.

###Decrypt
Input: publicKey, share2, encryptedFile, mDecryptedFile, professionalId, initialParameters(optional)
Output: decryptedFile
Explanation: Ideally we would use the professional_id to get the second part of the users decryption key. 
Then makes a call to the RS to get the half decrypted file. 
If the RS does not respond with an error, it will then take the half decrypted file and 
decrypt the rest of it with the second part of the decryption key. Then it will return back the decrypted file to the user.
However, currently it does not make automatic calls to the RS server. Instead it function as a straightforward API call.


