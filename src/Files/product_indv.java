package Files;

public class product_indv {

    int sn;
    String upc,sku,loc,prod_name,sku_scanned;

    public product_indv(int sn, String upc, String prod_name, String sku, String loc) {
        this.sn = sn;
        this.upc = upc;
        this.sku = sku;
        this.loc = loc;
        this.prod_name = prod_name;
    }

    public product_indv(int sn, String upc, String prod_name, String sku, String sku_scanned, String loc) {
        this.sn = sn;
        this.upc = upc;
        this.sku = sku;
        this.loc = loc;
        this.prod_name = prod_name;
        this.sku_scanned = sku_scanned;
    }

    public String getSku_scanned() {
        return sku_scanned;
    }

    public void setSku_scanned(String sku_scanned) {
        this.sku_scanned = sku_scanned;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }
}
