package game;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class Main extends Application {

	public static final String name = "Jones Bones";
	public static final String[] playerAddresses = {
			"10.24.65.10", 	// Oscar
			"10.24.2.217", 	// Frederik
			"10.24.65.135", // Jonas
	};

	public static final int size = 20;
	public static final int scene_height = size * 20 + 100;
	public static final int scene_width = size * 20 + 200;

	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right,hero_left,hero_up,hero_down;

	public static Player me;
	public static List<Player> players = new ArrayList<Player>();

	public Label[][] fields;
	private TextArea scoreList;

	private  String[] board = {    // 20x20
			"wwwwwwwwwwwwwwwwwwww",
			"w        ww        w",
			"w w  w  www w  w  ww",
			"w w  w   ww w  w  ww",
			"w  w               w",
			"w w w w w w w  w  ww",
			"w w     www w  w  ww",
			"w w     w w w  w  ww",
			"w   w w  w  w  w   w",
			"w     w  w  w  w   w",
			"w ww ww        w  ww",
			"w  w w    w    w  ww",
			"w        ww w  w  ww",
			"w         w w  w  ww",
			"w        w     w  ww",
			"w  w              ww",
			"w  w www  w w  ww ww",
			"w w      ww w     ww",
			"w   w   ww  w      w",
			"wwwwwwwwwwwwwwwwwwww"
	};


	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {
		setInstance(this);

		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();

			GridPane boardGrid = new GridPane();

			image_wall  = new Image(getClass().getResourceAsStream("Image/wall4.png"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("Image/heroRight.png"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("Image/heroLeft.png"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("Image/heroUp.png"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("Image/heroDown.png"),size,size,false,false);

			fields = new Label[20][20];
			for (int j=0; j<20; j++) {
				for (int i=0; i<20; i++) {
					switch (board[j].charAt(i)) {
					case 'w':
						fields[i][j] = new Label("", new ImageView(image_wall));
						break;
					case ' ':
						fields[i][j] = new Label("", new ImageView(image_floor));
						break;
					default: throw new Exception("Illegal field value: "+board[j].charAt(i) );
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);


			grid.add(mazeLabel,  0, 0);
			grid.add(scoreLabel, 1, 0);
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);

			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case UP:    localPlayerMoved(0,-1,"up");    break;
				case DOWN:  localPlayerMoved(0,+1,"down");  break;
				case LEFT:  localPlayerMoved(-1,0,"left");  break;
				case RIGHT: localPlayerMoved(+1,0,"right"); break;
				default: break;
				}
			});

            // Setting up standard players

			me = new Player(Main.name,9,4,"up");
			players.add(me);
			fields[9][4].setGraphic(new ImageView(hero_up));

//			Player harry = new Player("Harry",14,15,"up");
//			players.add(harry);
//			fields[14][15].setGraphic(new ImageView(hero_up));

			scoreList.setText(getScoreList());
		} catch(Exception e) {
			e.printStackTrace();
		}

		ConnectionController.getInstance().broadcastCommand(new Command(CMDT.JOIN, new String[]{Main.name, "9", "4", "up"}));
	}

	public void localPlayerMoved(int delta_x, int delta_y, String direction) {
		int x = me.getXpos(), y = me.getYpos();
		me.setXpos(x + delta_x);
		me.setYpos(y + delta_y);
		me.direction = direction;

		ConnectionController.getInstance().broadcastCommand(new Command(CMDT.MOVE, new String[]{Integer.toString(x), Integer.toString(y), direction}));

		updateGraphics();
	}

	public String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : players) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}

	public Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}

	public void updateGraphics() {
		for (Player p : players) {
			String direction = p.getDirection();
			int x = p.getXpos(), y = p.getYpos();

			if (direction.equals("right")) {
				fields[x][y].setGraphic(new ImageView(hero_right));
			};
			if (direction.equals("left")) {
				fields[x][y].setGraphic(new ImageView(hero_left));
			};
			if (direction.equals("up")) {
				fields[x][y].setGraphic(new ImageView(hero_up));
			};
			if (direction.equals("down")) {
				fields[x][y].setGraphic(new ImageView(hero_down));
			};
		}
	}

	public void layFloor(int x, int y) {
		fields[x][y].setGraphic(new ImageView(image_floor));
	}

	public void addPlayer(Player ply) {
		System.out.println("PLAYER " + ply.name + " JOINED!");
		players.add(ply);
		fxInstance.updateGraphics();
	}

	/*
			Commands
	 */
	public static void cmdPlayerJoin(Player player) {
		Platform.runLater(() -> fxInstance.addPlayer(player));
	}

	public static void cmdPlayerMove(int xpos, int ypos, String direction) {

		Player p = fxInstance.getPlayerAt(xpos, ypos);
		if (p != null) {
			Platform.runLater(() -> fxInstance.layFloor(xpos, ypos));

			if (direction.equals("right")) {
				p.setXpos(xpos + 1);
				p.setYpos(ypos);
			};
			if (direction.equals("left")) {
				p.setXpos(xpos - 1);
				p.setYpos(ypos);
			};
			if (direction.equals("up")) {
				p.setXpos(xpos);
				p.setYpos(ypos + 1);
			};
			if (direction.equals("down")) {
				p.setXpos(xpos);
				p.setYpos(ypos - 1);
			};
			p.setDirection(direction);
		}

		Platform.runLater(() -> fxInstance.updateGraphics());
	}

	public static void main(String[] args) throws Exception {
		connect();
		launch(args);
	}

	/*
			Game/Graphics
	 */
	private static Main fxInstance;
	private static void setInstance(Main instance) {
		fxInstance = instance;
	}

	/*
			Connection
	 */
	private static void connect() {
		ConnectionController cc = ConnectionController.getInstance();

		for (String ip : playerAddresses) {
			try {
				InetAddress inet = InetAddress.getByName(ip);
				if (!InetAddress.getLocalHost().equals(inet)) {
					System.out.println("Adding peer: " + ip);
					cc.addPeer(inet);
				}
			} catch (UnknownHostException e) {
				System.out.println("Host error for IP: " + ip);
				e.printStackTrace();
			}
		}

		cc.handshake();
		while (!cc.allConnected());
	}

}

