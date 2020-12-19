package com.javarush.task.task35.task3513;

//Смысл объекта класса MoveEfficiency в том, что в этом объекте у нас хранится:
//1) Ход (который move).
//2) Количество пустых плиток, которое будет на поле после этого хода.
//3) Счет, который будет после этого хода.
//Соответственно, метод getMoveEfficiency() принимает ход move и выдает объект moveEfficiency,
// который описывает состояние игры после этого хода.
//При этом реализация Move будет сделана позже.
public class MoveEfficiency implements Comparable<MoveEfficiency>{
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    //Для того, чтобы эффективности различных ходов можно было сравнивать, необходимо реализовать в классе MoveEfficiency поддержку интерфейса Comparable.
    @Override
    public int compareTo(MoveEfficiency o) {
        //количество пустых плиток сравниваем:
        if(numberOfEmptyTiles > o.numberOfEmptyTiles){
            return 1;
        } else if(numberOfEmptyTiles < o.numberOfEmptyTiles){
            return -1;
            //счет сравниваем:
        } else if (score > o.score){
            return 1;
        } else if (score < o.score){
            return -1;
        }else {
            return 0;
        }
    }
}
