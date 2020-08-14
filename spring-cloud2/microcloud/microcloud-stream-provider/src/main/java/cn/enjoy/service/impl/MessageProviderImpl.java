package cn.enjoy.service.impl;


import cn.enjoy.channel.DefaultProcess;
import cn.enjoy.service.IMessageProvider;
import cn.enjoy.vo.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@EnableBinding(DefaultProcess.class)
public class MessageProviderImpl implements IMessageProvider{
    @Resource
    @Qualifier("enjoy_output")
    private MessageChannel output;  // 消息的发送管道

    @Override
    public void send(Product product) {
        output.send(MessageBuilder.withPayload(product).build());
    }
}