package Interfaz;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class Aplicacio {

	private JFrame frame;
	private BufferedImage image;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Aplicacio window = new Aplicacio();
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
	public Aplicacio() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
	
		
		JLabel lblNewLabel = new JLabel("Generador de baralles de cartes");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblNewLabel.setBounds(252, 307, 402, 42);
		panel.add(lblNewLabel);
		
		
		
		JButton btnNewButton = new JButton("Inici");
		btnNewButton.setSelected(true);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		btnNewButton.setBorder(new EmptyBorder(50, 50, 50, 50));
		btnNewButton.setBounds(313, 357, 175, 51);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setFocusPainted(false);
		btnNewButton.setOpaque(false);
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
				new Editor();
				System.out.println("Carga de editor");
			}
		});
		panel.add(btnNewButton);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(Editor.class.getResource("/Interfaz/start_background.jpg")));
		iconLabel.setBounds(0, 0, 784, 441);
		panel.add(iconLabel);
	}


	

}
