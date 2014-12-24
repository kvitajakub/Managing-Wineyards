package pdb03.view.mainWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;

/**
 * Trida pro veskere nastavovani GUI v hlavnim okne aplikace. Pres tuto tridu se pote
 * pristupuje k vsem dalsim objektum GUI. Je modularni, pouziva dalsi tridy v tomto baliku, 
 * ktere jsou soucasti hlavniho okna a menu. Nevytvari zadne listenery, pouze definuje objekty,
 * jak jsou usporadany v gui a jejich gettery.
 * @author Jakub Kvita
 *
 */
public class MainWindow {

	public JFrame frame;
	private JLabel statusLabel;
	private LeftButtonsPanel leftPanel;
	private JPanel canvasPanel;
	private ItemListPanel itemListPanel;
	private ItemDetailPanel itemDetailPanel;
	private MapCanvas mapCanvas;
	private JMenuItem mntmFillDB;
	private JMenuItem mntmManual;
	private JMenuItem mntmAbout;
	private JMenuItem mntmSetValidity;
	private JMenuItem mntmQueryTemporalSelectPoints;
	private JMenuItem mntmQueryTemporalSelectLines;
	private JMenuItem mntmQueryTemporalSelectMultilines;
	private JMenuItem mntmQueryTemporalSelectPolygons;
	private JMenuItem mntmQueryTemporalSelectMultiPolygons;
	private JMenuItem mntmOwnerTable;
	private JMenuItem mntmWineTypeTable;
	private JMenuItem mntmDeleteSelectedInYear;
	private JMenuItem mntmQueryTemporalOwner;
	private JMenuItem mntmQueryTemporalWine;
	private JMenuItem mntmSetIntervalOwner;
	private JMenu mnSpatialQuery;
	private JMenu mnClosestWineyardsTo;
	private JMenuItem mntmClosest1;
	private JMenuItem mntmClosest2;
	private JMenuItem mntmClosest3;
	private JMenuItem mntmClosest4;
	private JMenuItem mntmScarecrowsInSelected;
	private JMenu mnWineyardsMaxDistance;
	private JMenuItem menuItemDistance10;
	private JMenuItem menuItemDistance20;
	private JMenuItem menuItemDistance30;
	private JMenuItem menuItemDistance40;
	private JMenuItem menuItemDistance50;
	private JMenuItem menuItemDistance60;
	private JMenuItem menuItemDistance70;
	private JMenuItem menuItemDistance80;
	private JMenuItem menuItemDistance90;
	private JMenuItem menuItemDistance100;
	private JMenuItem mntmWineArea;
	private JMenuItem mntmPercentagesOfWines;

	public JMenuItem getMntmFillDB() {
		return mntmFillDB;
	}

	/**
	 * Vytvori okno.
	 */
	public MainWindow() {
		initialize();
	}

	private void initialize() {
	
		frame = new JFrame();
		frame.getContentPane().setPreferredSize(new Dimension(1007, 700));
		frame.setName("frame");
		frame.setTitle("PDB03");
		frame.setSize(new Dimension(1024, 768));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		JMenu mnFile = new JMenu("Data");
		menuBar.add(mnFile);
		
		mntmFillDB = new JMenuItem("Fill database");
		mnFile.add(mntmFillDB);
		
		mntmOwnerTable = new JMenuItem("Owner Table");
		mnFile.add(mntmOwnerTable);
		
		mntmWineTypeTable = new JMenuItem("Wine Type Table");
		mnFile.add(mntmWineTypeTable);
		
		JMenu mnItem = new JMenu("Item");
		menuBar.add(mnItem);
		
		mntmSetValidity = new JMenuItem("Set Validity");
		mnItem.add(mntmSetValidity);
		
		mntmSetIntervalOwner = new JMenuItem("Set Interval Owner");
		mntmSetIntervalOwner.setToolTipText("Set New Owner in specified time interval.");
		mnItem.add(mntmSetIntervalOwner);
		
		mntmDeleteSelectedInYear = new JMenuItem("Delete Selected In Year");
		mnItem.add(mntmDeleteSelectedInYear);
		
		JMenu mnQueryTemporal = new JMenu("Temporal Query");
		menuBar.add(mnQueryTemporal);
		
		JMenu mnSimpleTemporal = new JMenu("Simple");
		mnQueryTemporal.add(mnSimpleTemporal);
		
		mntmQueryTemporalSelectPoints = new JMenuItem("Get Scarecrows");
		mnSimpleTemporal.add(mntmQueryTemporalSelectPoints);
		
		mntmQueryTemporalSelectLines = new JMenuItem("Get Roads");
		mnSimpleTemporal.add(mntmQueryTemporalSelectLines);
		
		mntmQueryTemporalSelectMultilines = new JMenuItem("Get Wine Rows");
		mnSimpleTemporal.add(mntmQueryTemporalSelectMultilines);
		
		mntmQueryTemporalSelectPolygons = new JMenuItem("Get Wineyards");
		mnSimpleTemporal.add(mntmQueryTemporalSelectPolygons);
		
		mntmQueryTemporalSelectMultiPolygons = new JMenuItem("Get Ponds");
		mnSimpleTemporal.add(mntmQueryTemporalSelectMultiPolygons);
		
		mntmQueryTemporalOwner = new JMenuItem("Get Owners");
		mntmQueryTemporalOwner.setToolTipText("Get Owners who grow wine in selected wineyard and when.");
		mnQueryTemporal.add(mntmQueryTemporalOwner);
		
		mntmQueryTemporalWine = new JMenuItem("Get Wines");
		mntmQueryTemporalWine.setToolTipText("Get all types of wine grown by selected owner in selected time period.");
		mnQueryTemporal.add(mntmQueryTemporalWine);
		
		mnSpatialQuery = new JMenu("Spatial Query");
		menuBar.add(mnSpatialQuery);
		
		mnClosestWineyardsTo = new JMenu("Closest wineyards to water");
		mnSpatialQuery.add(mnClosestWineyardsTo);
		
		mntmClosest1 = new JMenuItem("1 closest");
		mnClosestWineyardsTo.add(mntmClosest1);
		
		mntmClosest2 = new JMenuItem("2 closest");
		mnClosestWineyardsTo.add(mntmClosest2);
		
		mntmClosest3 = new JMenuItem("3 closest");
		mnClosestWineyardsTo.add(mntmClosest3);
		
		mntmClosest4 = new JMenuItem("4 closest");
		mnClosestWineyardsTo.add(mntmClosest4);
		
		mntmScarecrowsInSelected = new JMenuItem("Scarecrows in selected Wineyard");
		mnSpatialQuery.add(mntmScarecrowsInSelected);
		
		mnWineyardsMaxDistance = new JMenu("Wineyards max distance from Selected Road");
		mnSpatialQuery.add(mnWineyardsMaxDistance);
		
		menuItemDistance10 = new JMenuItem("10");
		mnWineyardsMaxDistance.add(menuItemDistance10);
		
		menuItemDistance20 = new JMenuItem("20");
		mnWineyardsMaxDistance.add(menuItemDistance20);
		
		menuItemDistance30 = new JMenuItem("30");
		mnWineyardsMaxDistance.add(menuItemDistance30);
		
		menuItemDistance40 = new JMenuItem("40");
		mnWineyardsMaxDistance.add(menuItemDistance40);
		
		menuItemDistance50 = new JMenuItem("50");
		mnWineyardsMaxDistance.add(menuItemDistance50);
		
		menuItemDistance60 = new JMenuItem("60");
		mnWineyardsMaxDistance.add(menuItemDistance60);
		
		menuItemDistance70 = new JMenuItem("70");
		mnWineyardsMaxDistance.add(menuItemDistance70);
		
		menuItemDistance80 = new JMenuItem("80");
		mnWineyardsMaxDistance.add(menuItemDistance80);
		
		menuItemDistance90 = new JMenuItem("90");
		mnWineyardsMaxDistance.add(menuItemDistance90);
		
		menuItemDistance100 = new JMenuItem("100");
		mnWineyardsMaxDistance.add(menuItemDistance100);
		
		mntmWineArea = new JMenuItem("Wine Area");
		mntmWineArea.setToolTipText("Area of wine type in selected year.");
		mnSpatialQuery.add(mntmWineArea);
		
		mntmPercentagesOfWines = new JMenuItem("Percentages of Wines");
		mnSpatialQuery.add(mntmPercentagesOfWines);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmManual = new JMenuItem("Manual");
		mnHelp.add(mntmManual);
		
		JSeparator separator = new JSeparator();
		mnHelp.add(separator);
		
		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		leftPanel = new LeftButtonsPanel();
		leftPanel.setPreferredSize(new Dimension(150, 600));
		leftPanel.setMinimumSize(new Dimension(20, 40));
		frame.getContentPane().add(leftPanel, BorderLayout.WEST);
		
		canvasPanel = new JPanel();
		canvasPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frame.getContentPane().add(canvasPanel, BorderLayout.CENTER);
		canvasPanel.setLayout(new BoxLayout(canvasPanel, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		canvasPanel.add(scrollPane);
		
		mapCanvas = new MapCanvas();
		scrollPane.setViewportView(mapCanvas);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(250, 600));
		frame.getContentPane().add(rightPanel, BorderLayout.EAST);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		itemListPanel = new ItemListPanel();
		itemListPanel.setPreferredSize(new Dimension(450, 250));
		rightPanel.add(itemListPanel);
		
		itemDetailPanel = new ItemDetailPanel();
		rightPanel.add(itemDetailPanel);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		statusPanel.setPreferredSize(new Dimension(10, 20));
		frame.getContentPane().add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		Component horizontalStrut = Box.createHorizontalStrut(7);
		statusPanel.add(horizontalStrut);
		
		statusLabel = new JLabel("Tady bude nejaky status, ktery se bude menit.");
		statusPanel.add(statusLabel);
		
		frame.pack();
	}

	public JMenuItem getMntmQueryTemporalSelectPoints() {
		return mntmQueryTemporalSelectPoints;
	}

	public JMenuItem getMntmQueryTemporalSelectLines() {
		return mntmQueryTemporalSelectLines;
	}

	public JMenuItem getMntmQueryTemporalSelectMultilines() {
		return mntmQueryTemporalSelectMultilines;
	}

	public JMenuItem getMntmQueryTemporalSelectPolygons() {
		return mntmQueryTemporalSelectPolygons;
	}

	public JMenuItem getMntmQueryTemporalSelectMultiPolygons() {
		return mntmQueryTemporalSelectMultiPolygons;
	}

	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public LeftButtonsPanel getLeftPanel() {
		return leftPanel;
	}

	public JPanel getCanvasPanel() {
		return canvasPanel;
	}

	public ItemListPanel getItemListPanel() {
		return itemListPanel;
	}

	public ItemDetailPanel getItemDetailPanel() {
		return itemDetailPanel;
	}

	public MapCanvas getMapCanvas() {
		return mapCanvas;
	}

	public JMenuItem getMntmManual() {
		return mntmManual;
	}

	public JMenuItem getMntmAbout() {
		return mntmAbout;
	}

	public JMenuItem getMntmSetValidity() {
		return mntmSetValidity;
	}

	public JFrame getFrame() {
		return frame;
	}

	public JMenuItem getMntmOwnerTable() {
		return mntmOwnerTable;
	}

	public JMenuItem getMntmWineTypeTable() {
		return mntmWineTypeTable;
	}

	public JMenuItem getMntmDeleteSelectedInYear() {
		return mntmDeleteSelectedInYear;
	}

	public JMenuItem getMntmQueryTemporalOwner() {
		return mntmQueryTemporalOwner;
	}

	public JMenuItem getMntmQueryTemporalWine() {
		return mntmQueryTemporalWine;
	}

	public JMenuItem getMntmSetIntervalOwner() {
		return mntmSetIntervalOwner;
	}

	public JMenuItem getMntmClosest1() {
		return mntmClosest1;
	}

	public JMenuItem getMntmClosest2() {
		return mntmClosest2;
	}

	public JMenuItem getMntmClosest3() {
		return mntmClosest3;
	}

	public JMenuItem getMntmClosest4() {
		return mntmClosest4;
	}

	public JMenuItem getMntmScarecrowsInSelected() {
		return mntmScarecrowsInSelected;
	}

	public JMenuItem getMenuItemDistance10() {
		return menuItemDistance10;
	}

	public JMenuItem getMenuItemDistance20() {
		return menuItemDistance20;
	}

	public JMenuItem getMenuItemDistance30() {
		return menuItemDistance30;
	}

	public JMenuItem getMenuItemDistance40() {
		return menuItemDistance40;
	}

	public JMenuItem getMenuItemDistance50() {
		return menuItemDistance50;
	}

	public JMenuItem getMenuItemDistance60() {
		return menuItemDistance60;
	}

	public JMenuItem getMenuItemDistance70() {
		return menuItemDistance70;
	}

	public JMenuItem getMenuItemDistance80() {
		return menuItemDistance80;
	}

	public JMenuItem getMenuItemDistance90() {
		return menuItemDistance90;
	}

	public JMenuItem getMenuItemDistance100() {
		return menuItemDistance100;
	}

	public JMenuItem getMntmWineArea() {
		return mntmWineArea;
	}

	public JMenuItem getMntmPercentagesOfWines() {
		return mntmPercentagesOfWines;
	}
	
}
