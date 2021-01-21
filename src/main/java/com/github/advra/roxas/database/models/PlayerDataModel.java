package com.github.advra.roxas.database.models;

import org.bson.Document;

public class PlayerDataModel extends DataModel {

    private Long userId;                // referenced as snowflake from discord api
    private String gender;

    public PlayerDataModel(Long userId) { this.userId = userId; }
    public PlayerDataModel(Long userId, String gender) {
        this.userId = userId;
        this.gender = gender;
    }

    public Long getUserId() { return this.userId; }
    public void setGender(String gender){ this.gender = gender.toLowerCase();}
    public String getGender() {return this.gender; }

    public Document toDocument(){
        return new Document()
            .append("userid", this.userId)
            .append("gender", this.gender);
    }
}
