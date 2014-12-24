package pdb03.model;


/**
 * Trida pro jeden radek tabulky WineGrower.
 * @author Jakub Kvita
 *
 */
public class WineGrower {

	private int id;
	private String name;
	private String surname;
	private String address;
	public WineGrower() {
	}

	/**
	 * Sestrojeni noveho zaznamu, vyplnime daty, ktera jsou parametry.
	 * @param id ID zaznamu.
	 * @param name Jmeno majitele.
	 * @param surname Prijmeni majitele.
	 * @param address Adresa majitele.
	 */
	public WineGrower(int id, String name, String surname, String address) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.address = address;
	}

	/**
	 * Vrati data v tomto zaznamu podle indexu:
	 * 0 - ID
	 * 1 - jmeno
	 * 2 - prijmeni
	 * 3 - adresa
	 * @param columnIndex Index ktery potrebujeme.
	 * @return Objekt u prislusneho indexu.
	 */
	public Object getColumn(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return id;
		case 1:
			return name;
		case 2:
			return surname;
		case 3:
			return address;
		default:
			return null;
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public String getAddress() {
		return address;
	}

}
