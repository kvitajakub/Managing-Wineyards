package pdb03.model;

import javax.swing.Icon;

/**
 * Trida pro jeden druh vina, jeden zaznam do tabulky.
 * @author Jakub Kvita
 *
 */
public class WineType {

	private int id;
	private String name;
	private String description;
	private String colour;
	private Icon icon;
	
	/**
	 * Sestrojeni noveho objektu s hodnotami.
	 * @param id ID zaznamu v databazi.
	 * @param name Jmeno vina.
	 * @param description Popis vina.
	 * @param colour Barva vina.
	 * @param icon Obrazek vina.
	 */
	public WineType(int id, String name, String description, String colour, Icon icon) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.colour = colour;
		this.icon = icon;
	}
	
	public Icon getIcon() {
		return icon;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getColour() {
		return colour;
	}

	/**
	 * Vraci jednotlive polozky podle indexu.
	 * 0 - id
	 * 1 - name
	 * 2 - description
	 * 3 - colour
	 * 4 - icon
	 * @param columnIndex Index, ktery chceme.
	 * @return Objekt odpovidajici indexu.
	 */
	public Object getColumn(int columnIndex) {

		switch (columnIndex) {
			case 0:
				return id;
			case 1:
				return name;
			case 2:
				return description;
			case 3:
				return colour;
			case 4:
				return icon;
			default:
				return null;
		}	
	}
}
