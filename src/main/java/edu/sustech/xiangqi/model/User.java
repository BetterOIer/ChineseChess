package edu.sustech.xiangqi.model;

public class User {
    
    private int id;
    private String username;
    private String pswordHash;
    private boolean isRed;
    private String description;

    public User(int id, String username, String password, boolean isRed){
        this.id=id;
        this.username = username;
        this.pswordHash = calHash(password);
        this.isRed = isRed;
    }
    public User(int id, String username, String password, boolean isRed, String description){
        this.id=id;
        this.username = username;
        this.pswordHash = calHash(password);
        this.isRed = isRed;
        this.description = description;
    }

    private String calHash(String password){
        String hash="";
        return hash;
    }

    //ID
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id=id;
    }

    //名字
    public String getName(){
        return this.username;
    }
    public void setName(String name){
        this.username = name;
    }

    //密码哈希
    public String getPswordHash(){
        return this.pswordHash;
    }
    public void setPswordHash(String hash){
        this.pswordHash = hash;
    }

    //isred
    public boolean isRed(){
        return this.isRed;
    }
    public void setSide(boolean isRed){
        this.isRed=isRed;
    }

    //描述
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}