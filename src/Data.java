import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class Data {

	static DatabaseConnection d = new DatabaseConnection(
			"jdbc:mysql://localhost:3306/xml_students", "root", "");

	public static boolean importData(String path) {
		List<Student> studenti = Files.readExcel(path);
		if (studenti != null) {

			for (Student student : studenti) {
				boolean ok = d.insertStudent(student.getIme(),
						student.getPrezime(), student.getIndex());
				boolean ok1 = d.insertStudentProgram(student.getIndex(),
						student.getSmjer());
				if (!ok || !ok1) {
					return false;
				}
			}
			return true;
		}
		return false;

	}

	public static boolean deleteAll() {

		boolean ok = d.deleteDataFromTable("student_smjer");
		boolean ok1 = d.deleteDataFromTable("studenti");
		if (!ok || !ok1) {
			return false;
		}
		return true;
	}

	public static List<String> getPrograms() {
		return d.getPrograms();
	}
	
	public static List<Student> getStudents(String smjer) {
		if(smjer.equalsIgnoreCase("SVI")){
			smjer = null;
		}
		return d.getStudents(smjer);
	}
	
	public static Object[][] getStudentsForTable(String smjer) {
		if(smjer.equalsIgnoreCase("SVI")){
			smjer = null;
		}
		List<Student> studenti =  d.getStudents(smjer);
		if(studenti!=null){
			Object[][] objects = new Object[studenti.size()][4];
			int i = 0;
			for (Student s: studenti) {
				Object[] o = {s.getIndex(), s.getIme(), s.getPrezime(), s.getSmjer()};
				objects[i] = o;
				i++;
			}
			return objects;
		}
		return null;
	}
}
