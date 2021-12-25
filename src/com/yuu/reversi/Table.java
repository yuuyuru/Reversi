package com.yuu.reversi;

import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class Table {
    private int[][] table;
    private Player firstPlayer;
    private Player secondPlayer;

    Table(){
        table = new int[][]{{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,2,1,0,0,0},
                {0,0,0,1,2,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
        firstPlayer = new Player();
        secondPlayer = new Player();
        firstPlayer.setIsFirst(true);
        secondPlayer.setIsFirst(false);
    }

    public void game(){
        Scanner scan = new Scanner(System.in);
        setNames(scan);
        int passCount = 0;
        while(true){
            //firstPlayer
            showTable();
            Stone firstPlayerStone = setStone(firstPlayer, scan);
            if(firstPlayerStone.getPlace()[0] <= -1 || firstPlayerStone.getPlace()[1] <= -1){
                passCount++;
            }else{
                passCount = 0;
                firstPlayer.putStone(table, firstPlayerStone);
                changeTable(firstPlayer, firstPlayerStone, table);
            }

            if(passCount >= 2){
                gameSet(firstPlayer, secondPlayer);
                break;
            }

            //secondPlayer
            showTable();
            Stone secondPlayerStone = setStone(secondPlayer, scan);
            if(secondPlayerStone.getPlace()[0] <= -1 || firstPlayerStone.getPlace()[1] <= -1){
                System.out.println("a");
                passCount++;
            }else{
                System.out.println("b");
                System.out.println(secondPlayerStone.getPlace()[0]);
                passCount = 0;
                secondPlayer.putStone(table, secondPlayerStone);
                changeTable(secondPlayer, secondPlayerStone, table);
            }

            if(passCount >= 2){
                gameSet(firstPlayer, secondPlayer);
                break;
            }
        }
    }

    public void setNames(Scanner scan){
        System.out.println("初手のユーザーの名前を入力してください！");
        String firstPlayerName = scan.nextLine();
        firstPlayer.setName(firstPlayerName);

        System.out.println("後手のユーザーの名前を入力してください！");
        String secondPlayerName = scan.nextLine();
        secondPlayer.setName(secondPlayerName);
    }

    public Stone setStone(Player player, Scanner scan){
        Stone stone;
        while(true){
            ArrayList<ArrayList<Integer>> allowPos = checkPerfect(player);
//            for(int i = 0; i < allowPos.size(); i++){
//                for(int j = 0; j < allowPos.get(i).size(); j++){
//                    System.out.println(allowPos.get(i).get(j));
//                }
//            }
            if(allowPos.isEmpty()){
                System.out.println("置く場所がありません");
                Stone emptyStone = new Stone(new int[]{-1, -1});
                return emptyStone;
            }
            System.out.println(player.getName() + "の石のx座標を入力してください！");
            int PlayerX = scan.nextInt();
            System.out.println(player.getName() + "の石のy座標を入力してください！");
            int PlayerY = scan.nextInt();

//            for(int i = 0; i < allowPos.length; i++){
//                for(int j = 0; j < allowPos[i].length; j++){
//                    System.out.println(allowPos[i][j]);
//                }
//            }
//
//            boolean allow = false;
//            for(int i = 0; i < allowPos.length; i++){
//                if(allowPos[i][1] == PlayerX-1 && allowPos[i][0] == PlayerY-1){
//                    allow = true;
//                    break;
//                }else{
//                    allow = false;
//                }
//            }

            boolean allow = false;
            for(int i = 0; i < allowPos.size(); i++){
                if(allowPos.get(i).get(1) == PlayerX-1 && allowPos.get(i).get(0) == PlayerY-1){
                    allow = true;
                    break;
                }else{
                    allow = false;
                }
            }
            if (1 > PlayerX || PlayerX > 8 || 1 > PlayerY || PlayerY > 8) {
                System.out.println("座標は1から8より選んでください");
                continue;
            } else {
                stone = new Stone(new int[]{PlayerX, PlayerY});
                if (!checkSamePos(stone) || !allow) {
                    System.out.println("その位置には置くことができません");
                    continue;
                }
                break;
            }
        }
        return stone;
    }

    public void showTable(){
        String[][] newTable = new String[table.length][table[0].length];
        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table[i].length; j++){
                if(table[i][j] == 1){
                    newTable[i][j] = "〇";
                }else if(table[i][j] == 2){
                    newTable[i][j] = "●";
                }else{
                    newTable[i][j] = " ";
                }
            }
        }
        System.out.println("---------------------------------");
        for(int i = 0; i < newTable.length; i++){
            System.out.print("|");
            for(int j = 0; j < newTable[0].length; j++){
                System.out.print(" " + newTable[i][j] + " |");
            }
            System.out.println("");
            System.out.println("---------------------------------");
        }
    }

    public boolean checkSamePos(Stone stone){
        boolean check = true;
        if(table[stone.getX()][stone.getY()] == 1 || table[stone.getX()][stone.getY()] == 2){
            System.out.println("その場所はすでに設置されています");
            check = false;
        }
        return check;
    }

    public int[] checkAll(Player player, int[] array){
        ArrayList<Integer> posList = new ArrayList<Integer>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        int differentPlayerNum;
        int playerNum;
        if(player.getIsFirst()){
            differentPlayerNum = 2;
            playerNum = 1;
        }else{
            differentPlayerNum = 1;
            playerNum = 2;
        }
        for(int i = 0; i < array.length; i++){
            if(array[i] == playerNum){
                list.add(i);
            }
        }
        for(int i = 0; i < list.size(); i++) {
            if(checkAfter(differentPlayerNum, array, list.get(i)) != -1){
                posList.add(checkAfter(differentPlayerNum, array, list.get(i)));
            }

            if(checkBefore(differentPlayerNum, array, list.get(i)) != -1){
                posList.add(checkBefore(differentPlayerNum, array, list.get(i)));
            }
        }
        int[] ary = new int[posList.size()];
        for(int i = 0; i < posList.size(); i++){
            ary[i] = posList.get(i);
        }
        return ary;
    }

    public int checkAfter(int differentPlayerNum, int[] l, int i){
        int differentCount = 0;
        int whileCount = 1;
        boolean afterBool;
        while(true){
            if(i + whileCount <= l.length-1){
                if(l[i + whileCount] == differentPlayerNum){
                    differentCount++;
                }else if(l[i + whileCount] == 0 && differentCount > 0){
                    afterBool = true;
                    break;
                }else{
                    afterBool = false;
                    break;
                }
            }else{
                afterBool = false;
                break;
            }
            whileCount++;
        }
        if(afterBool){
            return i + whileCount;
        }else{
            return -1;
        }
    }

    public int checkBefore(int differentPlayerNum, int[] l, int i){
        int differentCount = 0;
        int whileCount = 1;
        boolean beforeBool = true;
        while (true){
            if(i - whileCount >= 0){
                if(l[i - whileCount] == differentPlayerNum){
                    differentCount++;
                }else if(l[i - whileCount] == 0 && differentCount > 0){
                    beforeBool = true;
                    break;
                }else{
                    beforeBool = false;
                    break;
                }
            }else{
                beforeBool = false;
                break;
            }
            whileCount++;
        }
        if(beforeBool){
            return i - whileCount;
        }else{
            return -1;
        }
    }

    public ArrayList<ArrayList<Integer>> checkPerfect(Player player){
        //all
        int[][] besideLine = new int[8][];
        int[][] verticalLine = new int[8][];
        int[][] diagonalLine1 = new int[11][];
        int[][] diagonalLine2 = new int[11][];

        //beside
        for(int i = 0; i < table.length; i++){
            besideLine[i] = table[i];
        }

        //vertical
        for(int i = 0; i < table.length; i++){
            ArrayList<Integer> verticalList = new ArrayList<Integer>();
            for(int j = 0; j < table[0].length; j++){
                verticalList.add(table[j][i]);
            }
            int[] verticalArray = new int[verticalList.size()];
            for(int j = 0; j < verticalList.size(); j++){
                verticalArray[j] = verticalList.get(j);
            }
            verticalLine[i] = verticalArray.clone();
        }

        //diagonal-one
        for(int i = 0; i < 11; i++) {
            ArrayList<Integer> diagonalList = new ArrayList<Integer>();
            if(i <= 5){
                for(int j = 0; j < table.length - i; j++){
                    diagonalList.add(table[i + j][j]);
                }
            }else{
                for(int j = 0; j < 13 - i; j++){
                    diagonalList.add(table[j][i - 5 + j]);
                }
            }
            int[] diagonalArray = new int[diagonalList.size()];
            for(int j = 0; j < diagonalList.size(); j++){
                diagonalArray[j] = diagonalList.get(j);
            }
            diagonalLine1[i] = diagonalArray;
        }

        //diagonal-two
        for(int i = 0; i < 11; i++) {
            ArrayList<Integer> diagonalList = new ArrayList<Integer>();
            if(i <= 5){
                for(int j = 0; j < table.length - i; j++){
                    diagonalList.add(table[i + j][7-j]);
                }
            }else{
                for(int j = 0; j < 13 - i; j++){
                    diagonalList.add(table[j][12-i-j]);
                }
            }
            int[] diagonalArray = new int[diagonalList.size()];
            for(int j = 0; j < diagonalList.size(); j++){
                diagonalArray[j] = diagonalList.get(j);
            }
            diagonalLine2[i] = diagonalArray;
        }

        ArrayList<ArrayList<Integer>> allList = new ArrayList<ArrayList<Integer>>();
        //beside
        for(int i = 0; i < besideLine.length; i++){
            int[] besideAry = checkAll(player, besideLine[i]);
            for(int j = 0; j < besideAry.length; j++){
                ArrayList<Integer> besideAppendList = new ArrayList<Integer>();
                besideAppendList.add(i);
                besideAppendList.add(besideAry[j]);
                allList.add(besideAppendList);
            }
        }
        //vertical
        for(int i = 0; i < verticalLine.length; i++){
            int[] verticalAry = checkAll(player, verticalLine[i]);
            for(int j = 0; j < verticalAry.length; j++){
                ArrayList<Integer> verticalAppendList = new ArrayList<Integer>();
                verticalAppendList.add(verticalAry[j]);
                verticalAppendList.add(i);
                allList.add(verticalAppendList);
            }
        }
        //diagonal-one
        for(int i = 0; i < diagonalLine1.length; i++){
            int[] diagonalAry = checkAll(player, diagonalLine1[i]);
            ArrayList<Integer> diagonalAryOmit = new ArrayList<Integer>();
            for(int num : diagonalAry){
                if(!diagonalAryOmit.contains(num)){
                    diagonalAryOmit.add(num);
                }
            }
            int[] newDiagonalAry = new int[diagonalAryOmit.size()];
            for(int j = 0; j < newDiagonalAry.length; j++){
                newDiagonalAry[j] = diagonalAryOmit.get(j);
            }
            for(int j = 0; j < newDiagonalAry.length; j++){
                ArrayList<Integer> diagonalAppendList = new ArrayList<Integer>();
                if(i <= 5){
                    diagonalAppendList.add(i + newDiagonalAry[j]);
                    diagonalAppendList.add(newDiagonalAry[j]);
                }else{
                    diagonalAppendList.add(newDiagonalAry[j]);
                    diagonalAppendList.add(i - 5 + newDiagonalAry[j]);
                }
                allList.add(diagonalAppendList);
            }
        }
        //diagonal-two
        for(int i = 0; i < diagonalLine2.length; i++){
            int[] diagonalAry = checkAll(player, diagonalLine2[i]);
            ArrayList<Integer> diagonalAryOmit = new ArrayList<Integer>();
            for(int num : diagonalAry){
                if(!diagonalAryOmit.contains(num)){
                    diagonalAryOmit.add(num);
                }
            }
            int[] newDiagonalAry = new int[diagonalAryOmit.size()];
            for(int j = 0; j < newDiagonalAry.length; j++){
                newDiagonalAry[j] = diagonalAryOmit.get(j);
            }
            for(int j = 0; j < newDiagonalAry.length; j++){
                ArrayList<Integer> diagonalAppendList = new ArrayList<Integer>();
                if(i <= 5){
                    diagonalAppendList.add(i + newDiagonalAry[j]);
                    diagonalAppendList.add(table.length - newDiagonalAry[j] - 1);
                }else{
                    diagonalAppendList.add(newDiagonalAry[j]);
                    diagonalAppendList.add(7 - (i - 5) - newDiagonalAry[j]);
                }
                allList.add(diagonalAppendList);
            }
        }

//        int[][] allLineArray = new int[allList.size()][2];
//        for(int i = 0; i < allList.size(); i++){
//            for(int j = 0; j < allList.get(i).size(); j++){
//                System.out.println(allList.get(i).get(j));
//                allLineArray[i][j] = allList.get(i).get(j);
//            }
//        }
        return allList;
    }

    public void changeTable(Player player, Stone stone, int[][] tb){
        int differentPlayerNum;
        int playerNum;
        if(player.getIsFirst()){
            differentPlayerNum = 2;
            playerNum = 1;
        }else{
            differentPlayerNum = 1;
            playerNum = 2;
        }
        //besideList
        ArrayList<Integer> besideList = new ArrayList<Integer>();
        for(int i = 0; i < tb[stone.getX()].length; i++){
            if(tb[stone.getX()][i] == playerNum){
                besideList.add(i);
            }
        }
        int besideIndex = besideList.indexOf(stone.getY());

        //verticalList
        ArrayList<Integer> verticalList = new ArrayList<Integer>();
        for(int i = 0; i < tb.length; i++){
            if(tb[i][stone.getY()] == playerNum){
                verticalList.add(i);
            }
        }
        int verticalIndex = verticalList.indexOf(stone.getX());

        //diagonalList1
        ArrayList<Integer> diagonalList1 = new ArrayList<Integer>();
        for(int i = 0; i < tb.length-Math.abs(stone.getX() - stone.getY()); i++){
            if(stone.getX() <= stone.getY()){
                if(tb[i][Math.abs(stone.getX() - stone.getY()) + i] == playerNum){
                    diagonalList1.add(i);
                }
            }else{
                if(tb[Math.abs(stone.getX() - stone.getY()) + i][i] == playerNum){
                    diagonalList1.add(i);
                }
            }
        }
        int diagonalIndex1;
        if(stone.getX() <= stone.getY()){
            diagonalIndex1 = diagonalList1.indexOf(stone.getX());
        }else{
            diagonalIndex1 = diagonalList1.indexOf(stone.getY());
        }

        //diagonalList2
        ArrayList<Integer> diagonalList2 = new ArrayList<Integer>();
        for(int i = 0; i < tb.length-Math.abs((stone.getX() - (7 - stone.getY()))); i++){
            if(stone.getX() <= (7 - stone.getY())){
                if(tb[i][tb.length - 1 - Math.abs((stone.getX() - (7 - stone.getY()))) - i] == playerNum){
                    diagonalList2.add(i);
                }
            }else{
                if(tb[Math.abs((stone.getX() - (7 - stone.getY()))) + i][tb.length - i - 1] == playerNum){
                    diagonalList2.add(i);
                }
            }
        }
        int diagonalIndex2 = 0;
        if(stone.getX() <= tb.length - 1 - stone.getY()){
            diagonalIndex2 = diagonalList2.indexOf(stone.getX());
        }else{
            diagonalIndex2 = diagonalList2.indexOf(stone.getX() - Math.abs((stone.getX() - (7 - stone.getY()))));
        }

        HitStone(besideList, stone, tb, playerNum, differentPlayerNum, besideIndex, 1);
        HitStone(verticalList, stone, tb, playerNum, differentPlayerNum, verticalIndex, 2);
        HitStone(diagonalList1, stone, tb, playerNum, differentPlayerNum, diagonalIndex1, 3);
        HitStone(diagonalList2, stone, tb, playerNum, differentPlayerNum, diagonalIndex2, 4);
    }

    public void HitStone(ArrayList<Integer> list, Stone stone, int[][] tb, int playerNum, int differentPlayerNum, int index, int type){
        int min;
        int max;
        boolean bool = true;
        if(index <= 0){
            min = -1;
        }else{
            min = list.get(index-1);
        }

        if(index == list.size()-1){
            max = -1;
        }else{
            max = list.get(index+1);
        }

        if(min == -1 && max == -1){
        }else if(min == -1 && max != -1){
            switch (type){
                case 1:
                    for(int i = 1; i < max - stone.getY(); i++){
                        if(tb[stone.getX()][stone.getY() + i] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < max - stone.getY(); i++){
                            tb[stone.getX()][stone.getY() + i] = playerNum;
                        }
                    }
                    break;
                case 2:
                    for(int i = 1; i < max - stone.getX(); i++){
                        if(tb[stone.getX() + i][stone.getY()] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < max - stone.getX(); i++){
                            tb[stone.getX() + i][stone.getY()] = playerNum;
                        }
                    }
                    break;
                case 3:
                    if(stone.getX() <= stone.getY()){
                        for(int i = 1; i < max - stone.getX(); i++){
                            if(tb[stone.getX() + i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - stone.getX(); i++){
                                tb[stone.getX() + i][stone.getY() + i] = playerNum;
                            }
                        }
                    }else{
                        for(int i = 1; i < max - stone.getY(); i++){
                            if(tb[stone.getX() + i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - stone.getY(); i++){
                                tb[stone.getX() + i][stone.getY() + i] = playerNum;
                            }
                        }
                    }
                    break;
                case 4:
                    if(stone.getX() <= tb.length-1-stone.getY()){
                        for(int i = 1; i < max - stone.getX(); i++){
                            if(tb[stone.getX() + i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - stone.getX(); i++){
                                tb[stone.getX() + i][stone.getY() - i] = playerNum;
                            }
                        }
                    }else{
                        for(int i = 1; i < max - (tb.length-1-stone.getY()); i++){
                            if(tb[stone.getX() + i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - (tb.length-1-stone.getY()); i++){
                                tb[stone.getX() + i][stone.getY() - i] = playerNum;
                            }
                        }
                    }
                    break;
            }
        }else if(max == -1 && min != -1){
            switch (type){
                case 1:
                    for(int i = 1; i < stone.getY() - min; i++){
                        if(tb[stone.getX()][stone.getY() - i] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < stone.getY() - min; i++){
                            tb[stone.getX()][stone.getY() - i] = playerNum;
                        }
                    }
                    break;
                case 2:
                    for(int i = 1; i < stone.getX() - min; i++){
                        if(tb[stone.getX() - i][stone.getY()] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < stone.getX() - min; i++){
                            tb[stone.getX() - i][stone.getY()] = playerNum;
                        }
                    }
                    break;
                case 3:
                    if(stone.getX() <= stone.getY()){
                        for(int i = 1; i < stone.getX() - min; i++){
                            if(tb[stone.getX() - i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < stone.getX() - min; i++){
                                tb[stone.getX() - i][stone.getY() - i] = playerNum;
                            }
                        }
                    }else{
                        for(int i = 1; i < stone.getY() - min; i++){
                            if(tb[stone.getX() - i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < stone.getY() - min; i++){
                                tb[stone.getX() - i][stone.getY() - i] = playerNum;
                            }
                        }
                    }
                    break;
                case 4:
                    if(stone.getX() <= tb.length-1-stone.getY()){
                        for(int i = 1; i < stone.getX() - min; i++){
                            if(tb[stone.getX() - i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < stone.getX() - min; i++){
                                tb[stone.getX() - i][stone.getY() + i] = playerNum;
                            }
                        }
                    }else{
                        for(int i = 1; i < (tb.length-1-stone.getY()) - min; i++){
                            if(tb[stone.getX() - i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < (tb.length-1-stone.getY()) - min; i++){
                                tb[stone.getX() - i][stone.getY() + i] = playerNum;
                            }
                        }
                    }
                    break;
            }
        }else if(min != -1 && max != -1){
            switch (type){
                case 1:
                    for(int i = 1; i < max - stone.getY(); i++){
                        if(tb[stone.getX()][stone.getY() + i] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < max - stone.getY(); i++){
                            tb[stone.getX()][stone.getY() + i] = playerNum;
                        }
                    }

                    for(int i = 1; i < stone.getY() - min; i++){
                        if(tb[stone.getX()][stone.getY() - i] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < stone.getY() - min; i++){
                            tb[stone.getX()][stone.getY() - i] = playerNum;
                        }
                    }
                    break;
                case 2:
                    for(int i = 1; i < max - stone.getX(); i++){
                        if(tb[stone.getX() + i][stone.getY()] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < max - stone.getX(); i++){
                            tb[stone.getX() + i][stone.getY()] = playerNum;
                        }
                    }

                    for(int i = 1; i < stone.getX() - min; i++){
                        if(tb[stone.getX() - i][stone.getY()] == differentPlayerNum){
                            bool = true;
                            continue;
                        }else{
                            bool = false;
                            break;
                        }
                    }
                    if(bool == true){
                        for(int i = 1; i < stone.getX() - min; i++){
                            tb[stone.getX() - i][stone.getY()] = playerNum;
                        }
                    }
                    break;
                case 3:
                    if(stone.getX() <= stone.getY()){
                        for(int i = 1; i < max - stone.getX(); i++){
                            if(tb[stone.getX() + i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - stone.getX(); i++){
                                tb[stone.getX() + i][stone.getY() + i] = playerNum;
                            }
                        }

                        for(int i = 1; i < stone.getX() - min; i++){
                            if(tb[stone.getX() - i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < stone.getX() - min; i++){
                                tb[stone.getX() - i][stone.getY() - i] = playerNum;
                            }
                        }

                    }else{
                        for(int i = 1; i < max - stone.getY(); i++){
                            if(tb[stone.getX() + i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - stone.getY(); i++){
                                tb[stone.getX() + i][stone.getY() + i] = playerNum;
                            }
                        }

                        for(int i = 1; i < stone.getY() - min; i++){
                            if(tb[stone.getX() - i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < stone.getY() - min; i++){
                                tb[stone.getX() - i][stone.getY() - i] = playerNum;
                            }
                        }
                    }
                    break;
                case 4:
                    if(stone.getX() <= tb.length-1-stone.getY()){
                        for(int i = 1; i < max - stone.getX(); i++){
                            if(tb[stone.getX() + i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - stone.getX(); i++){
                                tb[stone.getX() + i][stone.getY() - i] = playerNum;
                            }
                        }

                        for(int i = 1; i < stone.getX() - min; i++){
                            if(tb[stone.getX() - i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < stone.getX() - min; i++){
                                tb[stone.getX() - i][stone.getY() + i] = playerNum;
                            }
                        }
                    }else{
                        for(int i = 1; i < max - (tb.length-1-stone.getY()); i++){
                            if(tb[stone.getX() + i][stone.getY() - i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < max - (tb.length-1-stone.getY()); i++){
                                tb[stone.getX() + i][stone.getY() - i] = playerNum;
                            }
                        }

                        for(int i = 1; i < (tb.length-1-stone.getY()) - min; i++){
                            if(tb[stone.getX() - i][stone.getY() + i] == differentPlayerNum){
                                bool = true;
                                continue;
                            }else{
                                bool = false;
                                break;
                            }
                        }
                        if(bool == true){
                            for(int i = 1; i < (tb.length-1-stone.getY()) - min; i++){
                                tb[stone.getX() - i][stone.getY() + i] = playerNum;
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void gameSet(Player firstPlayer, Player secondPlayer){
        int firstPlayerCount = 0;
        int secondPlayerCount = 0;
        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table[i].length; j++){
                if(table[i][j] == 1){
                    firstPlayerCount++;
                }else if(table[i][j] == 2){
                    secondPlayerCount++;
                }
            }
        }
        if(firstPlayerCount < secondPlayerCount){
            System.out.println(secondPlayer.getName() + "の勝利");
        }else if(firstPlayerCount > secondPlayerCount){
            System.out.println(firstPlayer.getName() + "の勝利");
        }else if(firstPlayerCount == secondPlayerCount){
            System.out.println("同点です");
        }
    }
}
