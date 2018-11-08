package wireworld;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.io.*;

public class Wireworld extends JFrame implements KeyListener
{
	private static final String CARD_MENU = "CARD_MENU";
	private static final String CARD_VIEW = "CARD_VIEW";

	private Model model;
	private View view;
	private Timer timer;
	private CardLayout cl;
	private JPanel menu;
	private JComboBox<String> cboMapList;
	
	public Wireworld()
	{
		super("Wireworld");
		setDefaultCloseOperation(EXIT_ON_CLOSE); // allow window to close
		setSize(500, 500);
		  
		model = new Model("/maps/Map1.w4m");
		view = new View(model);
		  
		this.addKeyListener(this);
		
		// Add a timer
		timer = new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleTimerTick();
			}
		});
		timer.start();
		
		menu = new JPanel();
		
		String[] maps = getMapList();
		cboMapList = new JComboBox<String>(maps);
		if (maps.length > 0) {
			cboMapList.setSelectedIndex(0);
		}
		menu.add(cboMapList);
		
		JButton btnBegin = new JButton("BEGIN");
		btnBegin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(getContentPane(), CARD_VIEW);
				requestFocusInWindow();
				model.mapName = "/maps/" + (String)cboMapList.getSelectedItem();
				model.reset();
				model.player.paused = false;
			}
		});
		menu.add(btnBegin);
		
		this.getContentPane().setLayout(new CardLayout());
		this.getContentPane().add(menu, CARD_MENU);
		this.getContentPane().add(view, CARD_VIEW);
		cl = (CardLayout)(this.getContentPane().getLayout());
		cl.show(getContentPane(), CARD_MENU);
		this.requestFocusInWindow();
	}
	
	public static void main(String[] args)
	{
		Wireworld wireworld = new Wireworld();
		wireworld.setVisible(true);
	}
	
	private void handleTimerTick()
	{
		model.update();
		view.refresh((int)getContentPane().getSize().getWidth(), (int)getContentPane().getSize().getHeight());
		if (model.player.returnToMenu) {
			cl.show(getContentPane(), CARD_MENU);
		}
		if (model.player.restart) {
			model.reset();
			model.player.paused = false;
		}
	}
	
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e)
	{
		model.keys.add(Integer.toString(e.getKeyCode()));
	}
	
	public void keyReleased(KeyEvent e)
	{
		model.keys.remove(Integer.toString(e.getKeyCode()));
	}
	
	public String[] getMapList() {
		ArrayList<String> maps = new ArrayList<String>();
		try {
			InputStream in = Wireworld.class.getResourceAsStream("/maps/map_list.w4l");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String resource;
			while ((resource = br.readLine()) != null) {
				maps.add(resource);
			}
		}
		catch (Exception e) {
		}
		String[] list = new String[maps.size()];
		for (int i = 0; i < maps.size(); ++i) {
			list[i] = maps.get(i);
		}
		return list;
	}
}