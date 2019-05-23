package zj.sink.kafka;

import com.thunisoft.sync.model.Aj;

import java.util.List;

/**
 * Title:
 * Description:
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author zhangjing
 * @version 1.0
 * @date 2019-05-15 16:21
 */
public interface IDocumentProducerService {
    public void insert(Aj aj);
    public void blukInsert(List<Aj> ajList);
}
