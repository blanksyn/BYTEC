package Files;

import java.sql.Date;

public class POout {
    int sn,qty,qtyMonth;
    String SONum,PONum,company,ppBy,status,upc,prod_name,sku,sku_scanned,reject,loc,DONum,year,month,createdBy;
    Date date_created,delivery_date,startDate,endDate;

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }

    public POout(int sn,String PONum, String SONum, String company, Date date_created, String ppBy, String status, String reject) {
        this.sn = sn;
        this.SONum = SONum;
        this.PONum = PONum;
        this.company = company;
        this.date_created = date_created;
        this.ppBy = ppBy;
        this.status = status;
        this.reject = reject;
    }

    public POout(int sn, String upc, String prod_name, String sku, String sku_scanned) {
        this.sn = sn;
        this.upc = upc;
        this.prod_name = prod_name;
        this.sku = sku;
        this.sku_scanned = sku_scanned;
        this.loc = sku_scanned;
    }

    public POout(String company, Date date_created) {
        this.company = company;
        this.date_created = date_created;

    }

    public POout(int sn, String PONum,String SONum,String DONum,String company,Date date_created,Date delivery_date){
        this.sn = sn;
        this.PONum =PONum;
        this.SONum =SONum;
        this.DONum = DONum;
        this.company = company;
        this.date_created=date_created;
        this.delivery_date = delivery_date;
    }

    public POout(int sn,String upc,String prod_name,int qty,String sku_scanned){
        this.sn=sn;
        this.upc=upc;
        this.prod_name=prod_name;
        this.qty=qty;
        this.sku_scanned=sku_scanned;
    }

    public POout(int sn,String SONum,String company,Date date_created){
        this.sn=sn;
        this.SONum=SONum;
        this.company=company;
        this.date_created=date_created;
    }
    
    public POout(int sn, String month, int qtyMonth, String year) {
        this.sn = sn;
        this.month = month;
        this.qtyMonth = qtyMonth;
        this.year = year;
    }
    
    public POout(int sn,String PONum, String SONum, String company, String createdBy, Date date_created, Date delivery_date ,String status) {
        this.sn = sn;
        this.SONum = SONum;
        this.PONum = PONum;
        this.company = company;
        this.createdBy = createdBy;
        this.date_created = date_created;
        this.delivery_date = delivery_date;
        this.status = status;
    }
    
    public POout(String DONum, int sn, String SONum, String company, String createdBy, Date date_created, Date delivery_date ,String status) {
        this.sn = sn;
        this.SONum = SONum;
        this.DONum = DONum;
        this.company = company;
        this.createdBy = createdBy;
        this.date_created = date_created;
        this.delivery_date = delivery_date;
        this.status = status;
    }
    
    public POout(int sn, String upc, String prod_name, String sku_scanned) {
        this.sn = sn;
        this.upc = upc;
        this.prod_name = prod_name;
        this.sku_scanned = sku_scanned;
    }

    public String getSONum() {
        return SONum;
    }

    public void setSONum(String SONum) {
        this.SONum = SONum;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDONum() {
        return DONum;
    }

    public void setDONum(String DONum) {
        this.DONum = DONum;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getLocation() {
        return loc;
    }

    public void setLocation(String loc) {
        this.loc = loc;
    }

    public POout(String upc) {
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getPONum() {
        return PONum;
    }

    public void setPONum(String PONum) {
        this.PONum = PONum;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getPpBy() {
        return ppBy;
    }

    public void setPpBy(String ppBy) {
        this.ppBy = ppBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getQtyMonth() {
        return qtyMonth;
    }

    public void setQtyMonth(int qtyMonth) {
        this.qtyMonth = qtyMonth;
    }
    
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
    
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}