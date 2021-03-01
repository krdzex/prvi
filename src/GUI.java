import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JButton importBtn;
	private JButton btnEmptyDatabase;
	private JFrame frame;
	private JComboBox comboBox;
	private JLabel lblNewLabel;
	private JButton showBtn;
	private JFileChooser fc;
	private JTable table;
	private JLabel label;
	private JTextField fileNameTxt;
	private JButton exportXmlBtn;
	private JButton exportJsonBtn;
	private JButton exportPdfBtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
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
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 833, 493);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getImportBtn());
		contentPane.add(getBtnEmptyDatabase());
		contentPane.add(getComboBox());
		contentPane.add(getLblNewLabel());
		contentPane.add(getShowBtn());
		frame = this;
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"EXCEL FILES", "xls", "xlsx");
		fc.setFileFilter(filter);
		table = new JTable();
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(23, 160, 747, 276);
		frame.getContentPane().add(scroll);
		contentPane.add(getLabel());
		contentPane.add(getFileNameTxt());
		contentPane.add(getExportXmlBtn());
		contentPane.add(getExportJsonBtn());
		contentPane.add(getExportPdfBtn());

	}

	private JButton getImportBtn() {
		if (importBtn == null) {
			importBtn = new JButton("Import");
			importBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fc.showOpenDialog(frame);
					boolean ok = Data
							.importData(fc.getSelectedFile().getPath());
					if (ok) {
						JOptionPane
								.showMessageDialog(frame, "Uspjesan import.");
					} else {
						JOptionPane.showMessageDialog(frame,
								"Greska u importu.", "Greska",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			importBtn.setBounds(23, 24, 89, 23);
		}
		return importBtn;
	}

	private JButton getBtnEmptyDatabase() {
		if (btnEmptyDatabase == null) {
			btnEmptyDatabase = new JButton("Empty database");
			btnEmptyDatabase.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean ok = Data.deleteAll();
					if (ok) {
						JOptionPane.showMessageDialog(frame, "Uspjesno.");
					} else {
						JOptionPane.showMessageDialog(frame,
								"Greska u brisanju.", "Greska",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			btnEmptyDatabase.setBounds(122, 24, 145, 23);
		}
		return btnEmptyDatabase;
	}

	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
			comboBox.setBounds(96, 83, 145, 20);
			List<String> list = Data.getPrograms();
			comboBox.addItem("SVI");
			if (list != null) {
				for (String string : list) {
					comboBox.addItem(string);
				}
			}
		}
		return comboBox;
	}

	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Smjer:");
			lblNewLabel.setBounds(51, 86, 46, 14);
		}
		return lblNewLabel;
	}

	private JButton getShowBtn() {
		if (showBtn == null) {
			showBtn = new JButton("Show");
			showBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Object[][] students = Data.getStudentsForTable(comboBox
							.getSelectedItem().toString());

					Object[] columnNames = { "Indeks", "Ime", "Prezime",
							"Smjer" };

					DefaultTableModel dtm = new DefaultTableModel(students,
							columnNames);
					table.setModel(dtm);
				}
			});
			showBtn.setBounds(259, 82, 89, 23);
		}
		return showBtn;
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Naziv fajla:");
			label.setBounds(27, 117, 78, 14);
		}
		return label;
	}

	private JTextField getFileNameTxt() {
		if (fileNameTxt == null) {
			fileNameTxt = new JTextField();
			fileNameTxt.setColumns(10);
			fileNameTxt.setBounds(93, 114, 234, 20);
		}
		return fileNameTxt;
	}

	private JButton getExportXmlBtn() {
		if (exportXmlBtn == null) {
			exportXmlBtn = new JButton("Export XML");
			exportXmlBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String s = null;
					List<Student> studenti = Data.getStudents(comboBox
							.getSelectedItem().toString());
					String name = fileNameTxt.getText();
					boolean ok = Files.kreirajXML("xml/" + name, studenti);
					if (ok) {
						String poruka = Files
								.validacija("xml/" + name + ".xml");
						JOptionPane.showMessageDialog(frame,
								"Uspjesan eksport. " + poruka);
					} else {
						JOptionPane.showMessageDialog(frame,
								"Neuspjesan eksport. ");
					}

				}
			});
			exportXmlBtn.setBounds(335, 113, 133, 23);
		}
		return exportXmlBtn;
	}

	private JButton getExportJsonBtn() {
		if (exportJsonBtn == null) {
			exportJsonBtn = new JButton("Export JSON");
			exportJsonBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<Student> studenti = Data.getStudents(comboBox
							.getSelectedItem().toString());
					String name = fileNameTxt.getText();
					boolean ok = Files.kreirajJSON("json/" + name, studenti);
					if (ok) {
						JOptionPane.showMessageDialog(frame,
								"Uspjesan eksport. ");
					} else {
						JOptionPane.showMessageDialog(frame,
								"Neuspjesan eksport. ");
					}
				}
			});
			exportJsonBtn.setBounds(478, 113, 133, 23);
		}
		return exportJsonBtn;
	}

	private JButton getExportPdfBtn() {
		if (exportPdfBtn == null) {
			exportPdfBtn = new JButton("Export PDF");
			exportPdfBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					List<Student> studenti = Data.getStudents(comboBox
							.getSelectedItem().toString());
					String name = fileNameTxt.getText();
					boolean ok = Files.kreirajPDFzaProizvode("pdf/" + name, studenti);
					if (ok) {
						JOptionPane.showMessageDialog(frame,
								"Uspjesan eksport. ");
					} else {
						JOptionPane.showMessageDialog(frame,
								"Neuspjesan eksport. ");
					}
				}
			});
			exportPdfBtn.setBounds(621, 113, 133, 23);
		}
		return exportPdfBtn;
	}
}
