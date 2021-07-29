package Files;

import javafx.beans.property.ObjectProperty;

import java.net.PortUnreachableException;
import java.security.cert.PolicyNode;
import java.sql.Date;

public class POin {

    int sn,qty_ordered,qty_rcv,qty_remaining,qty;
    String PONum,supplier,orderBy,status;
    String upc,prod_name;
    String DONum, rcvBy,appBy;
    Date date_rcv,orderDate,eta,expDate;

    public POin(int sn, String PONum, String supplier, String orderBy, Date orderDate, String status) {
        this.sn = sn;
        this.PONum = PONum;
        this.supplier = supplier;
        this.orderBy = orderBy;
        this.orderDate = orderDate;
        this.status = status;
    }

    public POin( String upc,int sn,String prod_name,int qty_ordered,int qty_rcv,int qty_remaining){
        this.sn = sn;
        this.upc = upc;
        this.prod_name = prod_name;
        this.qty_ordered = qty_ordered;
        this.qty_rcv = qty_rcv;
        this.qty_remaining = qty_remaining;
    }

    public POin(int sn, String PONum, String DONum, String supplier, Date date_rcv, String rcvBy,String appBy) {
        this.sn = sn;
        this.PONum = PONum;
        this.supplier = supplier;
        this.DONum = DONum;
        this.rcvBy = rcvBy;
        this.date_rcv = date_rcv;
        this.appBy = appBy;
    }

    public POin(String upc,String prod_name,int sn,int qty_ordered,int qty_rcv,int qty_remaining){
        this.upc =upc;
        this.prod_name = prod_name;
        this.sn = sn;
        this.qty_ordered = qty_ordered;
        this.qty_rcv = qty_rcv;
        this.qty_remaining = qty_remaining;

    }

    public POin(int sn, String upc, String prod_name,int qty){
        this.sn =sn;
        this.upc =upc;
        this.prod_name=prod_name;
        this.qty=qty;
    }

    public POin(int sn, String PONum, String supplier, String orderBy, Date orderDate, String status, Date eta){
        this.sn=sn;
        this.PONum =PONum;
        this.supplier=supplier;
        this.orderBy=orderBy;
        this.orderDate=orderDate;
        this.status=status;
        this.eta=eta;
    }

    public POin(int sn,String upc,String prod_name,int qty_ordered,int qty_rcv,int qty_remaining,Date expDate){
        this.sn=sn;
        this.upc= upc;
        this.prod_name= prod_name;
        this.qty_ordered=qty_ordered;
        this.qty_rcv=qty_rcv;
        this.qty_remaining=qty_remaining;
        this.expDate = expDate;
    }

    public POin(int sn, String poNum, String doNum, String supplier, String rcvBy, Date date_rcv) {
        this.sn=sn;
        this.PONum= poNum;
        this.DONum = doNum;
        this.supplier =supplier;
        this.rcvBy=rcvBy;
        this.date_rcv=date_rcv;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getAppBy() {
        return appBy;
    }

    public void setAppBy(String appBy) {
        this.appBy = appBy;
    }

    public String getDONum() {
        return DONum;
    }

    public void setDONum(String DONum) {
        this.DONum = DONum;
    }

    public String getRcvBy() {
        return rcvBy;
    }

    public void setRcvBy(String rcvBy) {
        this.rcvBy = rcvBy;
    }

    public Date getDate_rcv() {
        return date_rcv;
    }

    public void setDate_rcv(Date date_rcv) {
        this.date_rcv = date_rcv;
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getQty_ordered() {
        return qty_ordered;
    }

    public void setQty_ordered(int qty_ordered) {
        this.qty_ordered = qty_ordered;
    }

    public int getQty_rcv() {
        return qty_rcv;
    }

    public void setQty_rcv(int qty_rcv) {
        this.qty_rcv = qty_rcv;
    }

    public int getQty_remaining() {
        return qty_remaining;
    }

    public void setQty_remaining(int qty_remaining) {
        this.qty_remaining = qty_remaining;
    }

    public void qty_rcv(Integer newValue) {
    }

    public void expDate(String newValue) {
    }
}
