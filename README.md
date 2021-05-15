[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://lbesson.mit-license.org/)
![Vulnerability count](https://img.shields.io/snyk/vulnerabilities/github/Aarif123456/mCP-ABE_API)
[![Coverity Scan Build Status](https://img.shields.io/coverity/scan/23119.svg)](https://scan.coverity.com/projects/aarif123456-mcp-abe_api)
[![BCH compliance](https://bettercodehub.com/edge/badge/Aarif123456/mCP-ABE_API?branch=cpabe)](https://bettercodehub.com/)

# Description
[CP-ABE (Ciphertext-Policy Attribute-Based Encryption)](https://www.cs.utexas.edu/~bwaters/publications/papers/cp-abe.pdf) is an implementation of attribute-based encryption, where the access is dependent on a given policy. For example, a company like Google might want to make sure a certain document is only accessible by employees. But there might be certain files they only want to be accessible to people with a minimum level of seniority and who belong to a specific department. They might also want those files accessible to specific external auditors. So, their encryption files for their special files might look something like   

    (company:Google AND Department:searching AND level>6) OR (Profession:Auditor AND (name:Andrew-Guinn OR name:Teresa-Green))

The access policy in our program follows the same principle but has the following structure "attribute_1 attribute_2 ... attribute_n iofn" - where "i" is the number of attributes that need to be satisfied to grant access. 

So, the above policy would be expressed as the following

    ((company:Google Department:searching 2of2) (level:6 level:7 level:8 level:9 1of4) 2of2) (Profession:Auditor (name:Andrew-Guinn name:Teresa-Green 1of2) 2of2) 1of2

This encryption would be used in addition to some other access control So, even if the file was somehow compromised the information would not be compromised. 
The CP-ABE implementation comes from [Junwei Wang](https://github.com/junwei-wang/cpabe/)

The API supports GET and POST requests. However, POST requests are recommended for the encryption and decryption calls because you will be uploading a file.

# Quick Start :rocket:
Endpoint available at: https://northamerica-northeast1-mcpabe.cloudfunctions.net/cpabe \
Argument: method (type of method described below) - each method has its list of required arguments
You can look [here](https://github.com/Aarif123456/image_repository_api/blob/main/repository/encryption/callApi.php) for an example of it being used 

## API Detail :artificial_satellite:
### Generate Properties
Input: type, parameterMap(optional)\
Output: properties\
Explanation: We have four types of curve you can use ["a", "a1", "e", "f"]. The endpoint returns a string that represents the JSON string of the property map.\
The property map holds the parameter needed to generate the key, if you don't pass in a map the default values are used as outlined below. 

|Type| Inputs with defaults  | 
|----|:---------------------:| 
| A  | rBits=160, qBits=512  | 
| A1 | numPrimes=2, bits=512 |
| E  | rBits=160, qBits=512  |
| F  | rBits=160             |
More info available [here](http://gas.dia.unisa.it/projects/jpbc/docs/ecpg.html)

### Setup
Input: properties(optional)\
Output: publicKey, masterKey\
Explanation: Gives us the keys required for the encryption. The public key is used to encrypt files with a given policy. The master key is needed to create the private decryption keys for users.

### Keygen
Input: publicKey, masterKey, userAttributes, properties(optional)\
Output: privateKey\
Explanation: Uses the public key and master key to create a private key for users based on their attributes. 

*Note: user attributes must be in the shape "attributeType1:property1 attributeType2:property2"* Every property must be attached to a property type.

### Encrypt
Input: publicKey, policy, inputFile, properties(optional)\
Output: encryptedFile\
Explanation: Uses the public key and encryption policy to return the encrypted file in the storage server (represented in bytes)\
*Check out test classes to some idea of how the policies are supposed to be formed. If it still doesn't make sense then go* [here](https://github.com/junwei-wang/cpabe/)

### Decrypt
Input: publicKey, privateKey, encryptedFile, properties(optional)\
Output: decryptedFile\
Explanation: Use the users key and decrypt the encrypted file if they have access.


