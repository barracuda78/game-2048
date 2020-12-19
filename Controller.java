package com.javarush.task.task35.task3513;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private Model model;



    private View view;
    private static  final int WINNING_TILE = 2048;

    public Controller(Model model){
        this.model = model;
        view = new View(this);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //super.keyPressed(e);
        //1. Если была нажата клавиша ESC - вызови метод resetGame.
        if(e.getKeyCode() == 0x1B){
            resetGame();
        }
        //2. Если метод canMove модели возвращает false - установи флаг isGameLost в true.
        if(!model.canMove()){
            view.isGameLost = true;
        }
        //3. Если оба флага isGameLost и isGameWon равны false - обработай варианты движения:
        //а) для клавиши KeyEvent.VK_LEFT вызови метод left у модели;
        //б) для клавиши KeyEvent.VK_RIGHT вызови метод right у модели;
        //в) для клавиши KeyEvent.VK_UP вызови метод up у модели;
        //г) для клавиши KeyEvent.VK_DOWN вызови метод down у модели.
        if(!view.isGameLost & !view.isGameWon){
            int keyCode = e.getKeyCode();
            switch(keyCode){
                case KeyEvent.VK_LEFT:
                    model.left();
                    break;
                case KeyEvent.VK_RIGHT:
                    model.right();
                    break;
                case KeyEvent.VK_UP:
                    model.up();
                    break;
                case KeyEvent.VK_DOWN:
                    model.down();
                    break;
            }
        }
        //4. Если поле maxTile у модели стало равно WINNING_TILE, установи флаг isGameWon в true.
        if(model.maxTile == WINNING_TILE){
            view.isGameWon = true;
        }
        //5. В самом конце, вызови метод repaint у view.
        view.repaint();

    }

    public void resetGame(){
        model.score = 0;
        view.isGameLost = false;
        view.isGameWon = false;
        model.resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore(){
        return model.score;
    }

    public View getView() {
        return view;
    }
}
