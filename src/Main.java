import java.io.File;


public class Main {

	public static void main(String[] args) {
		DatabaseConnection d = new DatabaseConnection("jdbc:mysql://localhost:3306/xml_studenti", "root", "");
	
		d.insertStudent("Marko", "Markovic", "01-15");
		d.insertStudentProgram("01-15","IS");
		Files.readExcel("Studenti.xls");

	}

}
