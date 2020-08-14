package com.cusomer.demo.util;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

public class RamdomLoadBalance extends LoadBalance {
    public RamdomLoadBalance(List<String> service_list) {
        super(service_list);
    }

    @Override
    public String choseServiceHost() {
        String result = "";
        if (!CollectionUtils.isEmpty(SERVICE_LIST)) {
            int index = new Random().nextInt(SERVICE_LIST.size());
            result = SERVICE_LIST.get(index);
        }
        return result;
    }
}
