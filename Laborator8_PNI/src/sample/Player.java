package sample;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.awt.*;

public class Player {
    private static Player player = new Player();
    private Node prev;
    private Pozitie[] pozitie = new Pozitie[4];
    private int k;
    private boolean prevVal = true;
    private int score = 0;
    private Player(){

    }
    public int getScore() {
        return score;
    }
    public void setScore(int scor){score = scor;}
    public static Player getPlayer(){return player;}
    public Node getPrev(){return prev;}

    public void movePosible(GridPane pane, int i, int j){
        int x = 0;
        int y = 0;
        for(Node node:pane.getChildren()){
            if(GridPane.getRowIndex(node) == j && GridPane.getColumnIndex(node) == i){
                prev = node;
                k = 0;
                if(j-1>= 0 && i-1 >= 0) {
                    posibleMove(pane, i - 1, j - 1);
                }
                if(j-2>= 0 && i-2 >= 0 && prevVal) {
                    posibleMove(pane, i - 2, j - 2);
                }
                if(j - 1 >= 0 && i+1 < 8) {
                    posibleMove(pane, i + 1, j - 1);
                }
                if(j - 2 >= 0 && i+2 < 8 && prevVal) {
                    posibleMove(pane, i + 2, j - 2);
                }
                //back
                if(j + 1 < 8 && i - 1 >= 0) {
                    posibleMove(pane, i - 1, j + 1);
                }
                if(i-2>= 0 && j+2 < 8 && prevVal) {
                    posibleMove(pane, i - 2, j + 2);
                }
                if(j + 1 < 8 && i+1 < 8) {
                    posibleMove(pane, i + 1, j + 1);
                }
                if(j + 2 <8 && i+2 < 8 && prevVal) {
                    posibleMove(pane, i + 2, j + 2);
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
    public void removePosible(GridPane pane) {
        for(Pozitie poz :pozitie) {
            if(poz!=null) {
                removePosible(pane, poz.getX(), poz.getY());
            }
        }
        pozitie = new Pozitie[4];
    }
    private void posibleMove(GridPane pane,int i,int j){
        Node n = getNodeFromGridPane(pane, i , j);
        StackPane stackPane = (StackPane) n;
        assert stackPane != null;
        ImageView viewIMG = (ImageView) stackPane.getChildren().get(0);
        assert viewIMG != null;
        if (viewIMG.getOpacity() == 0) {
            stackPane.setStyle("-fx-background-color: green");
            pozitie[k++] = new Pozitie(j,i);
            prevVal = false;
        }else{
            prevVal = true;
        }
    }
    private void removePosible(GridPane pane,int i,int j){
        Node n = getNodeFromGridPane(pane, j,i);
        StackPane stackPane = (StackPane) n;
        assert stackPane != null;
        ImageView viewIMG = (ImageView) stackPane.getChildren().get(0);
        assert viewIMG != null;
        if (viewIMG.getOpacity() == 0) {
            stackPane.setStyle("-fx-background-color: brown");
        }
    }
    public boolean positionValid(GridPane pane,Node node){
        int i = GridPane.getRowIndex(node);
        int j = GridPane.getColumnIndex(node);
        for (Pozitie poz : pozitie){
            if(poz !=null) {
                if (poz.getX() == i && poz.getY() == j){
                    return true;
                }

            }
        }
        return false;
    }
    public void removePieceEnemy(GridPane pane,Node curent,Node next){
        int pozCurrentY = GridPane.getRowIndex(curent);
        int pozCurrentX = GridPane.getColumnIndex(curent);
        int pozNextY = GridPane.getRowIndex(next);
        int pozNextX = GridPane.getColumnIndex(next);
        int val1 = pozCurrentX - pozNextX;
        int val2 = pozCurrentY - pozNextY;
        int row = 0;
        int col = 0;
        if (val1 == -2 && val2 == -2) {
            col = pozNextX - 1;
            row = pozNextY - 1;
        } else if (val1 == 2 && val2 == 2) {
            col = pozNextX + 1;
            row = pozNextY + 1;
        } else if (val1 == -2 && val2 == 2) {
            col = pozNextX - 1;
            row = pozNextY + 1;
        } else if (val1 == 2 && val2 == -2) {
            col = pozNextX + 1;
            row = pozNextY - 1;
        }
        if(col !=0 && row != 0) {
            if (enemyPiece(pane, col, row)) {
                removePiece(pane, getNodeFromGridPane(pane, col, row));
                Computer.getComputer().removePieceBoard(col,row);
            }
        }
    }
    private boolean enemyPiece(GridPane pane,int i,int j){
        Node n = getNodeFromGridPane(pane, i , j);
        StackPane stackPane = (StackPane) n;
        assert stackPane != null;
        ImageView viewIMG = (ImageView) stackPane.getChildren().get(0);
        assert viewIMG != null;
        return viewIMG.getOpacity() == 0.95;
    }
    private void removePiece(GridPane gridPane,Node node){
        StackPane stackPane = (StackPane) node;
        assert stackPane != null;
        ImageView viewIMG = (ImageView) stackPane.getChildren().get(0);
        assert viewIMG != null;
        Image img = generateWhite(Color.BLACK);
        viewIMG.setImage(img);
        viewIMG.setOpacity(0);
        viewIMG.setFitWidth(55);
        viewIMG.setFitHeight(55);
        viewIMG.setOnMouseClicked((event)->{
            Player player = Player.getPlayer();
            if(player.positionValid(gridPane,viewIMG.getParent())){
                player.removePosible(gridPane);
                player.removePieceEnemy(gridPane,player.getPrev(),viewIMG.getParent());
                player.movePiece(viewIMG.getParent());
                Laborator.scorePlayer.setText("Score " + player.getScore());
                if(player.getScore() == 8){
                    Laborator.SceneWin("Player Win");
                }else {
                    Computer.getComputer().MoveComputer(gridPane);
                    Laborator.scoreComputer.setText("Score " + Computer.getComputer().getScore());
                    if(Computer.getComputer().getScore() == 8){
                        Laborator.SceneWin("Computer Win");
                    }
                }
            }
        });
        score++;
    }
    private Image generateWhite(Color color){
        WritableImage image = new WritableImage(1,1);
        PixelWriter writer = image.getPixelWriter();
        writer.setColor(0,0, color);
        return image;
    }
    public void movePiece(Node node){
        StackPane next = (StackPane) node;
        StackPane current = (StackPane) prev;
        ImageView nView = (ImageView) next.getChildren().get(0);
        ImageView pView = (ImageView) current.getChildren().get(0);
        next.getChildren().remove(0);
        current.getChildren().remove(0);

        next.getChildren().add(pView);
        current.getChildren().add(nView);

    }
    public int[] makePosition(Node node){
        int[] arr = new int[2];
        arr[0] = GridPane.getRowIndex(node);
        arr[1] = GridPane.getColumnIndex(node);
        return arr;
    }
}
