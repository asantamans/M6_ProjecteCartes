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
	private MongoDatabase database;
	private MongoCollection<Document> collection;

	public BarallaMongoImpl() {
		// Empty
		// Fem servir els metodes per obrir i tancar conexio tota l'estona
	}

	private void protocolConect() {
		obrirConect();
		conectar();
	}

	private void protocolDesconect() {
		desconectar();
		tancarConect();
	}

	// Metodes per conectar i desconectar BBDD Mongo
	private void obrirConect() {
		connectionString = new MongoClientURI("mongodb://localhost:27017");// Segun COMPASS
		mongoClient = new MongoClient(connectionString);
	}

	private void tancarConect() {
		mongoClient.close();
		connectionString = null;
	}

	// Creem dos metodes per a que les funcions puguin obrir i tancar la conexio
	private void conectar() {
		database = mongoClient.getDatabase("Projecte3");
		collection = database.getCollection("baralles");
	}

	private void desconectar() {
		collection = null;
		database = null;
	}

	// Metodes IBaralla

	public boolean guardarBaralla(Baralla b1) {
		protocolConect();

		MongoCursor<Document> cursor = collection.find(Filters.eq("deckName", b1.getDeckName())).iterator();
		ObjectMapper mapper = new ObjectMapper();
		boolean transaccio = false;
		if (!cursor.hasNext()) {
			String barallaJson = null;
			// String barallaJson = ""; Da problemas; mejor en null
			try {
				barallaJson = mapper.writeValueAsString(b1);

			} catch (Exception e) {
				e.printStackTrace();
			}
			Document userDoc = Document.parse(barallaJson);
			collection.insertOne(userDoc);

			transaccio = true;
		}

		protocolDesconect();
		return transaccio;
	}

	public Baralla getDeckFromName(String nom) {
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
