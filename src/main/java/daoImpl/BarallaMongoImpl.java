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
import com.mongodb.util.JSON;

import Objectes.Baralla;
import idao.IBaralla;

public class BarallaMongoImpl implements IBaralla {

	//http://mongodb.github.io/mongo-java-driver/3.4/driver/getting-started/quick-start/
	private MongoClientURI connectionString;
	private MongoClient mongoClient;
	private DB database;
	private DBCollection collection;

	public BarallaMongoImpl() {
		//Empty
		//Fem servir els metodes per obrir i tancar conexio tota l'estona
	}
	
	//Metodes per conectar i desconectar BBDD Mongo
	private void obrirConect() {
		connectionString = new MongoClientURI("mongodb://localhost:27017");//Segun COMPASS
		mongoClient = new MongoClient(connectionString);
	}

	private void tancarConect() {
		mongoClient.close();
		connectionString = null;
	}

	// Creem dos metodes per a que les funcions puguin obrir i tancar la conexio
	private void conectar() {
		database = mongoClient.getDB("Projecte3");
		collection = database.getCollection("baralles");
	}

	private void desconectar() {
		collection = null;
		database = null;
	}
	
	//Metodes IBaralla

	public boolean guardarBaralla(Baralla b1) {
		obrirConect();
		conectar();
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", b1.getDeckName());
		DBCursor cursor = collection.find(searchQuery);
		boolean transaccio = false;
		if(cursor.size()==0) {
			DBObject obj=null;
			ObjectMapper mapper = new ObjectMapper();
			try {
				obj = (DBObject) JSON.parse(mapper.writeValueAsString(b1));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			collection.insert(obj);
			
			transaccio= true;
		}
		
		desconectar();
		tancarConect();
		return transaccio;
	}

	public boolean actualitzarBaralla(Baralla b1) {
		obrirConect();
		conectar();

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", b1.getDeckName());
		DBCursor cursor = collection.find(searchQuery);

		boolean transaccio = false;
		if (cursor.size() != 0) {
			DBObject obj = null;
			try {
				obj = (DBObject) JSON.parse(new ObjectMapper().writeValueAsString(b1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			collection.insert(obj);
		
			transaccio = true;
		}

		desconectar();
		tancarConect();
		return transaccio;
	}

	public Baralla getDeckFromName(String nom) {
		obrirConect();
		conectar();

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", nom);
		DBCursor cursor = collection.find(searchQuery);
		Baralla searchDeck;
		
		try {
			DBObject object = cursor.next();
			searchDeck = new Gson().fromJson(object.toString(), Baralla.class);
		} catch (NoSuchElementException e) {
			searchDeck = null;
		}

		desconectar();
		tancarConect();
		return searchDeck;

	}
	

}
