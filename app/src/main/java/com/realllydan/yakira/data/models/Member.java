package com.realllydan.yakira.data.models;

import com.google.firebase.database.Exclude;

public class Member {

    String docid, name, contact, type, lastCall, lastCallMadeBy;

    public Member() {
    }

    public Member(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Member(String name, String contact, String type) {
        this.name = name;
        this.contact = contact;
        this.type = type;
    }

    public Member(String name, String contact, String type, String lastCall, String lastCallMadeBy) {
        this.name = name;
        this.contact = contact;
        this.type = type;
        this.lastCall = lastCall;
        this.lastCallMadeBy = lastCallMadeBy;
    }

    public Member(String name, String type, String lastCall, String lastCallMadeBy) {
        this.name = name;
        this.type = type;
        this.lastCall = lastCall;
        this.lastCallMadeBy = lastCallMadeBy;
    }

    @com.google.firebase.firestore.Exclude
    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
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

    public String getLastCall() {
        return lastCall;
    }

    public void setLastCall(String lastCall) {
        this.lastCall = lastCall;
    }

    public String getLastCallMadeBy() {
        return lastCallMadeBy;
    }

    public void setLastCallMadeBy(String lastCallMadeBy) {
        this.lastCallMadeBy = lastCallMadeBy;
    }
}
