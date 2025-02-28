package com.example.oop_chess;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static com.example.oop_chess.Game.*;
import static com.example.oop_chess.Move.*;
import static com.example.oop_chess.Save_And_Load.obj;

public class MenuController implements Initializable {
    private String Page = "Play";
    private Stage stage;
    private Parent root;
    Game game;



    @FXML
    private GridPane chessBoard;

    @FXML
    private Button Playbutton;

    @FXML
    private Button Settingsbutton;

    @FXML
    private AnchorPane playMenu;

    @FXML
    private AnchorPane settingsMenu;

    @FXML
    private ChoiceBox<String> themeChoice;

    @FXML
    private ImageView boardView;

    @FXML
    private Label whiteTimerLabel;

    @FXML
    private Label blackTimerLabel;

    @FXML
    private Label whiteMovesLabel;

    @FXML
    private Label blackMovesLabel;

    private final String[] boards = {"Green", "Brown", "Glass", "Pink", "Sky"};

    @FXML
    private AnchorPane resignPane;

    @FXML
    private AnchorPane gameEnd;

    @FXML
    private Label whiteVictoryLabel;

    @FXML
    private Label blackVictoryLabel;

    @FXML
    private Label resignationLabel;

    @FXML
    private Label checkmateLabel;

    @FXML
    private Label timeoutLabel;

    @FXML
    private void openResignMenu(ActionEvent ignoredEvent){
        resignPane.setVisible(true);
    }

    @FXML
    private void closeResignMenu(ActionEvent ignoredEvent){
        resignPane.setVisible(false);
    }

    @FXML
    private void whiteResignation(ActionEvent ignoredEvent) {
        gameEnd.setVisible(true);
        resignationLabel.setVisible(true);
        blackVictoryLabel.setVisible(true);
        resignPane.setVisible(false);

        String musicFile = "src/main/resources/game-end.wav";     // For example

        Media sound3 = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
        mediaPlayer3.play();
    }

    @FXML
    private void blackResignation(ActionEvent ignoredEvent){
        gameEnd.setVisible(true);
        resignationLabel.setVisible(true);
        whiteVictoryLabel.setVisible(true);
        resignPane.setVisible(false);

        String musicFile = "src/main/resources/game-end.wav";     // For example

        Media sound3 = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
        mediaPlayer3.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (themeChoice == null) {
            //? its working correctly just not the way java understands it
        } else {
            themeChoice.setValue(ChessBoard.theme);
            this.changeTheme(new ActionEvent());
            themeChoice.getItems().addAll(boards);
            themeChoice.setOnAction(this::changeTheme);
        }

    }

    private void changeTheme(javafx.event.ActionEvent actionEvent) {           /* Change Theme using choiceBox in settings*/
        String boardName = themeChoice.getValue();
        if (Objects.equals(boardName, "Green")) {
            boardView.setImage(new Image("com/example/oop_chess/assets/GreenBoard.png"));
            ChessBoard.theme = "Green";
        } else if (Objects.equals(boardName, "Brown")) {
            boardView.setImage(new Image("com/example/oop_chess/assets/BrownBoard.png"));
            ChessBoard.theme = "Brown";
        } else if (Objects.equals(boardName, "Glass")) {
            boardView.setImage(new Image("com/example/oop_chess/assets/GlassBoard.png"));
            ChessBoard.theme = "Glass";
        }  else if (Objects.equals(boardName, "Pink")) {
            boardView.setImage(new Image("com/example/oop_chess/assets/PinkBoard.png"));
            ChessBoard.theme = "Pink";
        }   else if (Objects.equals(boardName, "Sky")) {
            boardView.setImage(new Image("com/example/oop_chess/assets/SkyBoard.png"));
            ChessBoard.theme = "Sky";
    }

    }


    @FXML
    private void Play() {
        if (!Page.equals("Play")) {
            if (Playbutton == null || Settingsbutton == null) {
                System.err.println("playButton or settingsButton is null.");
                return;
            }
            // Update button styles
            Playbutton.getStylesheets().clear();
            Settingsbutton.getStylesheets().clear();

            Playbutton.getStylesheets().add("com/example/oop_chess/PlayActive.css");
            Settingsbutton.getStylesheets().add("com/example/oop_chess/PlayActive.css");
            Page = "Play";
            scrollDown();
        }
    }

    @FXML
    private void Settings() {
        if (!Page.equals("Settings")) {
            if (Playbutton == null || Settingsbutton == null) {
                System.err.println("playButton or settingsButton is null.");
                return;
            }
            // Update button styles
            Playbutton.getStylesheets().clear();
            Settingsbutton.getStylesheets().clear();

            Settingsbutton.getStylesheets().add("com/example/oop_chess/SettingActive.css");
            Playbutton.getStylesheets().add("com/example/oop_chess/SettingActive.css");
            Page = "Settings";
            scrollUp();
        }
    }

    @FXML
    private void scrollUp() {
        if (playMenu == null || settingsMenu == null) {
            System.err.println("playMenu or settingsMenu is null.");
            return;
        }

        settingsMenu.setOpacity(1);

        // Play Menu Animation
        TranslateTransition tr1 = new TranslateTransition(Duration.millis(350), playMenu);
        tr1.setFromX(0);
        tr1.setFromY(0);
        tr1.setToX(0);
        tr1.setToY(-1110);

        // Settings Menu Animation
        TranslateTransition tr2 = new TranslateTransition(Duration.millis(350), settingsMenu);
        tr2.setFromX(0);
        tr2.setFromY(1110);
        tr2.setToX(0);
        tr2.setToY(0);

        // Play animations in parallel
        ParallelTransition pt = new ParallelTransition(tr1, tr2);
        pt.play();
    }

    private void scrollDown() {
        if (playMenu == null || settingsMenu == null) {
            System.err.println("playMenu or settingsMenu is null.");
            return;
        }

        // Play Menu Animation
        TranslateTransition tr1 = new TranslateTransition(Duration.millis(350), settingsMenu);
        tr1.setFromX(0);
        tr1.setFromY(0);
        tr1.setToX(0);
        tr1.setToY(1110);

        // Settings Menu Animation
        TranslateTransition tr2 = new TranslateTransition(Duration.millis(350), playMenu);
        tr2.setFromX(0);
        tr2.setFromY(-1110);
        tr2.setToX(0);
        tr2.setToY(0);

        // Play animations in parallel
        ParallelTransition pt = new ParallelTransition(tr1, tr2);
        pt.play();
    }


    @FXML
    void switchToMenuGUI(ActionEvent event) throws Exception {
        check = false;
        wait = false;


        obj.updateState(ChessBoard.theme,ChessBoard.normalChess,Move.moveHistory, timer,(int)whiteTimer.getElapsed(),(int)blackTimer.getElapsed(),casual,currentPlayer,whiteNewMove,blackNewMove);
        Save_And_Load.write();
        Move.moveHistory.clear();


        // Clear all static or global game data
        game.clear();


        // Nullify the game instance
        game = null;

        // Reset casual and normalChess settings
        Game.casual = false;
        ChessBoard.normalChess = true;

        // Load the Menu GUI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MenuGUI.fxml"));
        root = fxmlLoader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);

        stage.show();

        System.out.println("Returned to the main menu and cleared old game data");
    }

    @FXML
    void undo(ActionEvent ignoredEvent) {
        Undo.undoLastMove();
    }



    @FXML
    private Button whiteQueenPromotion;

    @FXML
    private Button whiteRookPromotion;

    @FXML
    private Button whiteBishopPromotion;

    @FXML
    private Button whiteKnightPromotion;

    @FXML
    private Button blackQueenPromotion;

    @FXML
    private Button blackRookPromotion;

    @FXML
    private Button blackBishopPromotion;

    @FXML
    private Button blackKnightPromotion;

    @FXML
    private AnchorPane whitePromotion;

    @FXML
    private AnchorPane blackPromotion;

    @FXML
    private Label drawLabel;

    @FXML
    private Label stalemateLabel;

    @FXML
    private void setNormalButton1(ActionEvent event) throws Exception {
        Game.timer = 180;


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("3:00");
        controller.blackTimerLabel.setText("3:00");
        controller.enterGame(false);

    }

    @FXML
    private void setNormalButton2(ActionEvent event) throws Exception {
        Game.timer = 300;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("5:00");
        controller.blackTimerLabel.setText("5:00");

        controller.enterGame(false);
    }

    @FXML
    private void setNormalButton3(ActionEvent event) throws Exception {
        Game.timer = 600;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();
        // Get the new controller

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("10:00");
        controller.blackTimerLabel.setText("10:00");


        controller.enterGame(false);
    }

    @FXML
    private void setNormalButton4(ActionEvent event) throws Exception {
        Game.timer = 900;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("15:00");
        controller.blackTimerLabel.setText("15:00");

        controller.enterGame(false);
    }

    @FXML
    private void setNormalButton5(ActionEvent event) throws Exception {
        Game.timer = 1800;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("30:00");
        controller.blackTimerLabel.setText("30:00");


        controller.enterGame(false);
    }

    @FXML
    private ImageView infinityLabel;

    @FXML
    private ImageView infinityLabel2;

    @FXML
    private void setNormalButton6(ActionEvent event) throws Exception {
        Game.casual = true;
        timer = 90000;


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.infinityLabel.setVisible(true);
        controller.infinityLabel2.setVisible(true);


        controller.whiteTimerLabel.setText("");
        controller.blackTimerLabel.setText("");
        controller.enterGame(false);
    }

    @FXML
    private void setChaosButton1(ActionEvent event) throws Exception {
        Game.timer = 180;
        ChessBoard.normalChess = false;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("3:00");
        controller.blackTimerLabel.setText("3:00");


        controller.enterGame(false);
    }

    @FXML
    private void setChaosButton2(ActionEvent event) throws Exception {
        Game.timer = 300;
        ChessBoard.normalChess = false;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("5:00");
        controller.blackTimerLabel.setText("5:00");


        controller.enterGame(false);
    }

    @FXML
    private void setChaosButton3(ActionEvent event) throws Exception {
        Game.timer = 600;
        ChessBoard.normalChess = false;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("10:00");
        controller.blackTimerLabel.setText("10:00");

        controller.enterGame(false);
    }

    @FXML
    private void setChaosButton4(ActionEvent event) throws Exception {
        Game.timer = 900;
        ChessBoard.normalChess = false;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.whiteTimerLabel.setText("15:00");
        controller.blackTimerLabel.setText("15:00");

        controller.enterGame(false);
    }

    @FXML
    private void setChaosButton5(ActionEvent event) throws Exception {
        Game.timer = 1800;
        ChessBoard.normalChess = false;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();


        controller.whiteTimerLabel.setText("30:00");
        controller.blackTimerLabel.setText("30:00");

        controller.enterGame(false);
    }

    @FXML
    private void setChaosButton6(ActionEvent event) throws Exception {
        Game.casual = true;
        ChessBoard.normalChess = false;
        timer = 90000;
        
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.infinityLabel.setVisible(true);
        controller.infinityLabel2.setVisible(true);

        controller.whiteTimerLabel.setText("");
        controller.blackTimerLabel.setText("");

        controller.enterGame(false);

    }



    @FXML
    private void loadGame(ActionEvent event) throws Exception {
        
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();
        Save_And_Load.read();
        Game.casual = obj.casual;
        ChessBoard.normalChess = obj.normalChess;
        Move.moveHistory = obj.moveHistory;
        ChessBoard.theme = obj.theme;
        timer = obj.timer;
        currentPlayer = obj.player;
        whiteNewMove = obj.whiteNewMove;
        blackNewMove = obj.blackNewMove;

        if(Game.casual) {
            controller.infinityLabel.setVisible(true);
            controller.infinityLabel2.setVisible(true);
        }

        controller.whiteTimerLabel.setText("");
        controller.blackTimerLabel.setText("");
        
        controller.enterGame(true);
    }
    @FXML
    private void normalAi(ActionEvent event) throws Exception {
        Game.casual = true;
        timer = Integer.MAX_VALUE;
        ChessBoard.normalChess = true;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.infinityLabel.setVisible(true);
        controller.infinityLabel2.setVisible(true);

        controller.whiteTimerLabel.setText("");
        controller.blackTimerLabel.setText("");

        controller.enterGame();
    }

    @FXML
    private void chaosAi(ActionEvent event) throws Exception {
        Game.casual = true;
        timer = Integer.MAX_VALUE;
        ChessBoard.normalChess = false;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GameGUI.fxml"));
        root = fxmlLoader.load();
        MenuController controller = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);


        stage.show();

        controller.infinityLabel.setVisible(true);
        controller.infinityLabel2.setVisible(true);

        controller.whiteTimerLabel.setText("");
        controller.blackTimerLabel.setText("");

        controller.enterGame();
    }





    private void enterGame(boolean load) {
        new Advanced_Techniques(whiteQueenPromotion, whiteRookPromotion, whiteBishopPromotion, whiteKnightPromotion, blackQueenPromotion, blackRookPromotion, blackBishopPromotion, blackKnightPromotion, whitePromotion, blackPromotion);


        if (!load) {
            game = new Game(chessBoard,whiteTimerLabel,blackTimerLabel,false,whiteMovesLabel,blackMovesLabel,false, gameEnd, whiteVictoryLabel, blackVictoryLabel, timeoutLabel, checkmateLabel, stalemateLabel, drawLabel);
        } else {
            game = new Game(chessBoard,whiteTimerLabel,blackTimerLabel,true,whiteMovesLabel,blackMovesLabel,false, gameEnd, whiteVictoryLabel, blackVictoryLabel, timeoutLabel, checkmateLabel, stalemateLabel, drawLabel);
        }


    }
    private void enterGame() {
        new Advanced_Techniques(whiteQueenPromotion, whiteRookPromotion, whiteBishopPromotion, whiteKnightPromotion, blackQueenPromotion, blackRookPromotion, blackBishopPromotion, blackKnightPromotion, whitePromotion, blackPromotion);

        game = new Game(chessBoard,whiteTimerLabel,blackTimerLabel,false,whiteMovesLabel,blackMovesLabel,true, gameEnd, whiteVictoryLabel, blackVictoryLabel, timeoutLabel, checkmateLabel, stalemateLabel, drawLabel);
    }

    @FXML
    private void toggleMute(ActionEvent ignoreEvent){
        muteSound = !muteSound;
    }





    @FXML
    private void Quit() {
        System.exit(0);
    }
}
