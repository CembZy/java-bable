package cn.enjoyedu;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：
 */
public class WordCountExample {

    public static void main(String[] args) throws Exception{

        Properties props = new Properties();
        /*每个stream应用都必须有唯一的id*/
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount");
        /*kafka的地址*/
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        /*消息的序列化机制*/
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG,
                Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,
                Serdes.String().getClass().getName());

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //props.put(StreamsConfig.POLL_MS_CONFIG, 5);
        //props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 20);

        /*流的建造器*/
        KStreamBuilder builder = new KStreamBuilder();

        /*流的输入源，kafka中主题wordcount-input*/
        KStream<String, String> source = builder.stream("wordcount-input");

        /*正则负责拆分文本为单词*/
        final Pattern pattern = Pattern.compile("\\W+");
        KStream counts  =
                //将一行文本(事件)拆分为一系列的单词(事件)
                source.flatMapValues(value-> Arrays.asList(pattern.split(value.toLowerCase())))
                //将分拆后的单词重新映射为KeyValue实体，key和value一样
                .map((key, value) -> new KeyValue<Object, Object>(value, value))
                .filter((key, value) -> (!value.equals("the")))//过滤单词the
                .groupByKey()//按照key进行groupby，得到不重复的单词的集合
                //计算每个集合里的事件数
                //.count(TimeWindows.of(5000L),"CountStore")
                .count("CountStore")
                .mapValues(value->Long.toString(value)).toStream();
        //通过to方法可以将结果写回Kafka的主题wordcount-output
        //counts.to("wordcount-output");
        counts.print();

        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.cleanUp();
        streams.start();
        //streams.close();

    }
}
