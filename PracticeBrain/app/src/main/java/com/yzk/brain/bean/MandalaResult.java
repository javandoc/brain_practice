package com.yzk.brain.bean;

import java.io.Serializable;

/**
 * Created by GG on 2016/12/12.
 */
public class MandalaResult implements Serializable {
    public int whichDay;
    public Mandala data;


    public class Mandala implements Serializable{
        public int errorNumber;
        public int score;
    }

}
