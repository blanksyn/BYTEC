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
public class prod_master{
    int sn, qty, min_qty, max_qty;
    String upc, prod_name, unit, location, weight, desc, supplier, category, special_handling, auto_status, date_added;
    Double length, height, width, volume;
    
    public prod_master(int sn, String upc, String prod_name, int qty, String unit, 
                        String location, String supplier, String category, int min_qty, int max_qty, 
                        String auto_status, String date_added, Double volume ){
        this.sn = sn;
        this.upc = upc;
        this.prod_name = prod_name;
        this.qty = qty;
        this.unit = unit;
        this.location = location;
        this.supplier = supplier; 
        this.category = category;
        this.min_qty = min_qty; 
        this.max_qty = max_qty; 
        this.auto_status = auto_status; 
        this.date_added = date_added;
        this.volume = volume;
    }
    
    public prod_master(int sn, String location){
        this.sn = sn;
        this.location = location;
    }
    
    public void setSN(int sn){
        this.sn = sn;
    }
    public int getSN(){
        return sn;
    }
    public void setUPC(String upc){
        this.upc = upc;
    }
    public String getUPC(){
        return upc;
    }
    public void setProdName(String prod_name){
        this.prod_name = prod_name;
    }
    public String getProdName(){
        return prod_name;
    }
    public void setQty(int qty){
        this.qty = qty;
    }
    public int getQty(){
        return qty;
    }
    public void setUnit(String unit){
        this.unit = unit;
    }
    public String getUnit(){
        return unit;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getLocation(){
        return location;
    }
    public void setSupplier(String supplier){
        this.supplier = supplier;
    }
    public String getSupplier(){
        return supplier;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getCategory(){
        return category;
    }
    public void setMin_qty(int min_qty){
        this.min_qty = min_qty;
    }
    public int getMin_qty(){
        return min_qty;
    }
    public void setMax_qty(int max_qty){
        this.max_qty = max_qty;
    }
    public int getMax_qty(){
        return max_qty;
    }
    public void setAuto_status(String auto_status){
        this.auto_status = auto_status;
    }
    public String getAuto_status(){
        return auto_status;
    }
    public void setDate_added(String date_added){
        this.date_added = date_added;
    }
    public String getDate_added(){
        return date_added;
    }
    public void setVolume(Double volume){
        this.volume = volume;
    }
    public Double getVolume(){
        return volume;
    }
}
