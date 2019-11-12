package com.michael.qwitter.GatewayProxy.ProxyInterfaces;

import com.michael.qwitter.Model.User;

public interface IGetUserInfo extends IProxy
{
    User getUser();
}
