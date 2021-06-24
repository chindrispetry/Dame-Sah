package sample;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;

public class Laborator extends Application {

    private static Button btnPlay = new Button("Play");
    private Button btnExit = new Button("Exit ");
    private Image img = new Image(new FileInputStream("src\\sample\\img\\dame.jpg"));
    private static BorderPane panou = new BorderPane();
    public static Label scorePlayer = new Label();
    public static Label scoreComputer = new Label();
    public Laborator() throws FileNotFoundException {
    }

    public BorderPane getPanou() throws FileNotFoundException {
        ImageView btnIMG = new ImageView(new Image(new FileInputStream("src\\sample\\img\\lemn.jpg")));
        btnIMG.setFitHeight(75);
        btnIMG.setFitWidth(50);
        BackgroundImage myBIbtn= new BackgroundImage(btnIMG.snapshot(new SnapshotParameters(),null),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        btnPlay.setFont(new Font(30));
        btnPlay.setStyle("-fx-text-fill: white;");
        btnPlay.setBackground(new Background(myBIbtn));
        btnExit.setFont(new Font(30));
        btnExit.setBackground(new Background(myBIbtn));
        btnExit.setStyle("-fx-text-fill: white;");
        btnPlay.setOnAction((event)->{
            HBox player = new HBox();
            HBox computer = new HBox();
            try {
                ImageView profil = new ImageView(new Image(new FileInputStream("src\\sample\\img\\profil.png")));
                ImageView computerIMG = new ImageView(new Image(new FileInputStream("src\\sample\\img\\computer.png")));
                profil.setFitWidth(50);
                profil.setFitHeight(50);
                computerIMG.setFitWidth(50);
                computerIMG.setFitHeight(50);
                Label numePlayer = new Label("Player");
                scorePlayer.setText("Score " + Player.getPlayer().getScore());
                VBox playerDetails = new VBox(numePlayer,scorePlayer);
                numePlayer.setPadding(new Insets(15,0,0,3));
                numePlayer.setStyle("-fx-text-fill: white;");
                Label numeComputer = new Label("Computer");
                scoreComputer.setText("Score " + Computer.getComputer().getScore());
                VBox computerDetails = new VBox(numeComputer,scoreComputer);
                numeComputer.setPadding(new Insets(15,3,0,0));
                numeComputer.setStyle("-fx-text-fill: white;");
                player = new HBox(profil,playerDetails);
                computer = new HBox(computerDetails,computerIMG);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Button btnPause = new Button("||");
            btnPause.setFont(new Font(15));
            HBox menu = new HBox(player,btnPause,computer);
            menu.setSpacing(186);
            menu.setStyle("-fx-background-color:grey;");
            menu.setPadding(new Insets(2));
            VBox content = new VBox(menu);
            ImageView imgWhite = new ImageView(generateWhite(Color.gray(0.8)));
            imgWhite.setFitHeight(600);
            imgWhite.setFitWidth(500);
            BackgroundImage myBI= new BackgroundImage(imgWhite.snapshot(new SnapshotParameters(),null),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            GridPane table = generateTable();
            table.setPadding(new Insets(0,0,0,80));
            content.getChildren().add(table);

            try {
                addPiece(table,1);
                addPiece(table,0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            panou.setTop(content);
            System.out.println("call play again");
            panou.setBackground(new Background(myBI));
        });
        btnExit.setOnAction((event)->{

        });
        ImageView view = new ImageView(img);
        view.setFitWidth(600);
        view.setFitHeight(500);
        BackgroundImage myBI= new BackgroundImage(view.snapshot(new SnapshotParameters(),null),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        VBox controls = new VBox(btnPlay,btnExit);
        controls.setSpacing(15);
        controls.setPadding(new Insets(160,0,100,250));
        panou.setBackground(new Background(myBI));
        panou.setTop(controls);
        return panou;
    }
    private Image generateWhite(Color color){
        WritableImage image = new WritableImage(1,1);
        PixelWriter writer = image.getPixelWriter();
        writer.setColor(0,0, color);
        return image;
    }
    private GridPane generateTable(){
        GridPane gridPane = new GridPane();
        String color;
        for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                color = filterColor(i,j);
                StackPane pane = new StackPane();
                pane.setStyle("-fx-background-color: " + color );
                ImageView view = new ImageView();
                Image img = generateWhite(Color.BLACK);
                view.setImage(img);
                view.setOpacity(0);
                view.setFitWidth(55);
                view.setFitHeight(55);
                if(color.compareTo("brown") == 0){
                    view.setOnMouseClicked((event)->{
                        Player player = Player.getPlayer();
                        if(player.positionValid(gridPane,view.getParent())){
                            player.removePosible(gridPane);
                            player.removePieceEnemy(gridPane,player.getPrev(),view.getParent());
                            player.movePiece(view.getParent());
                            scorePlayer.setText("Score " + player.getScore());
                            if(player.getScore() == 8){
                                SceneWin("Player Win");
                            }else {
                                Computer.getComputer().MoveComputer(gridPane);
                                scoreComputer.setText("Score " + Computer.getComputer().getScore());
                                if(Computer.getComputer().getScore() == 8){
                                    SceneWin("Computer Win");
                                }
                            }
                        }
                    });
                }
                pane.getChildren().add(view);
                gridPane.add(pane,i,j);
            }
        }
        return  gridPane;
    }
    public static void SceneWin(String s){
        Label win = new Label(s);
        win.setFont(new Font(20));
        Button btnAgain = new Button("Play Again");
        btnAgain.setFont(new Font(15));
        btnAgain.setPadding(new Insets(0,0,0,10));
        btnAgain.setOnAction(btnPlay.getOnAction());
        VBox vBox = new VBox(win,btnAgain);
        vBox.setPadding(new Insets(200,0,200,250));
        Computer.getComputer().Start();
        Player.getPlayer().setScore(0);
        panou.setTop(vBox);
    }
    private void addPiece(GridPane pane,int val) throws FileNotFoundException {
        ImageView pieceWhite = new ImageView(new Image(new FileInputStream("src\\sample\\img\\piece_white.png")));
        ImageView pieceBlack = new ImageView(new Image(new FileInputStream("src\\sample\\img\\piece_black.png")));
        pieceWhite.setFitWidth(50);
        pieceWhite.setFitHeight(50);
        pieceBlack.setFitWidth(50);
        pieceBlack.setFitHeight(50);
        if(val == 1){
            for (int i=0;i<2;i++){
                for (int j=0;j<8;j++){
                    if(i % 2 == 1 && j % 2 == 0){
                        Node node = getNodeFromGridPane(pane,j,i);
                        StackPane panel = (StackPane) node;
                        assert panel != null;
                        ImageView view = (ImageView) panel.getChildren().get(0);
                        assert view != null;
                        view.setImage(pieceWhite.snapshot(new SnapshotParameters(),null));
                        view.setFitWidth(40);
                        view.setFitHeight(40);
                        view.setOpacity(0.95);
                        Computer.getComputer().setPozPieces(new Pozitie(j,i));
                    }else if(i % 2 == 0 && j % 2 == 1){
                        Node node = getNodeFromGridPane(pane,j,i);
                        StackPane panel = (StackPane) node;
                        assert panel != null;
                        ImageView view = (ImageView) panel.getChildren().get(0);
                        assert view != null;
                        view.setImage(pieceWhite.snapshot(new SnapshotParameters(),null));
                        view.setFitWidth(40);
                        view.setFitHeight(40);
                        view.setOpacity(0.95);
                        Computer.getComputer().setPozPieces(new Pozitie(j,i));
                    }
                }
            }
        }else{
            for (int i=7;i>5;i--){
                for (int j=0;j<8;j++){
                    if(i % 2 == 1 && j % 2 == 0){
                        Node node = getNodeFromGridPane(pane,j,i);
                        StackPane panel = (StackPane) node;
                        assert panel != null;
                        ImageView view = (ImageView) panel.getChildren().get(0);
                        assert view != null;
                        view.setImage(pieceBlack.snapshot(new SnapshotParameters(),null));
                        view.setFitWidth(40);
                        view.setFitHeight(40);
                        view.setOpacity(1);
                        view.setOnMouseClicked((event)->{
                            Player player = Player.getPlayer();
                            player.removePosible(pane);
                            int[] arr = player.makePosition(view.getParent());
                            player.movePosible(pane,arr[1],arr[0]);
                        });

                    }else if(i % 2 == 0 && j % 2 == 1){
                        Node node = getNodeFromGridPane(pane,j,i);
                        StackPane panel = (StackPane) node;
                        assert panel != null;
                        ImageView view = (ImageView) panel.getChildren().get(0);
                        assert view != null;
                        view.setImage(pieceBlack.snapshot(new SnapshotParameters(),null));
                        view.setFitWidth(40);
                        view.setFitHeight(40);
                        view.setOpacity(1);
                        view.setOnMouseClicked((event)->{
                            Player player = Player.getPlayer();
                            player.removePosible(pane);
                            int[] arr = player.makePosition(view.getParent());
                            player.movePosible(pane,arr[1],arr[0]);
                        });

                    }
                }
            }
        }
    }
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
    private String filterColor(int i,int j){
        String color;
        if(i %2 == 0) {
            if (j % 2 == 1) {
                color = "brown";
            } else {
                color = "white";
            }
        }else {
            if (j % 2 == 1) {
                color = "white";
            } else {
                color = "brown";
            }
        }
        return color;
    }
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Scene scena = new Scene(getPanou(), 600, 500);
        primaryStage.setScene(scena);
        primaryStage.setTitle("Dame");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

