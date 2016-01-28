/**
 *
 * @author Gustavo Magalhães
 */

import java.awt.*;
import java.awt.event.*;
//import java.net.URI;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.swing.*;
 
/**
 * Based on Conway's game of life, game of happiness simulates 
 * an environment of people and how their happiness changes.
 */

@SuppressWarnings("serial")
public class GameOfHappiness extends JFrame implements ActionListener {
	private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(400, 400);
    private static final int BLOCK_SIZE = 25;
    private JMenuBar mb_menu;
    private JMenu m_game, m_help;
    private JMenuItem mi_game_speed, mi_game_exit;
    private JMenuItem mi_game_happiness_amount, mi_game_play, mi_game_stop, mi_game_reset;
    private JMenuItem mi_help_about, mi_help_source;
    private int i_movesPerSecond = 3;
    private GameBoard gb_gameBoard;
    private Thread game;
 
    public static void main(String[] args) {
        JFrame game = new GameOfHappiness();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setTitle("Game of Happiness");
        game.setSize(DEFAULT_WINDOW_SIZE);
        game.setMinimumSize(MINIMUM_WINDOW_SIZE);
        game.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - game.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - game.getHeight())/2);
        game.setVisible(true);
//        game.setResizable(false);
    }
 
    public GameOfHappiness() {
        mb_menu = new JMenuBar();
        setJMenuBar(mb_menu);
        m_game = new JMenu("Play Game");
        mb_menu.add(m_game);
        m_help = new JMenu("Help");
        mb_menu.add(m_help);
        mi_game_exit = new JMenuItem("Exit");
        mi_game_exit.addActionListener(this);
        mi_game_happiness_amount = new JMenuItem("Happy");
        mi_game_happiness_amount.addActionListener(this);
        mi_game_speed = new JMenuItem("Speed");
        mi_game_speed.addActionListener(this);
        mi_game_play = new JMenuItem("Play");
        mi_game_play.addActionListener(this);
        mi_game_stop = new JMenuItem("Stop");
        mi_game_stop.setEnabled(false);
        mi_game_stop.addActionListener(this);
        mi_game_reset = new JMenuItem("Reset");
        mi_game_reset.addActionListener(this);
        m_game.add(mi_game_happiness_amount);
        m_game.add(mi_game_speed);
        m_game.add(new JSeparator());
        m_game.add(mi_game_play);
        m_game.add(mi_game_stop);
        m_game.add(mi_game_reset);
        m_game.add(mi_game_exit); 
        mi_help_about = new JMenuItem("About");
        mi_help_about.addActionListener(this);
        mi_help_source = new JMenuItem("Source");
        mi_help_source.addActionListener(this);
        m_help.add(mi_help_about);
        m_help.add(mi_help_source);
        gb_gameBoard = new GameBoard();
        add(gb_gameBoard);
    }
 
    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            mi_game_play.setEnabled(false);
            mi_game_stop.setEnabled(true);
            game = new Thread(gb_gameBoard);
            game.start();
        } else {
            mi_game_play.setEnabled(true);
            mi_game_stop.setEnabled(false);
            game.interrupt();
        }
    }
 
	@Override @SuppressWarnings("rawtypes")
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mi_game_exit)) {
            System.exit(0);
        } else if (ae.getSource().equals(mi_game_speed)) {
            final JFrame f_options = new JFrame();
            f_options.setTitle("Speed");
            f_options.setSize(300,60);
            f_options.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_options.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - f_options.getHeight())/2);
            f_options.setResizable(false);
            JPanel p_options = new JPanel();
            p_options.setOpaque(false);
            f_options.add(p_options);
            p_options.add(new JLabel("Number of moves per second:"));
            Integer[] secondOptions = {1,2,3,4,5,10,15,20};
            @SuppressWarnings("unchecked")
			final JComboBox cb_seconds = new JComboBox(secondOptions);
            p_options.add(cb_seconds);
            cb_seconds.setSelectedItem(i_movesPerSecond);
            cb_seconds.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    i_movesPerSecond = (Integer)cb_seconds.getSelectedItem();
                    f_options.dispose();
                }
            });
            f_options.setVisible(true);
        } else if (ae.getSource().equals(mi_game_happiness_amount)) {
            final JFrame f_autoFill = new JFrame();
            f_autoFill.setTitle("Happy");
            f_autoFill.setSize(360, 80);
            f_autoFill.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_autoFill.getWidth())/2, 
                (Toolkit.getDefaultToolkit().getScreenSize().height - f_autoFill.getHeight())/2);
            f_autoFill.setResizable(false);
            JPanel p_autoFill = new JPanel();
            p_autoFill.setOpaque(false);
            f_autoFill.add(p_autoFill);
            p_autoFill.add(new JLabel("Choose the percentage of happiness"));
            Object[] percentageOptions = {"Select",5,10,15,20,25,30,40,50,60,70,80,90,95,100};
            @SuppressWarnings("unchecked")
            final JComboBox cb_percent = new JComboBox(percentageOptions);
            p_autoFill.add(cb_percent);
            cb_percent.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (cb_percent.getSelectedIndex() > 0) {
                        gb_gameBoard.resetBoard();
                        gb_gameBoard.randomlyFillBoard((Integer)cb_percent.getSelectedItem());
                        f_autoFill.dispose();
                    }
                }
            });
            f_autoFill.setVisible(true);
        } else if (ae.getSource().equals(mi_game_reset)) {
            gb_gameBoard.resetBoard();
            gb_gameBoard.repaint();
        } else if (ae.getSource().equals(mi_game_play)) {
            setGameBeingPlayed(true);
        } else if (ae.getSource().equals(mi_game_stop)) {
            setGameBeingPlayed(false);
        } else if (ae.getSource().equals(mi_help_source)) {
            JOptionPane.showMessageDialog(null, "Source is available on GitHub at:\nhttps://github.com/gustavomagalhaes/GameOfLife ", "Source", JOptionPane.INFORMATION_MESSAGE);
        } else if (ae.getSource().equals(mi_help_about)) {
            JOptionPane.showMessageDialog(null, "Modification of Conway's game of life to model the way people's happiness works in an environment.\n"
            		+ "\nThis Java Swing based implementation was modified by Gustavo Magalhães Pereira"
            		+ "\n\nOriginaly created by Matthew Burke."
            		+ "\nhttp://burke9077.com"
            		+ "\nBurke9077@gmail.com"
            		+ "\n@burke9077"
            		+ "\n\nCreative Commons Attribution 4.0 International", "About", JOptionPane.INFORMATION_MESSAGE);
        }
    }
	
    private class GameBoard extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Runnable {
		private Dimension d_gameBoardSize = null;
        private ArrayList<Point> point = new ArrayList<Point>(0);
        private Integer[][] level;
        
        public GameBoard() {
            addComponentListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
        }
 
        private void updateArraySize() {
            ArrayList<Point> removeList = new ArrayList<Point>(0);
            for (Point current : point) {
                if ((current.x > d_gameBoardSize.width-1) || (current.y > d_gameBoardSize.height-1)) {
                    removeList.add(current);
                }
            }
            point.removeAll(removeList);
            repaint();
        }
 
        /**
         * Registra na matriz um ponto sendo utilizado e chama o método repaint() para pintar de acordo com o seu nível de felicidade
         * @param x
         * @param y
         */
        public void addPoint(int x, int y) {
            if (!point.contains(new Point(x,y))) {
                point.add(new Point(x,y));
            } 
            repaint();
        }
        
        /**
         * Necessário para checar e definir os pontos ao iniciar o jogo
         * @param x
         * @param y
         * @return true se o ponto já foi declarado ou false se não tiver sido 
         */
        public boolean checkPoint(int x, int y) {
            if (point.contains(new Point(x,y))) {
                return true;
            } else { 
            	return false;
            }
        }
        
        /**
         * Método responsável por iniciar a matriz com os valores do nível de felicidade de cada célula
         */
        public void startLevel() {
        	level = new Integer[d_gameBoardSize.width][d_gameBoardSize.height];
        	for (int i = 0; i < level.length; i++) {
        		for (int j = 0; j < level[0].length; j++) {
                    level[i][j] = -1;
                }
        	}
        }
        
        @SuppressWarnings("unused")
		public void removePoint(int x, int y) {
            point.remove(new Point(x,y));
        }
 
        public void resetBoard() {
            point.clear();
        }
        
        public int randomPercent(int min, int max) {
        	Random rand = new Random();
        	return rand.nextInt(max - min + 1) + min;
        }
        
        public int getLevel(int x, int y) {
        	return level[x][y];
        }
        
        public void setLevel(int x, int y, int happiness) {
        	level[x][y] = happiness;
        }
        
        /**
         * Método responsável por realizar os cálculos para mudar do nível de felicidade de uma célula
         * @param x
         * @param y
         * @param indicador
         */
        public void updateLevel(int x, int y, Integer[] indicador) {
        	
        	System.out.print(indicador[0]);
        	System.out.print(indicador[1]);
        	System.out.print(indicador[2]);
        	System.out.print(" ");
        }
 
        /**
         * Inicia a matriz com uma porcentagem de células com nível de felicidade acima da média
         * @param percent
         */
        public void randomlyFillBoard(int percent) {
        	startLevel();
        	int total_of_cells = d_gameBoardSize.width*d_gameBoardSize.height;
        	double num_of_cellsDouble = total_of_cells*(percent/100.0);
        	int numOfHappyCells = (int) num_of_cellsDouble;
        	
        	// Filling the happy cells
        	for (int i=0; i < numOfHappyCells; i++) {
        		int x = randomPercent(0,d_gameBoardSize.width-1);
        		int y = randomPercent(0,d_gameBoardSize.height-1);
        		if (!checkPoint(x,y)) {
        			setLevel(x,y,randomPercent(50,100));
                	addPoint(x,y);
        		} else {
        			i--;
        		}
        	}
        	
        	// Filling the sad cells
        	for (int i=0; i<d_gameBoardSize.width; i++) {
                for (int j=0; j<d_gameBoardSize.height; j++) {
                	if (!checkPoint(i,j)) {
                		setLevel(i,j, randomPercent(0,49));
                    	addPoint(i,j);
                	}
                }
        	}
        }
 
        /**
         * Método responsável por pintar a cor de cada célula, chamar o método repaint()
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                for (Point newPoint : point) {
                	
                	Color Green100 = new Color(0,255,0);
                	Color Green90 = new Color(51,255,51);
                	Color Green80 = new Color(102,255,102);
        			Color Green70 = new Color(153,255,153);
    				Color Green60 = new Color(204,255,204);
    				Color Red50 = new Color(255,0,0);
    				Color Red40 = new Color(255,51,51);
    				Color Red30 = new Color(255,102,102);
    				Color Red20 = new Color(255,153,153);
                	Color Red10 = new Color(255,204,204);
                	
                	int range = getLevel(newPoint.x,newPoint.y); 
                	
                	if (range>=90 && range<=100) {g.setColor(Green100);}
                	if (range>=80 && range<90) {g.setColor(Green90);}
                	if (range>=70 && range<80) {g.setColor(Green80);}
        			if (range>=60 && range<70) {g.setColor(Green70);}
    				if (range>=50 && range<60) {g.setColor(Green60);}
    				if (range>=40 && range<50) {g.setColor(Red50);}
    				if (range>=30 && range<40) {g.setColor(Red40);}
    				if (range>=20 && range<30) {g.setColor(Red30);}
    				if (range>=10 && range<20) {g.setColor(Red20);}
                	if (range>=0 && range<10) {g.setColor(Red10);}
                    
                    g.fillRect(BLOCK_SIZE + (BLOCK_SIZE*newPoint.x), BLOCK_SIZE + (BLOCK_SIZE*newPoint.y), BLOCK_SIZE, BLOCK_SIZE);
                }
            } catch (ConcurrentModificationException cme) {}
            
            g.setColor(Color.GRAY);
            for (int i=0; i<=d_gameBoardSize.width; i++) {
                g.drawLine(((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE, (i*BLOCK_SIZE)+BLOCK_SIZE, BLOCK_SIZE + (BLOCK_SIZE*d_gameBoardSize.height));
            }
            for (int i=0; i<=d_gameBoardSize.height; i++) {
                g.drawLine(BLOCK_SIZE, ((i*BLOCK_SIZE)+BLOCK_SIZE), BLOCK_SIZE*(d_gameBoardSize.width+1), ((i*BLOCK_SIZE)+BLOCK_SIZE));
            }
        }
 
        @Override
        public void componentResized(ComponentEvent e) {
            d_gameBoardSize = new Dimension(getWidth()/BLOCK_SIZE-2, getHeight()/BLOCK_SIZE-2);
            updateArraySize();
        }
        
        @Override
        public void componentMoved(ComponentEvent e) {}
        
        @Override
        public void componentShown(ComponentEvent e) {}
        
        @Override
        public void componentHidden(ComponentEvent e) {}
        
        @Override
        public void mouseClicked(MouseEvent e) {}
        
        @Override
        public void mousePressed(MouseEvent e) {}
        
        @Override
        public void mouseReleased(MouseEvent e) {
            // Mouse was released (user clicked)
//            addPoint(e);
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {}
 
        @Override
        public void mouseExited(MouseEvent e) {}
 
        @Override
        public void mouseDragged(MouseEvent e) {
            // Mouse is being dragged, user wants multiple selections
//            addPoint(e);
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {}
 
        /**
         * Método responsável por verificar a vizinhança de Moore e definir os indicadores
         */
        @Override
        public void run() {
        	Integer[] indicadores = new Integer[3];
            for (int i=1; i<level.length-1; i++) {
                for (int j=1; j<level[0].length-1; j++) {
                    int neighbors = 0;
                    if (getLevel(i-1,j-1) >= 50) { neighbors++; }
                    if (getLevel(i-1,j) >= 50)   { neighbors++; }
                    if (getLevel(i-1,j+1) >= 50) { neighbors++; }
                    if (getLevel(i,j-1) >= 50)   { neighbors++; }
                    if (getLevel(i,j+1) >= 50)   { neighbors++; }
                    if (getLevel(i+1,j-1) >= 50) { neighbors++; }
                    if (getLevel(i+1,j) >= 50)   { neighbors++; }
                    if (getLevel(i+1,j+1) >= 50) { neighbors++; }
                    
                    // Média da felicidade dos vizinhos
                    indicadores[0] = (getLevel(i-1,j-1)+getLevel(i-1,j)+getLevel(i-1,j+1)+getLevel(i,j-1)+getLevel(i,j+1)+getLevel(i+1,j-1)+getLevel(i+1,j)+getLevel(i+1,j+1))/8;
                    
                    // Quantidade de vizinhos felizes
                    indicadores[1] = neighbors;
                    
                    // Quantidade de vizinhos tristes
                    indicadores[2] = 8-neighbors;
                    
                    updateLevel(i,j,indicadores);
                }
            }
            repaint();
            try {
                Thread.sleep(1000/i_movesPerSecond);
                run();
            } catch (InterruptedException ex) {}
        }
    }
}
