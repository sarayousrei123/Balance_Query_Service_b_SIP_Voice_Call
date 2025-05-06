package org.mycompany;

public class Balance {
    private String msisdn;
    private double balance;
 
    public Balance() {
    }
 
    public Balance(String msisdn, double balance) {
        this.msisdn = msisdn;
        this.balance = balance;
    }
 
    public String getMsisdn() {
        return msisdn;
    }
 
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
 
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
 
    @Override
    public String toString() {
        return "Balance{" +
                "msisdn='" + msisdn + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}