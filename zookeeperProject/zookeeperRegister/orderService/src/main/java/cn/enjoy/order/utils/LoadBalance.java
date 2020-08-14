package cn.enjoy.order.utils;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by VULCAN on 2018/7/25.
 */
public abstract class LoadBalance {

    public volatile static   List<String> SERVICE_LIST;

    public abstract String choseServiceHost();



}
