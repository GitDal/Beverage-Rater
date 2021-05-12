package dk.au.mad21spring.appproject.group6.models;

public class CurrentUser {
    public boolean IsAdmin;
    public String Email;

    public String toString() {
        return "{ email: " + Email + ", admin: " + IsAdmin + " }";
    }
}
