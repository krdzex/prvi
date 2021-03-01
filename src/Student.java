
public class Student {

	
	private String ime;
	private String prezime;
	private String index;
	private String smjer;
	
	
	public Student(String ime, String prezime, String index, String smjer) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.index = index;
		this.smjer = smjer;
	}
		
	@Override
	public String toString() {
		return "Student [ime=" + ime + ", prezime=" + prezime + ", index="
				+ index + ", smjer=" + smjer + "]";
	}

	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getSmjer() {
		return smjer;
	}
	public void setSmjer(String smjer) {
		this.smjer = smjer;
	}
	
	
}
