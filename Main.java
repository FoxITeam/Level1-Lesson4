package com.company;


import java.util.Random;
import java.util.Scanner;

/**
 *
 * Максим Ресчиков・Наставник
 while (checkGameLoop()) { А так довольно своебразный алгоритм
 if (trigger) {
 currentDot = F_DOT_X;
 progressPlayer();
 } else {
 currentDot = FOX_DOT_O;
 progressMachine();
 }

 trigger = !trigger; да не знаю что сказать, я с таким первый раз сталкиваюсь
 printTheMap();
 actionCounter++;
 }
 здесь наверное напрасно, каждый раз присваивать значение переменнй currentDot. Проще передавать в метод, параметр: progressPlayer(F_DOT_X); и соответственно

 progressMachine(FOX_DOT_O);
 private static int[] progressInput() {
 int input[] = new int[2]; я конечно в данном случае понимаю откуда 2, но так писать все равно не рекомендуется
 int i = 0;
 do{
 while (!sc.hasNextInt()) {
 sc.next();
 }
 input[i] = sc.nextInt() - 1;
 i++;
 }while (i < input.length);
 return input;
 }

 Я бы так запонил массив входных координат игрока, не нужно что б код повторялся
 private static void progressPlayer() {
 int y;
 int x;

 do {
 System.out.printf("Ваш ход [%c], Введите Y X: ", currentDot);
 int[] input = progressInput();
 y = input[0]; Это тоже хардкор надо придумать как по другому присвоить значения
 x = input[1];
 } while (!progressAction(y, x)); желательно переделать метод progressAction(y, x), что бы можно было обойтись в условии while без !(не).

 tiles[y][x] = currentDot;
 }
 вот смотрите у вас в методе progressAction(int y, int x) условие if (x < 0 || y < 0 || x >= NUM || y >= NUM) return false;

 и в методах: checkRightUp(int y, int x), checkRight(int y, int x), checkRightDown(int y, int x), checkDown(int y, int x) то же часть условия одинакова плюс
 в этих самих методах условие if (y < 0 || y >= NUM || x < 0 || x >= NUM || currentDot != tiles[y][x]) return false; один в один, что придумать чтоб код не повторялся?
 *
 */


// todo | 1. Полностью разобраться с кодом, попробовать переписать с нуля, стараясь не подглядывать в методичку;
// todo | 2. Переделать проверку победы, чтобы она не была реализована просто набором условий, например, с использованием циклов.
// todo | 3. * Попробовать переписать логику проверки победы, чтобы она работала для поля 5х5 и количества фишек 4. Очень желательно не делать это просто набором условий для каждой из возможных ситуаций;
// todo | 4. *** Доработать искусственный интеллект, чтобы он мог блокировать ходы игрока.

public class Main {
    private static char[][] tiles;
    private static final int NUM = 5; // todo Игра 5х5 клеток.
    //private static final int NUM = 3; // todo игра 3х3 клеток.
    private static final int FOX_ACTION_SUM = NUM * NUM;
    private static final int FOX_DOTS_TO_WIN = NUM;
    private static final char FOX_DOT_EMPTY = '•';
    private static final char F_DOT_X = 'X';
    private static final char FOX_DOT_O = 'O';
    private static Scanner sc = new Scanner(System.in);
    private static Random random = new Random();
    private static boolean trigger = true;
    private static int actionCounter = 0;
    private static char currentDot;

    public static void main(String[] args) {
        fillingMap();
        printTheMap();

        while (checkGameLoop()) {
            if (trigger) {
                currentDot = F_DOT_X;
                progressPlayer();
            } else {
                currentDot = FOX_DOT_O;
                progressMachine();
            }

            trigger = !trigger;
            printTheMap();
            actionCounter++;
        }

    }

    private static void fillingMap() {
        tiles = new char[NUM][NUM];

        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                tiles[i][j] = FOX_DOT_EMPTY;
            }
        }
    }

    private static void printTheMap() {
        System.out.printf("   ");
        for (int i = 0; i < NUM; i++) {
            System.out.printf("%2d ", i + 1);
        }

        System.out.println();

        for (int i = 0; i < NUM; i++) {
            System.out.printf("%2d ", i + 1);
            for (int j = 0; j < NUM; j++) {
                System.out.printf("%2c ", tiles[i][j]);
            }
            System.out.println();
        }

        System.out.println();
    }

    private static int[] progressInput() {
        int input[] = new int[2];

        while (!sc.hasNextInt()) {
            sc.next();
        }

        while (!sc.hasNextInt()) {
            sc.next();
        }

        input[0] = sc.nextInt() - 1;
        input[1] = sc.nextInt() - 1;

        return input;
    }

    private static void progressPlayer() {
        int y;
        int x;

        do {
            System.out.printf("Ваш ход [%c], Введите Y X: ", currentDot);
            int[] input = progressInput();
            y = input[0];
            x = input[1];
        } while (!progressAction(y, x));

        tiles[y][x] = currentDot;
    }

    private static void progressMachine() {
        int y;
        int x;

        System.out.printf("Ходит AI [%c]", currentDot);

        do {
            y = random.nextInt(NUM);
            x = random.nextInt(NUM);
        } while (!progressAction(y, x));

        System.out.printf(" (y: %d, x: %d)\n", y + 1, x + 1);

        tiles[y][x] = currentDot;
    }

    private static boolean progressAction(int y, int x) {
        if (x < 0 || y < 0 || x >= NUM || y >= NUM) return false;
        if (FOX_DOT_EMPTY == tiles[y][x]) return true;
        return false;
    }

    private static boolean mainLoop() {
        for (int i = 0; i < NUM; i++) {
            for (int j = 0; j < NUM; j++) {
                if (checkRightUp(i, j)) return false;
                if (checkRight(i, j)) return false;
                if (checkRightDown(i, j)) return false;
                if (checkDown(i, j)) return false;
            }
        }
        return true;
    }

    private static boolean checkRightUp(int y, int x) {
        for (int j = 0; j < FOX_DOTS_TO_WIN; j++) {

            if (y < 0 || y >= NUM || x < 0 || x >= NUM || currentDot != tiles[y][x]) return false;

            y--;
            x++;
        }

        return true;
    }

    private static boolean checkRight(int y, int x) {
        for (int j = 0; j < FOX_DOTS_TO_WIN; j++) {

            if (y < 0 || y >= NUM || x < 0 || x >= NUM || currentDot != tiles[y][x]) return false;

            x++;
        }

        return true;
    }

    private static boolean checkRightDown(int y, int x) {
        for (int j = 0; j < FOX_DOTS_TO_WIN; j++) {

            if (y < 0 || y >= NUM || x < 0 || x >= NUM || currentDot != tiles[y][x]) return false;

            y++;
            x++;
        }
        return true;
    }

    private static boolean checkDown(int y, int x) {
        for (int j = 0; j < FOX_DOTS_TO_WIN; j++) {

            if (y < 0 || y >= NUM || x < 0 || x >= NUM || currentDot != tiles[y][x]) return false;

            y++;
        }
        return true;
    }

    private static boolean checkGameLoop() {
        if (actionCounter == FOX_ACTION_SUM) return false;
        if (!mainLoop()) return false;
        return true;
    }

}
