/* Abdullah Arif
*Program that will allow admins to manage attributes in the universe
*/
package com.phr.cpabe.AttributeManager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;

import org.springframework.web.bind.annotation.RestController;

// ** I have to make sure to remove access from any where - and only access from
@RestController
public class AttributeManager{
	// ** For now main as it will let me test using attributes
	/* ** When users register they send request to manager to be accepted - Upon accepting they send the user
	 *  attributes to the system and the system will add in any new attribute to the system
	 *  the reverse happens when deleting users (checks if there are any users left to a node then
	 * delete backwards recursively
	 */
//	@PostMapping(value = "/createUser") //
	public static void main(String[] args) { 
		// MongoClient mongoClient = new MongoClient( "localhost" , 200 );
		String mongo="mongodb+srv://PHRAdmin:ihAPwsj0TAU2csS7@phr-5c8no.mongodb.net/test?retryWrites=true&w=majority";
		MongoClientURI connectionString = new MongoClientURI(mongo);
		MongoClient mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("PHR");
		// get the collection you want to modify in our case it is the list of attributes
		MongoCollection<Document> collection = database.getCollection("test"); 
		Document doc = new Document("name", "MongoDB") // create document
                .append("type", "database") // append makes another field
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));
        collection.insertOne(doc); // insert one document in the document

        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());


	}
}