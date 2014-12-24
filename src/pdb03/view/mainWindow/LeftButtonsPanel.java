package pdb03.view.mainWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 * Podobne jako trida MainWindow, tato trida obsahuje format gui pro cast hlavniho okna,
 * prvek ovladajici nastaveni casu a tlacitka pro vkladani novych prostorovych objektu do canvasu.
 * Zde nejsou listenery, ty se nastavuji v tride, ktera tuto tridu instanciuje, pres gettery.
 * @author Jakub Kvita
 *
 */
public class LeftButtonsPanel extends JPanel {

	private static final long serialVersionUID = 3992346932036763469L;
	private JToggleButton drawPointButton;
	private JToggleButton drawLineButton;
	private JToggleButton drawMultilineButton;
	private JToggleButton drawPolygonButton;
	private JToggleButton drawMultiPolygonButton;
	private JSlider timeSlider;

	/**
	 * Vytvoreni panelu
	 */
	public LeftButtonsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel timePanel = new JPanel();
		add(timePanel);
		timePanel.setLayout(new BorderLayout(0, 0));
		
		timeSlider = new JSlider();
		timeSlider.setSize(new Dimension(20, 40));
		timeSlider.setPreferredSize(new Dimension(200, 40));
		timeSlider.setMaximumSize(new Dimension(32767, 40));
		timeSlider.setMinimumSize(new Dimension(40, 100));
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(true);
		timeSlider.setMinorTickSpacing(1);
		timeSlider.setMajorTickSpacing(2);
		timeSlider.setValue(2014);
		timeSlider.setMaximum(2020);
		timeSlider.setMinimum(2000);
		timeSlider.setOrientation(SwingConstants.VERTICAL);
		timePanel.add(timeSlider, BorderLayout.CENTER);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		verticalStrut.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		timePanel.add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		timePanel.add(verticalStrut_1, BorderLayout.SOUTH);
		
		JPanel itemsPanel = new JPanel();
		add(itemsPanel);
		itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
		
		drawPointButton = new JToggleButton("Scarecrow");
		drawPointButton.setPreferredSize(new Dimension(140, 50));
		drawPointButton.setMaximumSize(new Dimension(140, 750));
		drawPointButton.setMinimumSize(new Dimension(50, 2));
		drawPointButton.setToolTipText("Point");
		drawPointButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemsPanel.add(drawPointButton);
		
		drawLineButton = new JToggleButton("Road");
		drawLineButton.setMinimumSize(new Dimension(15, 7));
		drawLineButton.setMaximumSize(new Dimension(140, 750));
		drawLineButton.setPreferredSize(new Dimension(140, 50));
		drawLineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemsPanel.add(drawLineButton);
		
		drawMultilineButton = new JToggleButton("Wine Row");
		drawMultilineButton.setPreferredSize(new Dimension(150, 50));
		drawMultilineButton.setMinimumSize(new Dimension(15, 7));
		drawMultilineButton.setMaximumSize(new Dimension(140, 750));
		drawMultilineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemsPanel.add(drawMultilineButton);
		
		drawPolygonButton = new JToggleButton("Wineyard");
		drawPolygonButton.setMinimumSize(new Dimension(15, 7));
		drawPolygonButton.setMaximumSize(new Dimension(140, 750));
		drawPolygonButton.setPreferredSize(new Dimension(150, 50));
		drawPolygonButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemsPanel.add(drawPolygonButton);
		
		drawMultiPolygonButton = new JToggleButton("Ponds");
		drawMultiPolygonButton.setPreferredSize(new Dimension(150, 50));
		drawMultiPolygonButton.setMinimumSize(new Dimension(15, 7));
		drawMultiPolygonButton.setMaximumSize(new Dimension(140, 750));
		drawMultiPolygonButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		itemsPanel.add(drawMultiPolygonButton);

	}
	public JToggleButton getDrawPointButton() {
		return drawPointButton;
	}

	public JToggleButton getDrawLineButton() {
		return drawLineButton;
	}

	public JToggleButton getDrawMultiLineButton() {
		return drawMultilineButton;
	}

	public JToggleButton getDrawPolygonButton() {
		return drawPolygonButton;
	}

	public JToggleButton getDrawMultiPolygonButton() {
		return drawMultiPolygonButton;
	}

	public JSlider getTimeSlider() {
		return timeSlider;
	}
}
