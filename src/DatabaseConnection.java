import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.mysql.fabric.xmlrpc.base.Array;

public class DatabaseConnection {

	private String path;
	private String user;
	private String pass;
	private Connection conn = null;

	public DatabaseConnection(String path, String user, String pass) {
		super();
		this.path = path;
		this.user = user;
		this.pass = pass;
	}

	public boolean open() {
		try {
			conn = DriverManager.getConnection(path, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (conn != null) {
			return true;
		}

		return false;
	}

	public boolean close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				return false;
			}
		}
		return true;
	}

	public boolean insertStudent(String ime, String prezime, String index) {
		String sql = "INSERT INTO studenti (ime,prezime,indeks) VALUES ('"
				+ ime + "','" + prezime + "','" + index + "')";
		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public boolean insertStudentProgram(String index, String smjer) {
		String sql = "INSERT INTO student_smjer (fk_student_id,fk_smjer_id) VALUES ("
				+ "(select id from studenti where indeks ='"
				+ index
				+ "'),"
				+ "(select id from smjer where naziv ='" + smjer + "'))";
		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public boolean deleteDataFromTable(String table) {
		String sql = "DELETE FROM " + table;
		if (open()) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate(sql);
				close();
				return true;
			} catch (SQLException e) {
				return false;
			}
		}
		return false;

	}

	public List<String> getPrograms() {
		String sql = "SELECT naziv FROM smjer";
		if (open()) {
			try {
				List<String> smjerovi = new ArrayList<String>();
				Statement s = conn.createStatement();
				ResultSet set = s.executeQuery(sql);
				while (set.next()) {
					smjerovi.add(set.getString("naziv"));
				}
				close();
				return smjerovi;
			} catch (SQLException e) {
				return null;
			}
		}
		return null;

	}

	public List<Student> getStudents(String smjer) {
		String sql = "SELECT s.ime, s.prezime, s.indeks, sm.naziv"
				+ " FROM studenti s, student_smjer ss, smjer sm"
				+ " WHERE ss.fk_student_id = s.id AND ss.fk_smjer_id = sm.id ";
		if (smjer != null) {
			sql += " AND sm.naziv = '" + smjer + "'";
		}
		if (open()) {
			try {
				List<Student> studenti = new ArrayList<Student>();
				Statement s = conn.createStatement();
				ResultSet set = s.executeQuery(sql);
				while (set.next()) {
					String ime = set.getString("ime");
					String prezime = set.getString("prezime");
					String indeks = set.getString("indeks");
					String smjerS = set.getString("naziv");
					Student student = new Student(ime, prezime, indeks, smjerS);
					studenti.add(student);
				}
				close();
				return studenti;
			} catch (SQLException e) {
				return null;
			}
		}
		return null;

	}
}
