package com.michael.qwitter.GatewayProxy.ProxyInterfaces;

import com.michael.qwitter.Model.User;

import java.util.List;

public interface IGetUserList extends IProxy
{
    List<User> getUserList();
}
