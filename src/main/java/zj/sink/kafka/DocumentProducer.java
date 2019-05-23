package zj.sink.kafka;



import com.thunisoft.data.domain.Document;

import java.io.IOException;

/**
 * Created by lk on 2017/1/10.
 */
public interface DocumentProducer {
    void produce(Document document) throws IOException;
}
