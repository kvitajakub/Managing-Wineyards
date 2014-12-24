package pdb03.view;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;

/**
 * Trida pro vyvolani okna O programu. Obsahuje i text, neni potreba specialni kontrolni tridy.
 * @author Jakub Kvita
 */
public class AboutWindow extends javax.swing.JFrame {

	private static final long serialVersionUID = -4961858634542857915L;

	private String htmlText = "<font face=\"tahoma\"size=4><p style=\"text-align: center;\" ><br><br><font size=6><b>Wineyards</b></font><br><br>"
            + "<br>"
            + "Created as part of project, PDB course at FIT BUT 2014.<br><br><br><br>"
            + "<b>Authors:</b><br><br>"
            + "<i>Jakub Kvita<br>"
            + "<font size=3>kvitajakub@gmail.com</font><br><br>"
            + "<i>Petr Lacko<br>"
            + "<font size=3>lackopeter17@gmail.com</font><br><br>"
            + "<i>Jan Bednarik<br>"
            + "<font size=3>ja.bedna1@gmail.com<br></font><br>";
    
    
    private javax.swing.JTextPane textp;
    
    /**
     * Vytvoreni okna.
	 * Potreba pote zavolat setVisible odjinud. 
     */
    public AboutWindow() {
    	getContentPane().setPreferredSize(new Dimension(400, 400));
        initComponents();
    }

    private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        textp = new javax.swing.JTextPane();
        textp.setPreferredSize(new Dimension(400, 400));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setResizable(false);

        textp.setEditable(false);
        textp.setContentType("text/html");
        textp.setText(htmlText);
        jScrollPane1.setViewportView(textp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }
}
