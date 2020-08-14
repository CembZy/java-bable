package com.company.thread;

import java.util.concurrent.Callable;


/**
 * 实现Callable
 */
public class ImplCallable implements Callable {

    @Override
    public Object call() throws Exception {
        System.out.println("Callable already up");
        return "finish";
    }
}
