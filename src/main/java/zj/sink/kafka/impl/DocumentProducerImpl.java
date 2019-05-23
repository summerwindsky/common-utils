package zj.sink.kafka.impl;

import com.thunisoft.data.domain.Document;
import com.thunisoft.sync.sink.kafka.DocumentProducer;
import com.thunisoft.sync.sink.kafka.domain.DocumentEncoder;
import lombok.Data;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "spring.kafka.producer")
@PropertySource("classpath:application.yml")
@Data
public class DocumentProducerImpl implements DocumentProducer {

    private static Properties props;

    private static KafkaProducer<String, Document> producer;

    private String topic;

    private String bootstrapServers;

    @PostConstruct
    public void init() {
        if (props == null) {
            props = new Properties();

            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DocumentEncoder.class.getName());
            //自定义序列化
            props.put("serializer.class", DocumentEncoder.class.getName());
            //自定义分区
            //            props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DocumentPartitioner.class.getName());
        }
        producer = new KafkaProducer<String, Document>(props);
    }

    public void produce(Document document){
        producer.send(new ProducerRecord<String, Document>(topic, document));
    }
}
