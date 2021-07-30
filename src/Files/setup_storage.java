/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Files;

/**
 *
 * @author Oleman
 */
public class setup_storage {
    int sn;
    String name, name_conv, column, level, location;
    double length, width, height, vol, vol_avail;
    
    /*public setup_storage(int sn, String name, String name_conv, String column, 
            String level, double length, double width, double height, double vol, double vol_avail){
        this.sn = sn;
        this.name = name;
        this.name_conv = name_conv;
        this.column = column;
        this.level = level;
        this.length = length;
        this.width = width;
        this.height = height;
        this.vol = vol;
        this.vol_avail = vol_avail;
    }*/
    
    public setup_storage(int sn, String location, double length, double width, double height, double vol, double vol_avail){
        this.sn = sn;
        //this.name = name;
        //this.name_conv = name_conv;
        //this.column = column;
        //this.level = level;
        this.location = location;
        this.length = length;
        this.width = width;
        this.height = height;
        this.vol = vol;
        this.vol_avail = vol_avail;
    }
    
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setSN(int sn){
        this.sn = sn;
    }
    public int getSN(){
        return sn;
    }
    public void setNameC(String name_conv){
        this.name_conv = name_conv;
    }
    public String getNameC(){
        return name_conv;
    }
    public void setColumn(String column){
        this.column = column;
    }
    public String getColumn(){
        return column;
    }
    public void setLevel(String level){
        this.level = level;
    }
    public String getLevel(){
        return level;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getLocation(){
        return location;
    }
    public void setLength(double length){
        this.length = length;
    }
    public double getLength(){
        return length;
    }
    public void setWidth(double width){
        this.width = width;
    }
    public double getWidth(){
        return width;
    }
    public void setHeight(double height){
        this.height = height;
    }
    public double getHeight(){
        return height;
    }
    public void setVol(double vol){
        this.vol = vol;
    }
    public double getVol(){
        return vol;
    }
    public void setVolA(double vol_avail){
        this.vol_avail = vol_avail;
    }
    public double getVolA(){
        return vol_avail;
    }
    
}
