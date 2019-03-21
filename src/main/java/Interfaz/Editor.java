package Interfaz;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Logica.ControladorInterfaz;
import Objectes.Carta;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Editor {

	// Variables Swing
	private JFrame frame;
	private JScrollPane cartesScroll;
	private JScrollPane deckScroll;
	private static JLabel valueLabel;

	// Variables con listeners
	public static JList<Carta> cartasList;
	public static JList<Carta> deckList;

	private JButton btnRandomDeck;
	private JButton toCartas;
	private JButton toDeck;

	public static ArrayList<Carta> cartesArray;
	public static ArrayList<Carta> deckArray;
	public static DefaultListModel<Carta> cartesDLM;
	public static DefaultListModel<Carta> deckDLM;

	private static ControladorInterfaz controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor window = new Editor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Editor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		controller = new ControladorInterfaz();
		cartesArray = new ArrayList<Carta>();
		deckArray = new ArrayList<Carta>();
		cartesDLM = new DefaultListModel<Carta>();
		deckDLM = new DefaultListModel<Carta>();

		frame = new JFrame();
		frame.setBounds(100, 100, 800, 480);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		cartasList = new JList(cartesDLM);
		cartasList.setLayoutOrientation(JList.VERTICAL);// Surt rara si no
		// cartasList.setBounds(54, 80, 206, 332);
		cartasList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		cartesScroll = new JScrollPane(cartasList);
		cartesScroll.setBounds(54, 80, 206, 332);
		panel.add(cartesScroll);

		deckList = new JList(deckDLM);
		deckList.setLayoutOrientation(JList.VERTICAL);
		deckList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		deckScroll = new JScrollPane(deckList);
		// deckList.setBounds(533, 80, 206, 332);
		deckScroll.setBounds(533, 80, 206, 332);
		panel.add(deckScroll);

		toCartas = new JButton("<---");
		toCartas.setBackground(UIManager.getColor("CheckBox.light"));
		toCartas.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		toCartas.setBounds(323, 208, 127, 41);
		toCartas.setContentAreaFilled(false);
		toCartas.setFocusPainted(false);
		toCartas.setBorder(new EmptyBorder(1, 1, 1, 1));
		panel.add(toCartas);

		toDeck = new JButton("--->");
		toDeck.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		toDeck.setBounds(323, 290, 127, 41);
		toDeck.setContentAreaFilled(false);
		toDeck.setFocusPainted(false);
		toDeck.setBorder(new EmptyBorder(1, 1, 1, 1));
		panel.add(toDeck);

		JButton btnNewButton = new JButton("Carregar cartes");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		btnNewButton.setBounds(44, 22, 198, 34);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setOpaque(false);
		btnNewButton.setForeground(Color.BLACK);

		JButton btnCarregarBaralla = new JButton("Carregar Baralla");
		btnCarregarBaralla.setForeground(Color.BLACK);
		btnCarregarBaralla.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		btnCarregarBaralla.setFocusPainted(false);
		btnCarregarBaralla.setContentAreaFilled(false);
		btnCarregarBaralla.setBorder(new EmptyBorder(1, 1, 1, 1));
		btnCarregarBaralla.setBounds(290, 22, 198, 34);

		panel.add(btnCarregarBaralla);

		btnNewButton.setBorder(new EmptyBorder(1, 1, 1, 1));
		panel.add(btnNewButton);

		btnRandomDeck = new JButton("Baralla Aleatoria\r\n");
		// btnRandomDeck.setOpaque(false);
		btnRandomDeck.setForeground(Color.BLACK);
		btnRandomDeck.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		btnRandomDeck.setContentAreaFilled(false);
		btnRandomDeck.setFocusPainted(false);
		btnRandomDeck.setBorder(new EmptyBorder(1, 1, 1, 1));
		btnRandomDeck.setBounds(290, 125, 198, 34);

		panel.add(btnRandomDeck);

		JButton btnGuardarBaralla = new JButton("Guardar Baralla");
		btnGuardarBaralla.setOpaque(false);
		btnGuardarBaralla.setForeground(Color.BLACK);
		btnGuardarBaralla.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		btnGuardarBaralla.setFocusPainted(false);
		btnGuardarBaralla.setContentAreaFilled(false);
		btnGuardarBaralla.setBorder(new EmptyBorder(1, 1, 1, 1));
		btnGuardarBaralla.setBounds(510, 22, 198, 34);
		panel.add(btnGuardarBaralla);

		valueLabel = new JLabel("Valor baralla:" + controller.getDeckValue());
		valueLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		valueLabel.setBounds(537, 55, 153, 26);
		valueLabel.setForeground(Color.green);
		panel.add(valueLabel);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(Editor.class.getResource("/Interfaz/editor_background.jpg")));
		lblNewLabel.setBounds(0, 0, 784, 441);
		panel.add(lblNewLabel);

		// Action Listeners
		btnGuardarBaralla.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (controller.isCartasCargadas() || controller.isDeckCargado()) {
					if (deckArray.size() > 0) {
						String nom = JOptionPane.showInputDialog(null, "Nom de la Baralla",
								"Introdueix el nom de la baralla a guardar", 1);
						if (!nom.equals("") && nom != null) {
							controller.guardarDeck(deckArray, nom);
						} else {
							showError("El camp nom no pot estar buit");
						}
					}else {
						showError("No heu afegit cap carta a la baralla");
					}

				} else {
					showError("No heu carregat la colecio o cap baralla");
				}
			}
		});
		btnRandomDeck.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (controller.isCartasCargadas() || controller.isDeckCargado()) {
					if (deckDLM.size() > 0 && controller.getDeckValue() <= 20) {
						int opcion = JOptionPane.showConfirmDialog(null, "Has afegit cartes al deck, vols mantenirles?",
								"Alerta", JOptionPane.YES_NO_OPTION);
						if (opcion == JOptionPane.YES_OPTION) {
							controller.randomDeck(deckArray);
							actualizarValorDeck();
						} else if (opcion == JOptionPane.NO_OPTION) {
							controller.randomDeck();
							actualizarValorDeck();
						}
					} else if (controller.getDeckValue() > 20) {
						if (JOptionPane.showConfirmDialog(null,
								"El valor de la baralla es superior a 20, vol eliminarla?", "Alerta",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
							controller.randomDeck();
							actualizarValorDeck();
						} else {
							showError("No s'ha generat cap baralla ja que supera la restricció de Valor 20");
						}
					} else {
						controller.randomDeck();
					}

					actualizarValorDeck();
				}

				else {
					showError("No has carregat cap baralla o coleccio de cartes");
				}
			}
		});

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				controller.cargarCardList();
				controller.setCartasCargadas(true);
				cargarCardListInJList();
				
			}
		});

		btnCarregarBaralla.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(null, "Nom de la Baralla",
						"Introdueix el nom de la baralla a cercar", 1);
				if (name != null && !name.equals("")) {
					controller.setDeckCargado(true);
					controller.obtenirBaralla(name);
					actualizarValorDeck();
					controller.setDeckValue(controller.getBarallaCargada().getDeckValue());
				}
			}
		});
		toDeck.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (controller.isCartasCargadas() || controller.isDeckCargado()) {
					int point = cartasList.getSelectedIndex();

					if (point >= 0) {
						Carta tmp = cartasList.getModel().getElementAt(point);
						cartesArray.remove(tmp);
						deckArray.add(tmp);
						controller.setDeckValue(controller.getDeckValue() + tmp.getValue());
						deckDLM.addElement(cartasList.getSelectedValue());
						cartesDLM.remove(cartasList.getSelectedIndex());
						actualizarValorDeck();
						Editor.cartasList.setModel(Editor.cartesDLM);
						Editor.deckList.setModel(Editor.deckDLM);
					}
				}

			}

		});
		toCartas.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (controller.isCartasCargadas() || controller.isDeckCargado()) {
					int point = deckList.getSelectedIndex();
					if (point >= 0) {
						Carta tmp = deckList.getModel().getElementAt(point);
						deckArray.remove(tmp);
						cartesArray.add(tmp);
						controller.setDeckValue(controller.getDeckValue() - tmp.getValue());
						cartesDLM.addElement(deckList.getSelectedValue());
						deckDLM.remove(point);
						actualizarValorDeck();
						Editor.cartasList.setModel(Editor.cartesDLM);
						Editor.deckList.setModel(Editor.deckDLM);
					}

				} else {
					showError("No has cargado ninguna baraja o cartas");
				}
			}

		});

	}

	private void carregarCartes() {
		// Funcio hardcodejada temporalment en cas de no tenir eXist
		cartesArray.clear();
		deckArray.clear();
		for (int i = 1; i < 17; ++i) {
			Carta a = new Carta(i, "Carta " + i, i, i + 2, i - 2, 2);
			cartesArray.add(a);
		}
	}

	/*
	 * Deprecated private void cargarInvisibles() { cartasList.setVisible(true);
	 * deckList.setVisible(true); toDeck.setVisible(true);
	 * toCartas.setVisible(true); textField.setVisible(true);
	 * lblNomBaralla.setVisible(true); btnRandomDeck.setVisible(true); }
	 */
	public static void actualizarValorDeck() {
		valueLabel.setText("Valor baralla:" + controller.getDeckValue());
		if (controller.getDeckValue() > 20) {
			valueLabel.setForeground(Color.red);
		} else {
			valueLabel.setForeground(Color.green);
		}
	}

	public static void actualizarValorDeck(int newValor) {
		valueLabel.setText("Valor baralla:" + newValor);
		if (controller.getDeckValue() > 20) {
			valueLabel.setForeground(Color.red);
		} else {
			valueLabel.setForeground(Color.green);
		}
	}

	private void cargarCardListInJList() {
		// carregarCartes();
		cartesDLM.clear();
		deckDLM.clear();
		for (Carta a : cartesArray) {
			cartesDLM.addElement(a);
		}
		cartasList.setModel(cartesDLM);
	}

	public static void showError(String string) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, string, "Advertencia", 1);
	}
}
