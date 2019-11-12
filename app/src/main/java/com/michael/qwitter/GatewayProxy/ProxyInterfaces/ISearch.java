package com.michael.qwitter.GatewayProxy.ProxyInterfaces;

import com.michael.qwitter.Model.Status;

import java.util.List;

public interface ISearch extends IProxy
{
    List<Status> getResult();
}
