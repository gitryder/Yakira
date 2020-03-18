package com.realllydan.yakira.data.models;

import com.google.firebase.firestore.Exclude;

public class Member {

    private String docId;
    private String name, contact, type;

    public Member() {
    }

    public Member(String name, String contact, String type) {
        this.name = name;
        this.contact = contact;
        this.type = type;
    }

    @Exclude
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
