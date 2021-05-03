Endpoint available at: https://us-central1-mcpabe.cloudfunctions.net/test  \
Argument: method (type of method described below) - each method has its own list of required arguments

## Description
m-CP-ABE - based on the paper by https://link.springer.com/chapter/10.1007/978-3-642-10838-9_23 and original code by Mitu kumar/
The CP-ABE implementation comes from [Junwei Wang](https://github.com/junwei-wang/cpabe/)
The following is in API that implements the mCp-ABE protocol 
It supports both get and post request


# API
### GenerateProperties
Input: type, parameterMap(optional)\
Output: properties\
Explanation: We have four types of curve you can use ["a", "a1", "e", "f"]. Type a has been tested the most, so it is recommended to use it. Endpoint returns a string which represents the JSON string of the property map.\
The property map holds the parameter needed to generate the key, if you don't pass in a map the default values are used as outlined below. 

|Type| Inputs with defaults  | 
|----|:---------------------:| 
| A  | rBits=160, qBits=512  | 
| A1 | numPrimes=2, bits=512 |
| E  | rBits=160, qBits=512  |
| F  | rBits=160             |
More info available [here](http://gas.dia.unisa.it/projects/jpbc/docs/ecpg.html)

### Setup
Input: attributeUniverse, properties(optional)\
Output: publicKey, masterKey\
Explanation: Takes the set of all attributes and creates and return a master key and public key. All properties will eventually be converted to be lower-case so make sure the attributes are lower-case.

### Keygen
Input: publicKey, masterKey, userAttribute, properties(optional)\
Output: share1, share2\
Explanation: Uses the public key and master key to create 2 private keys for the doctor to use when decrypting files. One key should be in a database where the doctor will have access. The other should be stored in a database accessible by the revocation server

### Encrypt
Input: publicKey,policy, inputFile, properties(optional)\
Output: encryptedFile\
Explanation: Uses the public key and encryption policy to return the encrypted file in the storage server (represented in bytes)

### HalfDecrypt
Input: publicKey, share1, encryptedFile, properties(optional)\
Output: mDecryptedFile\
Explanation: If the user has not been revoked, then uses the userID to find the first half of the user's key and then uses that to half decrypt the file and return it.

### Decrypt
Input: publicKey, share2, encryptedFile, mDecryptedFile, properties(optional)\
Output: decryptedFile\
Explanation: Ideally we would use the professional_id to get the second part of the users' decryption key. 
Then makes a call to the RS to get the half decrypted file. 
If the RS does not respond with an error, it will then take the half decrypted file and 
decrypt the rest of it with the second part of the decryption key. Then it will return the decrypted file to the user.
However, currently it does not make automatic calls to the RS server. Instead, it functions as a straightforward API call.


