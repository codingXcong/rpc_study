package cn.zgc.hessian.model;

import java.io.Serializable;

public class User implements Serializable{

    private static final long serialVersionUID = 1692800630322115854L;
    //name属性
    private String name;

    public User() {
        
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}