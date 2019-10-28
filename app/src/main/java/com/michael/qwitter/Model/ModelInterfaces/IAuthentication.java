package com.michael.qwitter.Model.ModelInterfaces;

public interface IAuthentication
{
    boolean authenticated();
    void login(String username, String password);
    void confirm(String username, String code);
    void signup(String username, String password, String email);
}
