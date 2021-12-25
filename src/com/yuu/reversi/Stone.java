package com.yuu.reversi;

public class Stone {
    private int[] place;

    Stone(int[] place){
        setPlace(place);
    }

    //getter
    public int[] getPlace(){
        return place;
    }
    public int getX(){
        return place[1];
    }
    public int getY(){
        return  place[0];
    }

    //setter
    public void setPlace(int[] place){
        this.place = place;
        this.place[0] -= 1;
        this.place[1] -= 1;
    }

}
