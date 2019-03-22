package Logica;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import Interfaz.Editor;
import Objectes.Baralla;
import Objectes.Carta;
import daoImpl.BarallaMongoImpl;
import daoImpl.CartaExistImpl;

public class ControladorInterfaz {
	// Booleanos para controlar si las cartas han estado cargadas o hay un deck
	// cargado
	private boolean cartasCargadas;
	private boolean deckCargado;
	// Conectors a mongoDB i eXistDB
	private CartaExistImpl cartaExistDB;
	private BarallaMongoImpl barallaMongoDB;

	private int deckValue = 0; // Valor per representar en temps real el valor de la baralla en creacio
	private Baralla cargada; // Null si no s'ha carregat cap baralla

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

	public Baralla getBarallaCargada() {
		return cargada;
	}
	

	public Baralla getCargada() {
		return cargada;
	}

	public void setCargada(Baralla cargada) {
		this.cargada = cargada;
	}

	public void randomDeck() {
		setDeckValue(0);
		boolean end = false;
		ArrayList<Carta> deckRandomBuild = new ArrayList<Carta>();
		ArrayList<Carta> listaCartasTMP = Editor.cartesArray;
		ArrayList<Carta> posiblesCandidatos = new ArrayList<Carta>();
		while (!end) {
			// Una carta es candidata si el seu valor es <=(20 - [valor actual del deck en
			// construccio])
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
				// Eliminamos la carta de las proximas posibles cartas para que no pueda volver
				// a ser candidata

				/*
				 * for (int i = 0; i < listaCartasTMP.size(); ++i) { Carta buscar =
				 * listaCartasTMP.get(i); if (buscar.getName().equals(tmp.getName()) &&
				 * buscar.getAttack() == tmp.getAttack() && buscar.getDefense() ==
				 * tmp.getDefense() && buscar.getId() == tmp.getId() && buscar.getSummonCost()
				 * == tmp.getSummonCost() && buscar.getSummonCost() == tmp.getSummonCost()) {
				 * listaCartasTMP.remove(i); break; } }
				 */
				/*
				 * Removemos de posibles nuevos candidatos la carta que añadimos al nuevo random
				 * Deck evitando duplicados
				 **/
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
		// cartas de antes, solo se ejecuta esta en vez de la anterior si el usuario
		// desea mantener las cartas (Siempre que haya cartas en deck)
		boolean end = false;
		ArrayList<Carta> deckRandomBuild = mantener;
		ArrayList<Carta> listaCartasTMP = Editor.cartesArray;
		ArrayList<Carta> posiblesCandidatos = new ArrayList<Carta>();

		if (getDeckValue() >= 20) {
			// No hacemos nada y fin de la funcion; se mantiene la seleccion normal
			Editor.showError("No s'ha generat cap baralla Random ja que no pots afegir mes cartes;Valor Baralla = 20");
		} else {

			while (!end) {
				if (posiblesCandidatos.size() > 0)
					posiblesCandidatos.clear();
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

					/*
					 * for (int i = 0; i < listaCartasTMP.size(); ++i) { Carta buscar =
					 * listaCartasTMP.get(i); if (buscar.getName().equals(tmp.getName()) &&
					 * buscar.getAttack() == tmp.getAttack() && buscar.getDefense() ==
					 * tmp.getDefense() && buscar.getId() == tmp.getId() && buscar.getSummonCost()
					 * == tmp.getSummonCost() && buscar.getSummonCost() == tmp.getSummonCost()) {
					 * listaCartasTMP.remove(i); break; } }
					 */
					listaCartasTMP.remove(tmp);
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
		// Cargamos las cartas de la bbdd
		cartaExistDB.getConexion().carregarCartes();

		// Actualizamos los arrays de editor que son el reflejo de las dos listas de la
		// interfaz
		Editor.cartesArray = cartaExistDB.getConexion().obtenirCartes();
		Editor.deckArray = new ArrayList<Carta>();

	}

	private ArrayList<Carta> obtenirCardList() {

		// Funcion que devuelve un ArrayList con todas las cartas de eXist
		cartaExistDB.getConexion().carregarCartes();
		return cartaExistDB.getConexion().obtenirCartes();

	}

	private void actualizarDLM() {
		// Funció per actualitzar la informació de la interfaz a traves dels seus
		// arrayList propis

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
		/*
		 * Funcio per obtenir el valor de les cartes de la llista que correspon a la
		 * baralla en creació deckValue es un valor que reflexa de manera actualitzada
		 * el valor de les cartes de la baralla en creacio
		 * 
		 */
		return deckValue;
	}

	public void obtenirBaralla(String nom) {
		/*
		 * Funció que carrega una baralla de la bbdd de mongo segons el nom Si existeix;
		 * ControladorInterfaz.Cargada = (la baralla carregada)
		 * 
		 */
		barallaMongoDB = new BarallaMongoImpl();
		Baralla cargada = barallaMongoDB.getDeckFromName(nom);// Retornem la baralla

		/*
		 * Si la baralla existeix, el seu valor no sera null en cas contrari, si
		 */
		if (cargada != null) {
			// Carreguem als arrayList de Editor les cartes de la baralla carregada
			ArrayList<Carta> tmp = cargada.getLlistaDeCartes();
			Editor.deckArray = tmp;
			setDeckValue(tmp);
			// Establim el valor del deck recontant el valor de totes les cartes; podriem
			// utilitzar cargada.getDeckValue() amb altre funcio
			Editor.cartesArray = obtenirCardList();
			// Eliminem les cartes del deck carregat de la llista de totes les cartes per
			// evitar duplicats == buidem de la llista de cartes de coleccio les cartes de
			// la baralla carregada
			for (Carta eliminar : Editor.deckArray) {
				eliminarDuplicados(eliminar);

			}
			// Actualitzem l'apartat grafica
			actualizarDLM();// Actualitzem les llistes de la interfaz per a que representin la nova baralla
							// carregada i la colecio - [cartes baralla carregada]
			setDeckValue(tmp);// Tornem a actualitzar el valor de Controlador per representarlo a la interfaz
			setCargada(cargada);
		} else {
			Editor.showError("No s'ha trobat la baralla " + nom);
		}
	}

	public void eliminarDuplicados(Carta buscar) {
		/*
		 * Funcio per cercar una carta en la llista de cartes de la coleccio i
		 * eliminarla Nota: Implementada ja que avegades, ArrayList.remove(Object) falla
		 */
		for (int i = 0; i < Editor.cartesArray.size(); ++i) {
			Carta tmp = Editor.cartesArray.get(i);
			// Si tmp es exactament igual a buscar --> la elimina
			if (tmp.getName().equals(buscar.getName()) && tmp.getAttack() == buscar.getAttack()
					&& tmp.getDefense() == buscar.getDefense() && tmp.getId() == buscar.getId()
					&& tmp.getSummonCost() == buscar.getSummonCost() && tmp.getSummonCost() == buscar.getSummonCost()) {
				Editor.cartesArray.remove(i);
				break;
			}
		}
	}

	public void setDeckValue(ArrayList<Carta> cartes) {
		/*
		 * FUncio que recorre el ArrayList cartes sumant el valor individual de cada
		 * carta per actualitar el valor de la baralla o interfaz
		 */
		int valor = 0;
		for (Carta a : cartes) {
			valor += a.getValue();
		}
		setDeckValue(valor);
	}

	public void setDeckValue(int deckValue) {
		this.deckValue = deckValue;
	}

	public void guardarDeck(ArrayList<Carta> llistaDeck, String nom) {

		/*
		 * Funcio que guarda un arrayList i un nom com a baralla Creem la baralla
		 * mitjançant larrayList, el nom i valor de la baralla
		 */
		barallaMongoDB = new BarallaMongoImpl();
		Baralla a = new Baralla(nom, getDeckValue(), llistaDeck);
		if (isDeckCargado() && cargada != null && (a.getDeckName().equals(cargada.getDeckName()))) {
			if(barallaMongoDB.actualitzarBaralla(a)) {
				Editor.showError("S'ha actualitzat la baralla satisfactoriament");

				// Funcions per actualitzar valors de baralla i llistes
				cargarCardList();
				Editor.actualizarValorDeck();
				Editor.cargarCardListInJList();
				setDeckValue(0);
				Editor.actualizarValorDeck();
				cargada = null;
			}else {
				Editor.showError("Error: La baralla " + nom + " no s'ha pogut actualitzar");
			}
		} else {
			// Si la baralla s'ha desat exitosament, actualitzem la interfaz buidant la
			// llista de baralla i mostrem un missatge
			if (barallaMongoDB.guardarBaralla(a)) {
				Editor.showError("S'ha guardat la baralla satisfactoriament");

				// Funcions per actualitzar valors de baralla i llistes
				cargarCardList();
				Editor.actualizarValorDeck();
				Editor.cargarCardListInJList();
				setDeckValue(0);
				Editor.actualizarValorDeck();
				cargada = null;

			} else {
				// Reutilitzem la funcio de editor, tot i que no sigui un error exlicitament
				// Per informar que la baralla amb nom introduit ja existeix a la bbdd
				Editor.showError("Error: La baralla " + nom + " ja existex; editeula primer");
			}
		}
	}

}
