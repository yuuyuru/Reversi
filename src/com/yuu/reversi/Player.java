package com.yuu.reversi;

public class Player {
    private String name;
    private boolean isFirst;

    Player(){
        setName("player");
    }
    Player(String name){
        setName(name);
    }

    //getter
    public String getName(){
        return name;
    }
    public boolean getIsFirst() {
        return isFirst;
    }

    //setter
    public void setName(String name){
        this.name = name;
    }
    public void setIsFirst(boolean isFirst){
        this.isFirst = isFirst;
    }

    public void putStone(int[][] tb, Stone stone){
        if(isFirst){
            tb[stone.getX()][stone.getY()] = 1;
        }else{
            tb[stone.getX()][stone.getY()] = 2;
        }
    }
}
