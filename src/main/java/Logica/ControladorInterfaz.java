package Logica;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultListModel;

import Interfaz.Editor;
import Objectes.Baralla;
import Objectes.Carta;
import daoImpl.BarallaMongoImpl;
import daoImpl.CartaExistImpl;

public class ControladorInterfaz {
	private boolean cartasCargadas;
	private boolean deckCargado;
	private CartaExistImpl cartaExistDB;
	private BarallaMongoImpl barallaMongoDB;
	private int deckValue = 0;
	private Baralla cargada;

	public ControladorInterfaz() {
		cartasCargadas = false;
		deckCargado = false;
	}

	public boolean isCartasCargadas() {
		return cartasCargadas;
	}

	public void setCartasCargadas(boolean cartasCargadas) {
		this.cartasCargadas = cartasCargadas;
	}

	public boolean isDeckCargado() {
		return deckCargado;
	}

	public void setDeckCargado(boolean deckCargado) {
		this.deckCargado = deckCargado;
	}

	public void randomDeck() {
		setDeckValue(0);
		boolean end = false;
		ArrayList<Carta> deckRandomBuild = new ArrayList<Carta>();
		ArrayList<Carta> listaCartasTMP = Editor.cartesArray;
		ArrayList<Carta> posiblesCandidatos = new ArrayList<Carta>();
		while (!end) {
			posiblesCandidatos = new ArrayList<Carta>();
			for (Carta a : listaCartasTMP) {
				if (a.getValue() <= 20 - getDeckValue()) {

					posiblesCandidatos.add(a);
				}
			}
			if (posiblesCandidatos.size() > 0) {
				// Obtenemos una carta aleatoria de todos los posibles candidatos
				Random rand = new Random();
				Carta tmp = posiblesCandidatos.get(rand.nextInt(posiblesCandidatos.size()));
				// Eliminamos la carta de las proximas posibles cartas
				listaCartasTMP.remove(tmp);
				deckRandomBuild.add(tmp);

				// Actualizamos el deckValue del mazo
				setDeckValue(getDeckValue() + tmp.getValue());

			} else {
				end = true;
			}
		}
		Editor.cartesArray = listaCartasTMP;
		Editor.deckArray = deckRandomBuild;
		// Actualizamos los DefaultModelList
		actualizarDLM();
	}

	public void randomDeck(ArrayList<Carta> mantener) {
		// Mismo metodo anterior de generacion de deck a excepcion de mantener las
		// cartas de antes
		setDeckValue(0);
		boolean end = false;
		ArrayList<Carta> deckRandomBuild = mantener;
		ArrayList<Carta> listaCartasTMP = Editor.cartesArray;
		ArrayList<Carta> posiblesCandidatos = new ArrayList<Carta>();
		for (Carta a : mantener) {
			setDeckValue(getDeckValue() + a.getValue());
			System.out.println(a.toString());
		}
		System.out.println(getDeckValue());
		if (getDeckValue() >= 20) {
			// No hacemos nada y fin de la funcion; se mantiene la seleccion normal
			Editor.showError("No s'ha generat cap baralla Random ja que no pots afegir mes cartes;Valor Baralla = 20");
		} else {

			while (!end) {
				if (posiblesCandidatos.size()>0) posiblesCandidatos.clear();
				for (Carta a : listaCartasTMP) {
					if (a.getValue() <= 20 - getDeckValue()) {
						posiblesCandidatos.add(a);
					}
				}

				if (posiblesCandidatos.size() > 0) {
					// Obtenemos una carta aleatoria de todos los posibles candidatos
					Random rand = new Random();
					Carta tmp = posiblesCandidatos.get(rand.nextInt(posiblesCandidatos.size()));
					// Eliminamos la carta de las proximas posibles cartas
					listaCartasTMP.remove(tmp);
					System.out.println(tmp.toString());
					deckRandomBuild.add(tmp);

					// Actualizamos el deckValue del mazo
					setDeckValue(getDeckValue() + tmp.getValue());

				} else {
					end = true;
				}
				if (getDeckValue() >= 20) {
					end = true;
				}
			}
			System.out.println("\n\n\n\n\\nCartas random");
			for (Carta tmp : deckRandomBuild) {
				System.out.println(tmp.toString());
			}

			Editor.cartesArray = listaCartasTMP;
			Editor.deckArray = deckRandomBuild;
			// Actualizamos los DefaultModelList
			for (int i = 0; i < Editor.cartesDLM.size(); ++i) {
				Editor.cartesDLM.removeElementAt(i);
			}
			for (int i = 0; i < Editor.deckDLM.size(); ++i) {
				Editor.deckDLM.removeElementAt(i);
			}
			actualizarDLM();

		}
	}

	public void cargarCardList() {
		cartaExistDB.getConexion().carregarCartes();
		Editor.cartesArray = cartaExistDB.getConexion().obtenirCartes();
		

	}
	private ArrayList<Carta> obtenirCardList() {
		cartaExistDB.getConexion().carregarCartes();
		return cartaExistDB.getConexion().obtenirCartes();
		

	}
	private void actualizarDLM() {
		Editor.cartesDLM.clear();
		Editor.deckDLM.clear();
		for (Carta a : Editor.cartesArray) {
			Editor.cartesDLM.addElement(a);
		}
		for (Carta a : Editor.deckArray) {
			Editor.deckDLM.addElement(a);
		}
		Editor.cartasList.setModel(Editor.cartesDLM);
		Editor.deckList.setModel(Editor.deckDLM);
	}

	public int getDeckValue() {
		return deckValue;
	}
	public void obtenirBaralla(String nom) {
		barallaMongoDB = new BarallaMongoImpl();
		Baralla cargada = barallaMongoDB.getDeckFromName(nom);
		ArrayList<Carta> tmp = cargada.getLlistaDeCartes();
		Editor.deckArray = tmp;
		Editor.cartesArray = obtenirCardList();
		//Eliminem les cartes del deck carregat de la llista de totes les cartes
		for (Carta eliminar : Editor.deckArray) {
			Editor.cartesArray.remove(eliminar);
		}
		actualizarDLM();
	}

	public void setDeckValue(int deckValue) {
		this.deckValue = deckValue;
	}
	public void guardarDeck(ArrayList<Carta> llistaDeck, String nom) {
		barallaMongoDB = new BarallaMongoImpl();
		Baralla a = new Baralla(nom, getDeckValue(), llistaDeck);
		if (barallaMongoDB.guardarBaralla(a)) {
			Editor.showError("S'ha guardat la baralla satisfactoriament");
			//Reutilitzem la funcio de editor, tot i que no sigui un error esplicitament
		}else {
			Editor.showError("La baralla ja existeix, cambieu el nom");
		}
	}

}
