package daoImpl;

import java.util.NoSuchElementException;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;

import Objectes.Baralla;
import idao.IBaralla;

public class BarallaMongoImpl implements IBaralla {

	// http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
	private MongoClientURI connectionString;
	private MongoClient mongoClient;
	private MongoDatabase database; //DBObject esta deprecated
	private MongoCollection<Document> collection;

	public BarallaMongoImpl() {
		// Empty
		// Fem servir els metodes per obrir i tancar conexio tota l'estona
	}

	private void protocolConect() {
		//Funcio per evitar redundancia de aquestes dos funcions 
		obrirConect();
		conectar();
	}

	private void protocolDesconect() {
		//Funcio per evitar redundancia de aquestes dos funcions 
		desconectar();
		tancarConect();
	}

	// Metodes per conectar i desconectar BBDD Mongo
	private void obrirConect() {
		//Iniciem les variables per obrir la conect amb  la nostra bbdd
		connectionString = new MongoClientURI("mongodb://localhost:27017");// Segun COMPASS
		mongoClient = new MongoClient(connectionString);
	}

	private void tancarConect() {
		//Tanquem les variables encarregades de obrir la conexió
		mongoClient.close();
		connectionString = null;
	}

	// Creem dos metodes per a que les funcions puguin obrir i tancar la conexio
	private void conectar() {
		//Establim la conexió amb mongo 
		database = mongoClient.getDatabase("Projecte3");//Segons bbdd
		collection = database.getCollection("baralles");//Segons la coleció
	}

	private void desconectar() {
		//Posem a null la bbdd de mongo i colecip
		collection = null;
		database = null;
	}

	// Metodes IBaralla

	public boolean guardarBaralla(Baralla b1) {
		/*Funcio per guardar la baralla b1
		 *Primer buscarem la baralla a la bbdd ; Si existeix cursor == 1 o te hasNext
		 *en cas contrari, la baralla no existeix; la inserim
		 * 
		 */
		protocolConect();

		MongoCursor<Document> cursor = collection.find(Filters.eq("deckName", b1.getDeckName())).iterator();
		ObjectMapper mapper = new ObjectMapper();
		boolean transaccio = false;
		if (!cursor.hasNext()) {
			String barallaJson = null;
			// String barallaJson = ""; Da problemas; mejor en null
			try {
				barallaJson = mapper.writeValueAsString(b1);//Passem el objecte a format Json

			} catch (Exception e) {
				e.printStackTrace();
			}
			//Pasemla baralla en format JSON al document
			Document userDoc = Document.parse(barallaJson);
			collection.insertOne(userDoc);//Insertem a la bbdd

			transaccio = true;//transacció satisfactoria
		}

		protocolDesconect();
		return transaccio;//Retornarem true o false en funció si s'ha pogut o no insertar la baralla a la bbdd
	}

	public Baralla getDeckFromName(String nom) {
		
		/*Funció per buscar una baralla a la bbdd
		 * Retornem la baralla si la trobem, en cas contrari
		 * retorna una baralla = null;
		 */
		protocolConect();

		MongoCursor<Document> cursor = collection.find(Filters.eq("deckName", nom)).iterator();
		Baralla barallaBuscar;

		try {
			Document document = cursor.next();
			barallaBuscar = new Gson().fromJson(document.toJson(), Baralla.class);
		} catch (NoSuchElementException e) {
			barallaBuscar = null;
		}

		protocolDesconect();
		return barallaBuscar;

	}

}
