package Files;

public class product_indv {

    int sn;
    String upc,sku,loc,prod_name,sku_scanned,date_added,expiry_date, delivery_date;

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

    public product_indv(String date_added, int sn, String sku, String loc, String expiry_date) {
        this.sn = sn;
        this.sku = sku;
        this.loc = loc;
        this.date_added = date_added;
        this.expiry_date = expiry_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
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

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }
}
