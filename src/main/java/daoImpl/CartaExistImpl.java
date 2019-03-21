package daoImpl;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.XMLResource;

import com.google.gson.Gson;

import Objectes.Carta;
import idao.ICarta;

public class CartaExistImpl implements ICarta {
	// Model singleton
	private static CartaExistImpl cartaExistImpl;

	// Conexió amb eXIST
	private Class cExist;
	private Database dbExist;
	private final String driver = "org.exist.xmldb.DatabaseImpl";
	private final String uri = "xmldb:exist://localhost:8844/exist/xmlrpc/db/";
	private final String resourceName = "card_collection.xml";

	private Collection collection;
	private XMLResource xmlResource;
	private ArrayList<Carta> llistaCartes;

	private JSONObject jsonObject;
	private JSONArray llistatCartesParseades;

	// Model Singleton
	public static CartaExistImpl getConexion() {
		if (cartaExistImpl == null) {
			cartaExistImpl = new CartaExistImpl();
			return cartaExistImpl;
		}
		return cartaExistImpl;
	}

	private CartaExistImpl() {
		conectar();
		// carregarCartes();
	}

	private void conectar() {
		try {

			cExist = Class.forName(driver);
			dbExist = (Database) cExist.newInstance();
			dbExist.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(dbExist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void desconectar() {
		cExist = null;
		dbExist = null;
	}

	public void carregarCartes() {
		try {
			
			collection = DatabaseManager.getCollection(uri);
			xmlResource = (XMLResource) collection.getResource(resourceName);
			
			jsonObject = XML.toJSONObject((String) xmlResource.getContent());
			JSONArray llistatCartesParseades = jsonObject.getJSONObject("cards").getJSONArray("card");
			llistaCartes = new ArrayList<Carta>();
			
			for (int i = 0; i < llistatCartesParseades.length(); i++) {
				Carta tmp = new Gson().fromJson(llistatCartesParseades.getJSONObject(i).toString(), Carta.class);
				llistaCartes.add(tmp);
			}
			llistatCartesParseades = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Carta> obtenirCartes() {
		// TODO Auto-generated method stub
		return llistaCartes;
	}
	
	//Solo para pruebas
	public static void main(String[] args) {
		CartaExistImpl.getConexion().carregarCartes();
		ArrayList<Carta> a = CartaExistImpl.getConexion().obtenirCartes();
		for (Carta b : a) {
			System.out.println(b.toString());
		}
		
	}

}
