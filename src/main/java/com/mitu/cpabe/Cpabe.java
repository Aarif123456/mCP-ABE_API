/*
 * 
 */
package com.mitu.cpabe;

import com.mitu.abe.*;
import com.mitu.cpabe.policy.LangPolicy;
import com.mitu.utils.exceptions.AttributesNotSatisfiedException;
import com.mitu.utils.exceptions.NoSuchDecryptionTokenFoundException;
import it.unisa.dia.gas.jpbc.Element;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cpabe {

	/**
	 * Setup.
	 * 
	 * @author Mitu Kumar Debnath
	 * @param publicKeyFile
	 *            the publicKeyFile
	 * @param masterKeyFile
	 *            the masterKeyFile
	 * @param attrs
	 *            the attrs
	 * @param parameterPath
	 * 			  the parameters needed by CPABE algorithm - such as the type of curve used and the prime numbers(key)
	 * 			  that are used for the generation for keys
	 */

	public static JsonObject setup(String publicKeyFile, String masterKeyFile, String[] attrs, String parameterPath) throws IOException
    {
    	var jsonObject = new JsonObject(); 
		byte[] pub_byte, msk_byte;

		AbePub pub = new AbePub();
		AbeMsk msk = new AbeMsk();
		Abe.setup(pub, msk, attrs, parameterPath);
		/* store public-key in JSON object to return to server*/
		pub_byte = SerializeUtils.serializeBswabePub(pub);
		jsonObject.addProperty("public_key", Base64.getEncoder().encodeToString(pub_byte));

		/* store AbeMsk into masterKeyFile */
		System.err.println("WARNING :In Cpabe.java - currently storing master key to local text file");
		msk_byte = SerializeUtils.serializeBswabeMsk(msk);
		Common.spitFile(masterKeyFile, msk_byte);

		// Utility.serializeObject(masterKeyFile, msk);
	}

	/**
	 * Keygen.
	 * 
	 * @param publicKeyFile
	 *            the publicKeyFile
	 * @param masterKeyFile
	 *            the masterKeyFile
	 * @param attr_str
	 *            the list of users attributes - I will change it so that trusted authority gets it from a database
	 */
	public static void keygen(String publicKeyFile, String masterKeyFile, String attr_str)
			throws IOException
    {
		AbePub pub;
		AbeMsk msk;

		byte[] pub_byte, msk_byte, prv_bytePart1, prv_bytePart2;

		/* get AbePub from publicKeyFile */
		pub_byte = Common.suckFile(publicKeyFile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		// ObjectInputStream inputStream = Utility.deSerializeObject(publicKeyFile);
		// pub = (AbePub) inputStream.readObject();

		/* get AbeMsk from masterKeyFile */
		msk_byte = Common.suckFile(masterKeyFile);
		msk = SerializeUtils.unserializeBswabeMsk(pub, msk_byte);

		// ObjectInputStream inputStream2 = Utility.deSerializeObject(masterKeyFile);
		// msk = (AbeMsk) inputStream2.readObject();

		String[] attr_arr = null;
		try {
			attr_arr = LangPolicy.parseAttribute(attr_str);
		} catch (Exception ex) {
			Logger.getLogger(Cpabe.class.getName()).log(Level.SEVERE, null, ex);
		}
		assert attr_arr != null;
		AbePrv prv = Abe.keygen(pub, msk, attr_arr);

		/* store AbePrv into prvfile */
		prv_bytePart1 = SerializeUtils.serializeBswabePrvPart1(prv.prv1);
		prv_bytePart2 = SerializeUtils.serializeBswabePrvPart2(prv.prv2);
		System.err.println("WARNING :In Cpabe.java - Keygen -currently sending share to local file instead of revocation share");
		Common.spitFile("src/main/resources/cpabe_files/share1.txt", prv_bytePart1);
		Common.spitFile("src/main/resources/cpabe_files/share2.txt", prv_bytePart2);
		// String[] shares = new String[2];
		// shares[0] = new String(prv_bytePart1); // ** Store share 1 for revocation server **
		// shares[1] = new String(prv_bytePart2); // ** Store share 2 for health professional **
		// return shares;
		// Utility.serializeObject(prvfilePart1, prv.prv1);
		// Utility.serializeObject(prvfilePart2, prv.prv2);
	}

	/**
	 * Enc.
	 * 
	 * @param publicKeyFile
	 *            the publicKeyFile
	 * @param policy
	 *            the policy
	 * @param inputFile
	 *            the input file as a byte array - as the data owner will be sending the file
	 * @param userID
	 *            the id of the data owner who is uploading their file
	 */
	public static void enc(String publicKeyFile, String policy, byte[] inputFile, String userID, String fileName)
            throws IOException,
			NoSuchAlgorithmException,
            InvalidKeyException,
            IllegalBlockSizeException,
            NoSuchPaddingException,
			BadPaddingException
    {

		AbePub pub;
		AbeCph cph;
		AbeCphKey keyCph;

		byte[] cphBuf;
		byte[] aesBuf;
		byte[] pub_byte;
		Element m;

		/* get AbePub from publicKeyFile */
		pub_byte = Common.suckFile(publicKeyFile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		// ObjectInputStream inputStream = Utility.deSerializeObject(publicKeyFile);
		// pub = (AbePub) inputStream.readObject();

		keyCph = Abe.enc(pub, policy);
		cph = keyCph.cph;
		m = pub.p.getGT().newElement();
		m = keyCph.key.duplicate();
		System.err.println("In encrypt: m = " + m.toString());

		if (cph == null) {
			System.err.println("Error happened in enc");
			System.exit(0);
		}

		cphBuf = SerializeUtils.bswabeCphSerialize(cph);

		// cphBuf = Utility.objectToByteArray(cph);

		/* read file that will be encrypted */
		// ObjectInputStream inputStream2 =
		// Utility.deSerializeObject(inputFile);
		// inputStream2.readFully(inputFile);
		aesBuf = AESCoder.encrypt(m.toBytes(), inputFile);

		Common.writeCpabeFile(fileName, userID, cphBuf, aesBuf);
	}

	/**
	 * mdecrypt - intermediate decryption process done by the revocation server. It checks the revocation list and
	 * only runs decryption if user has the appropriate permission
	 * 
	 * @param publicKeyFile
	 *            the publicKeyFile
	 * @param prvfilePart1
	 *            the prvfile part1
	 * @param encfile
	 *            the encfile
     * @param patientID
	 *            used to navigate to correct AWS folder ** might replace and moves part of file name **
	 */
	public static byte[] mdecrypt(String publicKeyFile, String prvfilePart1, String encfile, String patientID)
            throws AttributesNotSatisfiedException,
            NoSuchDecryptionTokenFoundException,
            IOException {

		byte[] cphBuf;
		byte[] prv_1_byte;
		byte[] pub_byte, m_dec_byte;
		byte[][] tmp;

		AbeCph cph;
		AbePub pub;
		AbePrvPart1 prvPart1;
		AbeMDec mDec;

		/* get AbePub from publicKeyFile */
		pub_byte = Common.suckFile(publicKeyFile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		// ObjectInputStream inputStream = Utility.deSerializeObject(publicKeyFile);
		// pub = (AbePub) inputStream.readObject();

		/* read ciphertext */
		tmp = Common.readCpabeFile(encfile, patientID);
		cphBuf = tmp[1];
		cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

		/* get AbePrvPart1 form prvfilePart1 */
		System.err.println("WARNING :In Cpabe.java - m decrypt - currently reading share locally instead of database");
		prv_1_byte = Common.suckFile(prvfilePart1);
		prvPart1 = SerializeUtils.unserializeBswabePrvPart1(pub, prv_1_byte);

		// ObjectInputStream inputStream2 =
		// Utility.deSerializeObject(prvfilePart1);
		// prvPart1 = (AbePrvPart1) inputStream2.readObject();

		mDec = Abe.m_dec(pub, prvPart1, cph);
		System.err.println("Returning file as byte array make sure spring is handling ");
		m_dec_byte = SerializeUtils.serializeBswabeMDec(mDec);
		return m_dec_byte; // Return the file represented as a byte array
//

		// Utility.serializeObject(m_decfile, mDec);
	}

	/**
	 * Dec.
	 *  @param publicKeyFile
	 *            the publicKeyFile
	 * @param prvfilePart2
	 *            the prvfile part2
     * @param encFileName
     *            the name of the encrypted file used to pull from AWS server
     * @param m_dec_byte
     *            the partially decrypted file as bytes
     * @param patientID
     *           Used to find the correct folder in AWS ** Might remove and put it as part of file name
     * @return the decrypted file as a byte array
     */
	public static byte[] dec(String publicKeyFile, String prvfilePart2, String encFileName, byte[] m_dec_byte,
                             String patientID) throws IOException, // Regular file IO exception
			IllegalBlockSizeException, // Problem from cp-abe jbr library
			InvalidKeyException, // Exception thrown if key was in wrong format
			BadPaddingException, // Exception comes from the AES encryption used under the hood
			NoSuchAlgorithmException, // Thrown in case the environment running the server doesn't have the requested algorithm
			NoSuchPaddingException // Thrown in case a padding is requested but does not exist in environment
	{

		byte[] aesBuf, cphBuf;
        byte[] prv_2_byte;
		byte[] pub_byte;
		byte[][] tmp;

		AbeCph cph;
		AbePub pub;
		AbePrvPart2 prvPart2;
		AbeMDec mDec =null;

		/* get AbePub from publicKeyFile */
		System.err.println("WARNING :In Cpabe.java - decrypt public key is being read locally");
		pub_byte = Common.suckFile(publicKeyFile);
		pub = SerializeUtils.unserializeBswabePub(pub_byte);

		// ObjectInputStream inputStream = Utility.deSerializeObject(publicKeyFile);
		// pub = (AbePub) inputStream.readObject();

		/* read ciphertext */
		tmp = Common.readCpabeFile(encFileName, patientID);
		aesBuf = tmp[0];
		cphBuf = tmp[1];
		cph = SerializeUtils.bswabeCphUnserialize(pub, cphBuf);

		/* get AbePrvPart2 form prvfilePart2 */
		System.err.println("WARNING :In Cpabe.java -decrypt - currently reading share locally instead of database");
		prv_2_byte = Common.suckFile(prvfilePart2);
		prvPart2 = SerializeUtils.unserializeBswabePrvPart2(pub, prv_2_byte);

		// ObjectInputStream inputStream2 =
		// Utility.deSerializeObject(prvfilePart2);
		// prvPart2 = (AbePrvPart2) inputStream2.readObject();

//		m_dec_byte = Common.suckFile(m_decfile);
	try{
		mDec = SerializeUtils.unserializeBswabeMDec(pub, m_dec_byte);
	}catch(Exception e){
		e.printStackTrace();
	}



		// ObjectInputStream inputStream3 =
		// Utility.deSerializeObject(m_decfile);
		// mDec = (AbeMDec) inputStream3.readObject();
		byte[] plt;
		Element m = pub.p.getGT().newElement();
		m = Abe.dec(pub, prvPart2, cph, mDec).duplicate();
		plt = AESCoder.decrypt(m.toBytes(), aesBuf);
		// Common.spitFile( "src/main/resources/cpabe_files/decrypted.png", plt);
		return  plt;
	}


}