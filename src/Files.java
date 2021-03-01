import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Files {

asda

	public static List<Student> readExcel(String path) {
		File inputWorkbook = new File(path);
		Workbook w;

		try {
			w = Workbook.getWorkbook(inputWorkbook);
			// Get the first sheet
			Sheet sheet = w.getSheet("Studenti");
			List<Student> studenti = new ArrayList<Student>();
			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);
				String indeks = cells[0].getContents();
				String imePrezime = cells[1].getContents();
				String ime = imePrezime.split(" ")[0];
				String prezime = imePrezime.split(" ")[1];
				Student s = new Student(ime, prezime, indeks, "");
				studenti.add(s);
			}
			Sheet sheet1 = w.getSheet("Smjer");
			for (int i = 1; i < sheet1.getRows(); i++) {
				Cell[] cells = sheet1.getRow(i);
				String index = cells[0].getContents();
				String smjer = cells[1].getContents();
				Student s = findByIndex(index, studenti);
				if (s != null) {
					s.setSmjer(smjer);
				}
			}
			return studenti;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Student findByIndex(String index, List<Student> studenti) {
		for (Student student : studenti) {
			if (student.getIndex().equalsIgnoreCase(index)) {
				return student;
			}
		}
		return null;

	}

	public static boolean kreirajXML(String naziv, List<Student> studenti) {
		Document d = DocumentHelper.createDocument();
		Element root = d.addElement("Studentska_sluzba");
		for (Student s : studenti) {
			Element student = root.addElement("Student");
			Element brInd = student.addElement("BrojIndeksa");
			brInd.setText(s.getIndex());
			Element ime = student.addElement("Ime");
			ime.setText(s.getIme());
			Element prezime = student.addElement("Prezime");
			prezime.setText(s.getPrezime());
			Element smjer = student.addElement("Smjer");
			smjer.setText(s.getSmjer());
		}

		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(
					new FileOutputStream(naziv + ".xml"), "UTF8"), format);
			writer.write(d);
			writer.close();
			return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;

	}

	public static String validacija(String xml) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			factory.setSchema(schemaFactory
					.newSchema(new Source[] { new StreamSource(
							"studentska_sluzba_sema.xsd") }));

			SAXParser parser = factory.newSAXParser();
			SAXReader reader = new SAXReader(parser.getXMLReader());

			try {
				reader.read(new File(xml));
				return "Validacija uspjesna. ";
			} catch (Exception e) {
				return "Validacija nije uspjela. " + e.getMessage();
			}

		} catch (ParserConfigurationException | SAXException e) {
		}
		return "Greska u validaciji.";

	}

	public static boolean kreirajJSON(String naziv, List<Student> studenti) {

		JSONArray list1 = new JSONArray();

		for (Student s : studenti) {
			JSONObject sO = new JSONObject();
			sO.put("indeks", s.getIndex());
			sO.put("ime", s.getIme());
			sO.put("prezime", s.getPrezime());
			sO.put("smjer", s.getSmjer());
			System.out.println(list1);
			list1.add(sO);
		}

		try {

			FileWriter file = new FileWriter(naziv + ".json");
			file.write(list1.toJSONString());
			file.flush();
			file.close();
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
		}
		return false;

	}

	public static boolean kreirajPDFzaProizvode(String naziv, List<Student> studenti) {
		com.itextpdf.text.Document d = new com.itextpdf.text.Document();
		try {
			PdfWriter.getInstance(d, new FileOutputStream(new File(naziv
					+ ".pdf")));
			d.open();
			Font headerFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLUE);
			Font titleFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.RED);
			Font textFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL,
					BaseColor.BLACK);

			Paragraph p1 = new Paragraph("STUDENTI", titleFont);
			p1.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
			d.add(p1);
			d.add(new Paragraph(" "));
			kreiranjeTabeleZaProizvode(d, textFont, headerFont, studenti);
			d.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}

	private static void kreiranjeTabeleZaProizvode(com.itextpdf.text.Document d,
			Font textFont, Font headerFont, List<Student> studenti)
			throws DocumentException {
		PdfPTable table = new PdfPTable(4);

		PdfPCell c1 = new PdfPCell(new Phrase("Broj indeksa", headerFont));
		table.addCell(c1);

		PdfPCell c2 = new PdfPCell(new Phrase("Ime", headerFont));
		table.addCell(c2);

		PdfPCell c3 = new PdfPCell(new Phrase("Prezime", headerFont));
		table.addCell(c3);

		PdfPCell c4 = new PdfPCell(new Phrase("Smjer", headerFont));
		table.addCell(c4);

		d.add(new Paragraph(" "));

		for (Student s : studenti) {

			// Paragraph p1 = new Paragraph(s.toString(), textFont);
			table.addCell(new Phrase(s.getIndex(), textFont));
			table.addCell(new Phrase(s.getIme(), textFont));
			table.addCell(new Phrase(s.getPrezime(), textFont));
			table.addCell(new Phrase(s.getSmjer(), textFont));

		}

		d.add(table);
	}
	
	

}
