package zj.database;

import java.util.List;

/**
 * 
 * Title: BaseDaoI
 * Description: 数据库信息维护基础操作
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author tianxiupeng
 * @version 1.0
 * @date 2017年9月7日 下午3:13:27
 *
 * @param <T>
 */
public interface BaseDaoI<T> {

    /**
     * 保存数据
     * @param sql
     * @param params
     * @return
     */
    long saveForLong(final String sql, final List<Object> params);

    /**
     * 保存数据
     * @param sql
     * @param params
     * @return
     */
    int save(String sql, List<Object> params);

    /**
     * 批量保存数据
     * @param sql
     * @param params
     * @return
     */
    int[] batchSave(String sql, List<List<Object>> params);

    /**
     * 删除数据
     * @param sql
     * @param params
     * @return
     */
    int delete(String sql, List<Object> params);

    /**
     * 更新数据
     * @param sql
     * @param params
     * @return
     */
    int update(String sql, List<Object> params);

    /**
     * 查询单条数据
     * @param sql
     * @param params
     * @param clazz
     * @return
     */
    T findOne(String sql, List<Object> params, Class<T> clazz);

    /**
     * 查询数据集合
     * @param sql
     * @param params
     * @param clazz
     * @return
     */
    List<T> find(String sql, List<Object> params, Class<T> clazz);

    /**
     * 计算数据条数
     * @param sql
     * @param params
     * @return
     */
    long count(String sql, List<Object> params);

    /**
     * 计算long类型数据和
     * @param sql
     * @param params
     * @return
     */
    long sumForLong(String sql, List<Object> params);

    /**
     * 计算double类型数据和
     * @param sql
     * @param params
     * @return
     */
    double sumForDouble(String sql, List<Object> params);
}
