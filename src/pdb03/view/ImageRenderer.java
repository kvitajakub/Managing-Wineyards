package pdb03.view;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Nase vlastni vykreslovatko jednotlivych bunek v JTable.
 * Tato trida vykresluje misto textu obrazek v JLable, v nahledoven rozliseni SIZExSIZE,
 * ktere je 50x50;
 * @author Jakub Kvita
 *
 */
public class ImageRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;

		/**
		 * Rozliseni obrazku vykreslovanych timto rendererem.
		 */
		public static int SIZE = 50;
		
		/**
		 * Vlastni JLabel kteremu se obrazek vykresluje v rozliseni SIZE
		 * uprostred plochy.
		 */
		JLabel lbl = new JLabel() {
			private static final long serialVersionUID = 1L;

			@Override
		    public void paintComponent (Graphics g) {
//		        super.paintComponent (g);
		        if (getIcon() != null) {
		            g.drawImage (((ImageIcon)getIcon()).getImage(), (getWidth()/2)-SIZE/2,(getHeight()/2)-SIZE/2, SIZE, SIZE, null);
		        }
		    }
	  };
	  
	  /**
	   * Prepsana metoda pro nastaveni vysky radku tabulky podle velikosti SIZE.
	   */
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
	      boolean hasFocus, int row, int column) {
		  
		  	table.setRowHeight(row, SIZE);
			lbl.setIcon((Icon)value);
			return lbl;
	  }
}