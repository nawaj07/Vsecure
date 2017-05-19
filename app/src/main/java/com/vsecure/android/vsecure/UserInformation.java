package com.vsecure.android.vsecure;


public class UserInformation {
    public String fname,rel,phn;
    public int id;

    public UserInformation(){

    }

    public UserInformation( String fname, String rel, String phn){
        this.fname= fname;
        this.rel=rel;
        this.phn=phn;
    }

    public UserInformation(int id, String fname, String rel, String phn){
        this.fname= fname;
        this.rel=rel;
        this.phn=phn;
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getName(){
        return fname;
    }
    public void setName(String fname){
        this.fname = fname;
    }
    public String getRelation(){
        return rel;
        }
    public void setRelation(String rel){
        this.rel= rel;
    }
    public String getPhone(){
        return phn;
    }
    public void setPhone(String phn){
        this.phn= phn;
    }

}
