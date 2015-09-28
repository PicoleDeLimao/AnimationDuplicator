package io.github.picoledelimao;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class HelpWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HelpWindow frame = new HelpWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HelpWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 285);
		setResizable(false);
		setTitle("Animation Duplicator 1.0 - Help");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		contentPane.add(btnOk, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("<html>This is a simple program capable to cloning a model animations under another animation tag. Very useful as a base to alternate forms.<br>This program receives as input the input model file (in MDL format. If you don't have the MDL format, you can use the following conversion tool: <a href=\"http://www.hiveworkshop.com/forums/tools-560/mdlx-converter-62991\">http://www.hiveworkshop.com/forums/tools-560/mdlx-converter-62991/</a>), the output file and the animation tag of the cloned animations.<br><br>If you found any bug, please, send-me an e-mail: <a href=\"mailto:abner.math.c@gmail.com\" target=\"_top\">abner.math.c@gmail.com</a><br><br>Animation Duplicator - 28/09/15<br>Version: 1.0<br>Author: Abner M. C.</html>");
		contentPane.add(lblNewLabel, BorderLayout.CENTER);
	}

}
