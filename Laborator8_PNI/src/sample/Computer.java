package sample;

import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.*;

public class Computer {
    private static Computer computer = new Computer();
    private Pozitie[] pozitie = new Pozitie[4];
    private Pozitie[] pozPieces = new Pozitie[8];
    private Map<Pozitie, List<Pozitie>> pozPosible = new TreeMap<>();
    private int k = 0;
    private boolean prevVal = true;
    private int score = 0;
    private Computer(){

    }

    public int getScore() {
        return score;
    }
    public void Start(){
        k = 0;
        pozPieces = new Pozitie[8];
        score = 0;
    }
    public void removePieceBoard(int col, int row){

        for (int i = 0;i<pozPieces.length;i++){
            if(pozPieces[i] != null) {
                if (pozPieces[i].getX() == col && pozPieces[i].getY() == row) {
                    pozPieces[i] = null;
                    break;
                }
            }
        }
        Pozitie[] copy = new Pozitie[pozPieces.length - 1];
        int k = 0;
        int j = 0;
        while(k < pozPieces.length){
            if(pozPieces[k] != null){
                copy[j++] = pozPieces[k];
            }
            k += 1;
        }
        pozPieces = copy;
    }

    public void MoveComputer(GridPane pane){
        for (Pozitie poz :pozPieces){
            if(poz != null) {
                movePosible(pane, poz.getX(), poz.getY());
                calculatePosibleMove(pane, poz);
            }
        }
        executeMove(pane);

    }

    private void executeMove(GridPane pane){
        Pozitie max = null;
        for(Pozitie poz:pozPosible.keySet()){
            if((max == null || poz.getPrioritate() > max.getPrioritate()) && pozPosible.get(poz)!= null){
                max = poz;
            }
        }
        List<Pozitie> lst = pozPosible.get(max);
        Pozitie last = max;
        Pozitie curent = max;
        boolean valid = false;
        if(lst != null) {
            for (Pozitie pozitie1 : lst) {
                if(pozitie1.getX() != curent.getX() || pozitie1.getY() != curent.getY()) {
                    movePiece(getNodeFromGridPane(pane, pozitie1.getX(), pozitie1.getY()), getNodeFromGridPane(pane, curent.getX(), curent.getY()));
                    removePieceEnemy(pane, curent, pozitie1);
                    curent.setX(pozitie1.getX());
                    curent.setY(pozitie1.getY());
                    valid = true;
                }
            }
            if(valid) {
                Pozitie copy = new Pozitie(curent.getX(), curent.getY());
                for (Pozitie pozPiece : pozPieces) {
                    if (pozPiece != null) {
                        if (pozPiece.getX() == last.getX() && pozPiece.getY() == last.getY()) {
                            pozPiece.setX(copy.getX());
                            pozPiece.setY(copy.getY());
                        }
                    }
                }
            }
        }
        pozPosible = new TreeMap<>();
    }
    public void removePieceEnemy(GridPane pane,Pozitie curent,Pozitie next){
        int val1 = next.getX() -  curent.getX();
        int val2 = next.getY() - curent.getY();
        int row = 0;
        int col = 0;
        if (val1 == -2 && val2 == -2) {
            col = next.getY() - 1;
            row = next.getX() - 1;
        } else if (val1 == 2 && val2 == 2) {
            col = next.getY() - 1;
            row = next.getX() - 1;
        } else if (val1 == -2 && val2 == 2) {
            col = next.getY() - 1;
            row = next.getX() + 1;
        } else if (val1 == 2 && val2 == -2) {
            col = next.getY() + 1;
            row = next.getX() - 1;
        }
        if(col !=0 && row != 0) {
            if (enemyPiece(pane, row, col)) {
                removePiece(pane, getNodeFromGridPane(pane, row, col));
            }
        }
    }
    private void calculatePosibleMove(GridPane pane,Pozitie poz) {
        for(Pozitie pozitie1 :pozitie) {
            if (pozitie1 != null) {
                List<Pozitie> pozitieList = new ArrayList<>();
                if (poz.getY() - pozitie1.getX() == 2 ||
                        poz.getX() - pozitie1.getY() == 2 ||
                        poz.getY() - pozitie1.getX() == -2 ||
                        poz.getX() - pozitie1.getY() == -2) {
                    pozitieList.add(poz);
                    while (true) {
                        Pozitie pozitie = pozitieList.get(pozitieList.size()-1);
                        if ((pozitie.getX() + 1 < 8 && pozitie.getY() + 1 < 8 && pozitie.getX() + 2 < 8 && pozitie.getY() + 2 < 8) && (enemyPiece(pane, pozitie.getX() + 1, pozitie.getY() + 1) || !emptyPozition(pane, pozitie.getX() + 1, pozitie.getY() + 1))) {
                            if (emptyPozition(pane, pozitie.getX() + 2, pozitie.getY() + 2)) {
                                    if(!pozitieList.contains(new Pozitie(pozitie.getX() + 2, pozitie.getY() + 2))) {
                                        pozitieList.add(new Pozitie(pozitie.getX() + 2, pozitie.getY() + 2));
                                    }else{ break; }
                                }else{break;}
                        } else if ((pozitie.getY() + 1 < 8 && pozitie.getX() - 2 >= 0 && pozitie.getX() - 1 >= 0 && pozitie.getY() + 2 < 8) && (enemyPiece(pane, pozitie.getX() - 1, pozitie.getY() + 1)|| !emptyPozition(pane, pozitie.getX() - 1, pozitie.getY() + 1))) {
                            if (emptyPozition(pane, pozitie.getX() - 2, pozitie.getY() + 2)) {
                                    if(!pozitieList.contains(new Pozitie(pozitie.getX() - 2, pozitie.getY() + 2))) {
                                        pozitieList.add(new Pozitie(pozitie.getX() - 2, pozitie.getY() + 2));
                                    }else{ break;}
                                }else{break;}
                        } else if ((pozitie.getY() - 1 >= 0 && pozitie.getX() + 1 < 8 && pozitie.getY() - 2 >= 0 && pozitie.getX() + 2 < 8) && (enemyPiece(pane, pozitie.getX() + 1, pozitie.getY() - 1)|| !emptyPozition(pane, pozitie.getX() + 1, pozitie.getY() - 1))) {
                            if (emptyPozition(pane, pozitie.getX() + 2, pozitie.getY() - 2)) {
                                    if(!pozitieList.contains(new Pozitie(pozitie.getX() + 2, pozitie.getY() - 2))) {
                                        pozitieList.add(new Pozitie(pozitie.getX() + 2, pozitie.getY() - 2));
                                    }else {break;}
                                }else{break;}
                        } else if ((pozitie.getY() - 1 >= 0 && pozitie.getX() - 1 >= 0 && pozitie.getY() - 2 >= 0 && pozitie.getX() - 2 >= 0) && (enemyPiece(pane, pozitie.getX() - 1, pozitie.getY() - 1)|| !emptyPozition(pane, pozitie.getX() - 1, pozitie.getY() - 1))) {
                            if (emptyPozition(pane, pozitie.getX() - 2, pozitie.getY() - 2)) {
                                    if(!pozitieList.contains(new Pozitie(pozitie.getX() - 2, pozitie.getY() - 2))) {
                                        pozitieList.add(new Pozitie(pozitie.getX() - 2, pozitie.getY() - 2));
                                    }else{break;}
                                }else{break;}
                        }else{
                            break;
                        }
                    }
                    if (pozitieList.size() > 1) {
                        poz.setPrioritate(pozitieList.size());
                        pozitieList.remove(0);
                    }
                } else {
                    pozitieList.add(new Pozitie(pozitie1.getY(),pozitie1.getX()));
                    poz.setPrioritate(pozitieList.size());
                }
                if(pozitieList.size() > 0) {
                    pozPosible.put(poz, pozitieList);
                }
            }
        }
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
    private boolean enemyPiece(GridPane pane,int i,int j){
        Node n = getNodeFromGridPane(pane, i, j);
        StackPane stackPane = (StackPane) n;
        assert stackPane != null;
        ImageView viewIMG = (ImageView) stackPane.getChildren().get(0);
        assert viewIMG != null;
        return viewIMG.getOpacity() == 1;
    }
    private boolean emptyPozition(GridPane pane,int i,int j){
        Node n = getNodeFromGridPane(pane, i , j);
        StackPane stackPane = (StackPane) n;
        assert stackPane != null;
        ImageView viewIMG = (ImageView) stackPane.getChildren().get(0);
        assert viewIMG != null;
        return viewIMG.getOpacity() == 0;
    }
    public void movePosible(GridPane pane, int i, int j){
        pozitie = new Pozitie[4];
        for(Node node:pane.getChildren()){
            if(GridPane.getRowIndex(node) == j && GridPane.getColumnIndex(node) == i){
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
    private void posibleMove(GridPane pane,int i,int j){
        Node n = getNodeFromGridPane(pane, i , j);
        StackPane stackPane = (StackPane) n;
        assert stackPane != null;
        ImageView viewIMG = (ImageView) stackPane.getChildren().get(0);
        assert viewIMG != null;
        if (viewIMG.getOpacity() == 0) {
            pozitie[k++] = new Pozitie(j,i);
            prevVal = false;
        }else{
            prevVal = true;
        }
    }
    public static Computer getComputer(){return computer;}
    public void setPozPieces(Pozitie poz){
        pozPieces[k++] = poz;
    }
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
    private boolean positionValid(Node node){
        int i = GridPane.getRowIndex(node);
        int j = GridPane.getColumnIndex(node);
        for (Pozitie poz : pozitie){
            if(poz !=null) {
                if (poz.getX() == i && poz.getY() == j)
                    return true;
            }
        }
        return false;
    }
    private void movePiece(Node node,Node prev){
        StackPane next = (StackPane) node;
        StackPane current = (StackPane) prev;
        ImageView pView = (ImageView) current.getChildren().remove(0);
        ImageView nView = (ImageView) next.getChildren().remove(0);

        next.getChildren().add(pView);
        current.getChildren().add(nView);

    }
}
