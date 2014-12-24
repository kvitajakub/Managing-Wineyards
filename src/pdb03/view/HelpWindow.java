package pdb03.view;
import java.awt.Cursor;

/**
 * Trida pro vyvolani okna manualu. Obsahuje formatovany text, ktery se ma vypisovat
 * a tvar GUI. Vytvari staticke okno, takze nepotrebuje dalsi kontrolni tridu.
 * @author Jakub Kvita
 */
public class HelpWindow extends javax.swing.JFrame {
    
	private static final long serialVersionUID = -3725554114903349478L;
	
	private final String header1 = "Main Window";
	private final String htmlText1 = "<font face=\"tahoma\"size=4><br><br><center><font size=6><b> THE Application</b></font></center><br><br>"
            + "This application is for managing wineyards and their surroundings in the database. "
            + "In the main window we can insert objects in the map by clicking on the appropriate button "
            + "on the left and then on the map. After completion press Enter to create and upload object "
            + "to database. Table on the upper right side contains same data as map, but in different format, "
            + "both table and the map are connected and selecting one will cause selection of the other. "
            + "Detailed information about the selected object will be in the lower right part, if there are "
            + "data needed to be shown. <br>"
            + "Selecting active year which controls which data will be painted is possible with time slider "
            + "on the upper left side. Year selected here is the used for queries working with active year.	<br>"
            + "At the menu part Data, database can be filled with default data and other table windows can be "
            + "opened.";
	
	private final String header2 = "Other Tables";
    private final String htmlText2 = "<font face=\"tahoma\"size=4><br><br><center><font size=6><b>Other Tables</b></font></center><br><br>"
            + "Two more data tables can be accessed from the menu Data or through edit button in lower right detail part, "
            + "if they are shown.<br>"
            + "Owner Window is simple table with all the owners, form which you can select up to one and "
            + "do something with him - edit or delete or add new one. <br>"
            + "Wine Type Window works in similar way, with addition of pictures, preview in table and full image on the right.";
    
	private final String header3 = "Temporal Queries";    
    private final String htmlText3 = "<font face=\"tahoma\"size=4><br><br><center><font size=6><b>Temporal Queries</b></font></center><br><br>"
            + "For temporal queries you can set new validity of interval of selected object. If it clash with other validities, they "
            + "will be shifted. "
			+ "You can select one row and delete or edit, and rotate selected picture or find most similar one. Adding new items "
			+ "is also possible. Set new owner for specific interval in time and detele item in currently selected year. "
			+ "For other simple temporal queries you can select all items of specific type in selected year. <br>"
			+ "Also all owners who are growing something on specific selected wineyard in any time or all wines grown by "
			+ "selected owner in picked time period.";
    
	private final String header4 = "Spatial Queries";    
    private final String htmlText4 = "<font face=\"tahoma\"size=4><br><br><center><font size=6><b>Spatial Queries</b></font></center><br><br>"
            + "As part of spacial queries, you can select wineyards close to any source of water (pond) and set how "
            + "many of them you want. Also get all the scarecrows in selected wineyard and all the wineyards "
            + "with distance at most some number from selected road. "
            + "You can get total area of selected wine which was grown in spefic year or get percentages of wine types "
            + "grown in selected year. Also its not going to work if there are no wine_rows in whole year.";
    
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    private javax.swing.JTextPane jTextPane4;
    
    /**
     * Vytvoreni okna.
     * Potreba pote zavolat setVisible odjinud.
     */
    public HelpWindow() {
        initComponents();
    }
    
    private void initComponents() {
    	
        javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        javax.swing.JScrollPane jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane4 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manual");
        setResizable(false);

        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        jScrollPane1.setViewportView(jTextPane1);
        jTextPane1.setEditable(false);
        jTextPane1.setContentType("text/html");
        jTextPane1.setText(htmlText1);

        jTabbedPane1.addTab(header1, jScrollPane1);

        jScrollPane2.setViewportView(jTextPane2);
        jTextPane2.setEditable(false);
        jTextPane2.setContentType("text/html");
        jTextPane2.setText(htmlText2);

        jTabbedPane1.addTab(header2, jScrollPane2);

        jScrollPane3.setViewportView(jTextPane3);
        jTextPane3.setEditable(false);
        jTextPane3.setContentType("text/html");
        jTextPane3.setText(htmlText3);

        jTabbedPane1.addTab(header3, jScrollPane3);

        jScrollPane4.setViewportView(jTextPane4);
        jTextPane4.setEditable(false);
        jTextPane4.setContentType("text/html");
        jTextPane4.setText(htmlText4);

        jTabbedPane1.addTab(header4, jScrollPane4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleName("");

        pack();
    }
}
