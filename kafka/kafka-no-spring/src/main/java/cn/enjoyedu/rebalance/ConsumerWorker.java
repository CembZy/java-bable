package cn.enjoyedu.rebalance;

import cn.enjoyedu.config.BusiConst;
import cn.enjoyedu.config.KafkaConst;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
public class ConsumerWorker  implements Runnable{

    private final KafkaConsumer<String,String> consumer;
    /*用来保存每个消费者当前读取分区的偏移量*/
    private final Map<TopicPartition, OffsetAndMetadata> currOffsets;
    private final boolean isStop;

    public ConsumerWorker(boolean isStop) {
        /*消息消费者配置*/
        Properties properties = KafkaConst.consumerConfig(
                RebalanceConsumer.GROUP_ID,
                StringDeserializer.class,
                StringDeserializer.class);
        /*取消自动提交*/
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG
                ,false);
        this.isStop = isStop;
        this.consumer
                = new KafkaConsumer<String, String>(properties);
        this.currOffsets
                = new HashMap<TopicPartition, OffsetAndMetadata>();
        consumer.subscribe(Collections.singletonList(BusiConst.REBALANCE_TOPIC),
                new HandlerRebalance(currOffsets,consumer));
    }

    public void run() {
        final String id = Thread.currentThread().getId()+"";
        int count = 0;
        TopicPartition topicPartition = null;
        long offset = 0;
        try {
            while(true){
                ConsumerRecords<String, String> records
                        = consumer.poll(500);
                //业务处理
                //开始事务
                for(ConsumerRecord<String, String> record:records){
                    System.out.println(id+"|"+String.format(
                            "处理主题：%s，分区：%d，偏移量：%d，" +
                                    "key：%s，value：%s",
                            record.topic(),record.partition(),
                            record.offset(),record.key(),record.value()));
                    topicPartition = new TopicPartition(record.topic(),
                            record.partition());
                    offset = record.offset()+1;
                    currOffsets.put(topicPartition,new OffsetAndMetadata(offset,
                            "no"));
                    count++;
                    //执行业务sql
                }
                if(currOffsets.size()>0){
                    for(TopicPartition topicPartitionkey:currOffsets.keySet()){
                        HandlerRebalance.partitionOffsetMap.put(topicPartitionkey,
                                currOffsets.get(topicPartitionkey).offset());
                    }
                    //提交事务,同时将业务和偏移量入库
                }
                if(isStop&&count>=5){
                    System.out.println(id+"-将关闭，当前偏移量为："+currOffsets);
                    consumer.commitSync();
                    break;
                }
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }
}
