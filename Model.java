package com.javarush.task.task35.task3513;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//класс ответственен за все манипуляции производимые с игровым полем.
public class Model {
    private final static int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    //поля score и maxTile типа int, которые должны хранить текущий счет и максимальный вес плитки на игровом поле.
    int score;
    int maxTile;

    public Model(){
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    //8:
    //2. Добавь в класс Model метод canMove возвращающий true в случае,
    // если в текущей позиции возможно сделать ход так, чтобы состояние игрового поля изменилось.
    // Иначе - false.
    public boolean canMove(){

        for(int i = 0; i < FIELD_WIDTH; i++){
            for(int j = 0; j < FIELD_WIDTH; j++){
                if(gameTiles[i][j].isEmpty()){
                    //"...если на игровом поле есть хотя бы одна свободная клетка,
                    return true;
                }
            }
        }

        //ИЛИ если есть плитки соприкасающиеся друг с другом и при этом имеющие одинаковые значения
        //1. одинаковые значения по горизонтали:
        for(int i = 0; i < FIELD_WIDTH; i++){
            for(int j = 0; j < FIELD_WIDTH - 1; j++){
                if(gameTiles[i][j].value == gameTiles[i][j+1].value){
                    return true;
                }
            }
        }
        //1. одинаковые значения по горизонтали:
        for(int i = 0; i < FIELD_WIDTH - 1; i++){
            for(int j = 0; j < FIELD_WIDTH; j++){
                if(gameTiles[i][j].value == gameTiles[i+1][j].value){
                    return true;
                }
            }
        }

        return false;
    }

    //P.S. Пожалуй стоит весь код из конструктора переместить в метод resetGameTiles,
// для того, чтобы при необходимости/начать новую игру,
// не приходилось создавать новую модель,
// а можно было бы просто вернуться в начальное состояние вызвав его.
// Уровень доступа должен быть шире приватного.
    void resetGameTiles(){
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for(int i = 0; i < FIELD_WIDTH; i++){
            for(int j = 0; j < FIELD_WIDTH; j++){
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    //приватный метод addTile, который будет смотреть какие плитки пустуют и, если такие имеются,
    //менять вес одной из них, выбранной случайным образом, на 2 или 4
    // (на 9 двоек должна приходиться 1 четверка).
    private void addTile(){
        List<Tile> list = getEmptyTiles();
        if(list.isEmpty()){
            return;
        }
        int randomIndex = (int)(list.size() * Math.random()); //выбор индекса случайной пустой плитки
        int newValue = Math.random() < 0.9 ? 2 : 4; //новое значение value
        //list.set(randomIndex, new Tile(newValue));
        Tile tile = list.get(randomIndex);
        tile.value = newValue;

    }


    //1. Метод getEmptyTiles должен возвращать список пустых плиток в массиве gameTiles.
    //Также получение свободных плиток можно вынести в отдельный приватный метод getEmptyTiles,
    // возвращающий список
    //свободных плиток в массиве gameTiles.
    private List<Tile> getEmptyTiles(){
        List<Tile> list = new ArrayList<>();
        for(int i = 0; i < FIELD_WIDTH; i++){
            for(int j = 0; j < FIELD_WIDTH; j++){
                if(gameTiles[i][j].isEmpty())
                    list.add(gameTiles[i][j]);
            }
        }
        return list;
    }

    //5:
    //движение влево:
    //Сжатие плиток, таким образом, чтобы все пустые плитки были справа,
    //         т.е. ряд {4, 2, 0, 4}
    // становится рядом {4, 2, 4, 0}
    //6: 1. Изменим метод compressTiles, чтобы он возвращал true в случае, если он вносил изменения во входящий массив, иначе - false.
    private boolean compressTiles(Tile[] tiles){
        boolean flag = false;
        //left <---
        //это только для одного ряда:
        //двигаем только нулевые плитки вправо, поэтому последнюю ячейку не двигаем:
        //int i = 0;
            for(int j = 0; j < tiles.length; j++) {
                for (int i = 0; i < tiles.length - 1; i++) {
                    //если плитка нулевая: меняем её с той, что правее.
                    if (tiles[i].value == 0) {
                        //меняем ее местами с правой. Она уходит на 1 индекс левее.
                        if(tiles[i+1].value != 0){
                            flag = true;
                        }
                        tiles[i].value = tiles[i].value ^ tiles[i + 1].value;
                        tiles[i + 1].value = tiles[i].value ^ tiles[i + 1].value;
                        tiles[i].value = tiles[i].value ^ tiles[i + 1].value;
                    }
                }
            }
            return flag;
    }

    //б) Слияние плиток одного номинала, т.е. ряд {4, 4, 2, 0} становится рядом {8, 2, 0, 0}.
    //Обрати внимание, что ряд {4, 4, 4, 4}
    //           превратится в {8, 8, 0, 0},
    //           а {4, 4, 4, 0}
    //           в {8, 4, 0, 0}.
    //6:  возвращал true в случае, если он вносил изменения во входящий массив, иначе - false.
    private boolean mergeTiles(Tile[] tiles){
        boolean flag = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            //если плитка с индексом 1 имеет то же значение, что плитка с индексом 2:.
            if(tiles[i].value != 0 & tiles[i+1].value != 0) {
                if (tiles[i].value == tiles[i + 1].value) {
                    //сложим их значения и запишем в первую плитку.
                    tiles[i].value = tiles[i + 1].value + tiles[i].value;
                    flag = true;
                    //записать в поле с плиткой максимального веса новое значение:
                    if (tiles[i].value > maxTile) {
                        maxTile = tiles[i].value;
                    }
                    // Увеличиваем значение поля score на величину веса плитки образовавшейся в результате слияния.
                    score += tiles[i].value;
                    tiles[i + 1].value = 0;
                    i++;
                }
            }
        }
        compressTiles(tiles);
        return flag;
    }

    //6:
    //3. Реализуем метод left, который будет для каждой строки массива gameTiles
    // вызывать методы compressTiles и mergeTiles и добавлять одну плитку с помощью метода addTile
    // в том случае, если это необходимо.
    //4. Метод left не должен быть приватным, т.к. вызваться он будет, помимо прочего,
    // из класса Controller.
    void left(){
        for(int i = 0; i < gameTiles.length; i++){
            boolean compressed = compressTiles(gameTiles[i]);
            boolean merged = mergeTiles(gameTiles[i]);
            if(compressed | merged){
                addTile();
            }
        }
    }

    //7: теперь необходимо реализовать методы right, up, down.
    // если повернуть двумерный массив на 90 градусов по часовой стрелке, сдвинуть влево,
    //
    // а потом еще трижды выполнить поворот?
    void up(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        left();
        rotateClockwise();
    }

    void down(){
        rotateClockwise();
        left();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    void right(){
        rotateClockwise();
        rotateClockwise();
        left();
        rotateClockwise();
        rotateClockwise();
    }

    //вспомогательный метод поворота на 90 градусов по часовой стрелке:
    private void rotateClockwise(){
        //example:
        //    for (int i = 0; i < n; ++i) {
        //        for (int j = 0; j < n; ++j) {
        //            ret[i, j] = matrix[n - j - 1, i];
        //        }
        //    }

        //new[i][j] = old[FIELD_WIDTH - 1 - j][i]
        //Tile[][] tempTiles = gameTiles;
//        for(int i = 0; i < gameTiles.length; i++){
//            for(int j = 0; j < gameTiles.length; j++){
//                gameTiles[i][j].value = gameTiles[i][j].value ^ gameTiles[gameTiles.length - j - 1][i].value;
//                gameTiles[gameTiles.length - j - 1][i].value = gameTiles[i][j].value ^ gameTiles[gameTiles.length - j - 1][i].value;
//                gameTiles[i][j].value = gameTiles[i][j].value ^ gameTiles[gameTiles.length - j - 1][i].value;
//                int temp = tempTiles[i][j].value;
//                tempTiles[i][j].value = tempTiles[tempTiles.length - j - 1][i].value;
//                tempTiles[tempTiles.length - j - 1][i].value = temp;
//            }
//        }
        //return tempTiles;

        //создаю новый пустой массив, куда запишу потом "повернутеы" значения:
        Tile[][] tempTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        tempTiles[0][3] = gameTiles[0][0];
        tempTiles[1][3] = gameTiles[0][1];
        tempTiles[2][3] = gameTiles[0][2];
        tempTiles[3][3] = gameTiles[0][3];

        tempTiles[0][2] = gameTiles[1][0];
        tempTiles[1][2] = gameTiles[1][1];
        tempTiles[2][2] = gameTiles[1][2];
        tempTiles[3][2] = gameTiles[1][3];

        tempTiles[0][1] = gameTiles[2][0];
        tempTiles[1][1] = gameTiles[2][1];
        tempTiles[2][1] = gameTiles[2][2];
        tempTiles[3][1] = gameTiles[2][3];

        tempTiles[0][0] = gameTiles[3][0];
        tempTiles[1][0] = gameTiles[3][1];
        tempTiles[2][0] = gameTiles[3][2];
        tempTiles[3][0] = gameTiles[3][3];

        gameTiles = tempTiles;

    }

    ////////////////////////////testing/////////////////////////////


//    public static void main(String[] args) {
//        Model m = new Model();
//        Tile[] tiles = {new Tile(4), new Tile (4), new Tile(4), new Tile(4)};
//        m.mergeTiles(tiles);
//        for(int i = 0; i < tiles.length; i++){
//            System.out.print(tiles[i]);
//            //4800 expected
//        }


//        m.resetGameTiles();
//        for(int i = 0; i < m.gameTiles.length; i++){
//            for(int j = 0; j < m.gameTiles.length; j++){
//                System.out.print(m.gameTiles[i][j].value + "  ");
//            }
//            System.out.println();
//        }

//        //тестирование rotateClockwise():
//        m.rotateClockwise();
//
//        System.out.println("===after rotation===");
//
//        for(int i = 0; i < m.gameTiles.length; i++){
//            for(int j = 0; j < m.gameTiles.length; j++){
//                System.out.print(m.gameTiles[i][j] + "  ");
//            }
//            System.out.println();
//        }

        //тестирование down():
//        System.out.println("===after right()===");
//
//        m.right();
//
//        for(int i = 0; i < m.gameTiles.length; i++){
//            for(int j = 0; j < m.gameTiles.length; j++){
//                System.out.print(m.gameTiles[i][j].value + "  ");
//            }
//            System.out.println();
//        }

//    }
}
