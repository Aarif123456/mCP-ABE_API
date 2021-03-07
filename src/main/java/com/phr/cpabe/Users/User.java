package com.phr.cpabe.Users;

// @Entity // ** Entity comes from a databse**
public class User {
//    @Id
    String id;

    public void setID(String id){
        this.id =id;
    }

    public String getID(){
        return id;
    }

}
