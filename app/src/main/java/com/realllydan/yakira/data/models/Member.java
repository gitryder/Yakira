package com.realllydan.yakira.data.models;

import com.google.firebase.database.Exclude;

public class Member {

    private String memberId;
    private String name, contact, type;
    private CallRecord lastCall;

    public Member() {

    }

    public Member(String name, String contact, String type) {
        this.name = name;
        this.contact = contact;
        this.type = type;
    }

    @Exclude
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public CallRecord getLastCall() {
        return lastCall;
    }

    public void setLastCall(CallRecord lastCall) {
        this.lastCall = lastCall;
    }
}
