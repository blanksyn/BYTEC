
package Files;

import javafx.scene.control.Button;

public class setup_courier {
    int sn;
    String name;
    
    public setup_courier(int sn, String name){
        this.sn = sn;
        this.name = name;
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
}
