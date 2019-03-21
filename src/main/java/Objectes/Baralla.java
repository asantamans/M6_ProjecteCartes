package Objectes;

import java.util.ArrayList;

public class Baralla {
	
	//Mantenim el nom de les variables tal el format de mongo
	private String deckName;
	private int deckValue;
	private ArrayList<Carta> llistaDeCartes;
	public Baralla(String deckName, int deckValue, ArrayList<Carta> llistaDeCartes) {
		super();
		this.deckName = deckName;
		this.deckValue = deckValue;
		this.llistaDeCartes = llistaDeCartes;
	}
	public String getDeckName() {
		return deckName;
	}
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	public int getDeckValue() {
		return deckValue;
	}
	public void setDeckValue(int deckValue) {
		this.deckValue = deckValue;
	}
	public ArrayList<Carta> getLlistaDeCartes() {
		return llistaDeCartes;
	}
	public void setLlistaDeCartes(ArrayList<Carta> llistaDeCartes) {
		this.llistaDeCartes = llistaDeCartes;
	}
	
	
}
