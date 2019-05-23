package zj.sink.kafka.domain;

import com.thunisoft.data.domain.Document;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DocumentEncoder implements Serializer<Document> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Document document) {
        return document.toBytes();
    }

    @Override
    public void close() {

    }
}
