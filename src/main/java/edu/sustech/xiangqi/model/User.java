package edu.sustech.xiangqi.model;

public class User {
    
    private int id;
    private int type;
    /* 1 normal account 2 unloginable account 4 in use or not*/
    private String username;
    private String pswordHash;
    private String description;

    public User(int id, String username, String password, int type){
        this.id=id;
        this.username = username;
        this.type=type;
        this.pswordHash = DBOperationUser.calHash(password);
    }
    public User(int id, String username, String password, int type, String description){
        this.id=id;
        this.username = username;
        this.type=type;
        this.pswordHash = DBOperationUser.calHash(password);
        this.description = description;
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

    //type
    public int getType(){
        return this.type;
    }
    public void setType(int type){
        this.type=type;
    }

    //描述
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return this.username;
    }
}