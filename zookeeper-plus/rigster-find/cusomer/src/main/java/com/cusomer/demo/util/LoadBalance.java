package com.cusomer.demo.util;

import java.util.List;

public abstract class LoadBalance {

    public final List<String> SERVICE_LIST;

    protected LoadBalance(List<String> service_list) {
        SERVICE_LIST = service_list;
    }

    public abstract String choseServiceHost();

}
