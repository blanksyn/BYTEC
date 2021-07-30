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
public class setup_supplier {
    int sn;
    String name, email, cn;
    
    public setup_supplier(int sn, String name, String email, String cn){
        this.sn = sn;
        this.name = name;
        this.email = email;
        this.cn = cn;
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
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setCN(String cn){
        this.cn = cn;
    }
    public String getCN(){
        return cn;
    }
    
}
