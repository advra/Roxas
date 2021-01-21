package com.github.advra.roxas.database.models;

import org.bson.Document;

public class PlayerDataModel extends DataModel {
    public enum TypeGender{
        male,
        female
    }
    private String userId;
    private TypeGender gender;

    public PlayerDataModel(String userId, TypeGender gender){
        this.userId = userId;
        this.gender = gender;
    }

    public String getUserId() { return this.userId;}
    public void setGender(TypeGender gender){this.gender = gender;}
    public TypeGender getGender() {return this.gender;}
    public String getGenderString() {return this.gender.toString();}

    public Document toDocument(){
        return new Document()
            .append("userid", this.userId)
            .append("gender", this.gender);
    }
}
