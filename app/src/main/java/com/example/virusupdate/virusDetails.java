package com.example.virusupdate;

public class virusDetails {
    private String country;
    private String cases,new_cses,deaths,new_deaths,recov,new_recov;
    public virusDetails(String country,String c,String nc,String d,String nd,String r,String nr){
        this.country=country;
        this.cases=c;
        this.new_cses=nc;
        this.deaths=d;
        this.new_deaths=nd;
        this.recov=r;
        this.new_recov=nr;
    }
    public String getCountry(){
        return country;
    }
    public String get_cases(){
        return cases+"+"+new_cses;
    }
    public String get_deaths(){
        return deaths+"+"+new_deaths;
    }
    public String get_recov(){
        return recov+"+"+new_recov;
    }
}
