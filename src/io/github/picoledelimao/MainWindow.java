package io.github.picoledelimao;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;

import io.github.picoledelimao.mdl.core.MDLNotFoundException;
import io.github.picoledelimao.mdl.core.MDLParserErrorException;

public class MainWindow {

	private JFrame frame;
	private PlaceholderTextField inputFileField;
	private final JButton inputFileButton = new JButton("...");
	private PlaceholderTextField outputFileField;
	private PlaceholderTextField animationTagField;
	private JCheckBox chckbxCloneTextureAnimations;
	private JCheckBox chckbxCloneParticleEmitters;
	private JButton btnClone;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOpen;
	private JMenu mnHelp;
	private File inputFile;
	private File outputFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 260);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Animation Duplicator 1.0");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {30, 341, 1, 1};
		gridBagLayout.rowHeights = new int[]{30, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		inputFileField = new PlaceholderTextField();
		inputFileField.setPlaceholder("Input file");
		GridBagConstraints gbc_inputFileField = new GridBagConstraints();
		gbc_inputFileField.insets = new Insets(0, 0, 5, 5);
		gbc_inputFileField.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputFileField.gridx = 1;
		gbc_inputFileField.gridy = 1;
		frame.getContentPane().add(inputFileField, gbc_inputFileField);
		inputFileField.setColumns(10);
		GridBagConstraints gbc_inputFileButton = new GridBagConstraints();
		gbc_inputFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_inputFileButton.gridx = 2;
		gbc_inputFileButton.gridy = 1;
		inputFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "*.mdl";
					}
					
					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
			                return true;
			            }
			            final String name = f.getName();
			            return name.endsWith(".mdl");
					}
				});
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					inputFile = fc.getSelectedFile();
					inputFileField.setText(inputFile.getAbsolutePath());
				}
			}
		});
		frame.getContentPane().add(inputFileButton, gbc_inputFileButton);
		
		outputFileField = new PlaceholderTextField();
		outputFileField.setPlaceholder("Output file");
		GridBagConstraints gbc_outputFileField = new GridBagConstraints();
		gbc_outputFileField.insets = new Insets(0, 0, 5, 5);
		gbc_outputFileField.fill = GridBagConstraints.HORIZONTAL;
		gbc_outputFileField.gridx = 1;
		gbc_outputFileField.gridy = 2;
		frame.getContentPane().add(outputFileField, gbc_outputFileField);
		outputFileField.setColumns(10);
		
		JButton outputFileButton = new JButton("...");
		outputFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc;
				if (inputFile != null) {
					fc = new JFileChooser(inputFile);
				} else {
					fc = new JFileChooser();
				}
				fc.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "*.mdl";
					}
					
					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
			                return true;
			            }
			            final String name = f.getName();
			            return name.endsWith(".mdl");
					}
				});
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					outputFile = fc.getSelectedFile();
					String path = outputFile.getAbsolutePath();
					if (!path.endsWith(".mdl")) {
						path += ".mdl";
					}
					outputFileField.setText(path);
				}
			}
		});
		GridBagConstraints gbc_outputFileButton = new GridBagConstraints();
		gbc_outputFileButton.insets = new Insets(0, 0, 5, 0);
		gbc_outputFileButton.gridx = 2;
		gbc_outputFileButton.gridy = 2;
		frame.getContentPane().add(outputFileButton, gbc_outputFileButton);
		
		animationTagField = new PlaceholderTextField();
		animationTagField.setPlaceholder("Animation tag");
		GridBagConstraints gbc_animationTagField = new GridBagConstraints();
		gbc_animationTagField.insets = new Insets(0, 0, 5, 5);
		gbc_animationTagField.fill = GridBagConstraints.HORIZONTAL;
		gbc_animationTagField.gridx = 1;
		gbc_animationTagField.gridy = 3;
		frame.getContentPane().add(animationTagField, gbc_animationTagField);
		animationTagField.setColumns(10);
		
		chckbxCloneTextureAnimations = new JCheckBox("Clone texture animations");
		chckbxCloneTextureAnimations.setSelected(true);
		GridBagConstraints gbc_chckbxCloneTextureAnimations = new GridBagConstraints();
		gbc_chckbxCloneTextureAnimations.anchor = GridBagConstraints.WEST;
		gbc_chckbxCloneTextureAnimations.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCloneTextureAnimations.gridx = 1;
		gbc_chckbxCloneTextureAnimations.gridy = 4;
		frame.getContentPane().add(chckbxCloneTextureAnimations, gbc_chckbxCloneTextureAnimations);
		
		chckbxCloneParticleEmitters = new JCheckBox("Clone particle emitters animations");
		chckbxCloneParticleEmitters.setSelected(true);
		GridBagConstraints gbc_chckbxCloneParticleEmitters = new GridBagConstraints();
		gbc_chckbxCloneParticleEmitters.anchor = GridBagConstraints.WEST;
		gbc_chckbxCloneParticleEmitters.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxCloneParticleEmitters.gridx = 1;
		gbc_chckbxCloneParticleEmitters.gridy = 5;
		frame.getContentPane().add(chckbxCloneParticleEmitters, gbc_chckbxCloneParticleEmitters);
		
		btnClone = new JButton("Clone");
		btnClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputFile = inputFileField.getText();
				String outputFile = outputFileField.getText();
				String animationTag = animationTagField.getText();
				boolean cloneTextures = chckbxCloneTextureAnimations.isSelected();
				boolean cloneParticleEmitters = chckbxCloneParticleEmitters.isSelected();
				if (inputFile.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "You must specify an input file.", "An error ocurred", JOptionPane.WARNING_MESSAGE);
				} else if (outputFile.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "You must specify an output file.", "An error ocurred", JOptionPane.WARNING_MESSAGE);
				} else if (animationTag.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "You must specify the cloned animations tag.", "An error ocurred", JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						AnimationDuplicator.cloneAnimations(inputFile, outputFile, animationTag, cloneTextures, cloneParticleEmitters);
						JOptionPane.showMessageDialog(frame, "Done!", "Success", JOptionPane.INFORMATION_MESSAGE);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | IOException | MDLNotFoundException
							| MDLParserErrorException e1) {
						JOptionPane.showMessageDialog(frame, e1.getMessage(), "An error ocurred", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		});
		GridBagConstraints gbc_btnClone = new GridBagConstraints();
		gbc_btnClone.insets = new Insets(0, 0, 0, 5);
		gbc_btnClone.gridx = 1;
		gbc_btnClone.gridy = 6;
		frame.getContentPane().add(btnClone, gbc_btnClone);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		
		mnHelp = new JMenu("Help");
		mnHelp.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent arg0) {
			}
			public void menuDeselected(MenuEvent arg0) {
			}
			public void menuSelected(MenuEvent arg0) {
				new HelpWindow().setVisible(true);
			}
		});

		mnHelp.setMnemonic(KeyEvent.VK_F1);
		menuBar.add(mnHelp);
	}

}
