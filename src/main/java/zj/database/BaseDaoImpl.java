package zj.database;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Title: BaseDaoImpl
 * Description: 数据库信息维护基础操作
 * Company: 北京华宇元典信息服务有限公司
 *
* @author tianxiupeng
 * @version 1.0
 * @date 2017年9月7日 下午3:13:27
 *
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDaoI<T> {

    private JdbcTemplate jdbcTemplate;

    /*
     * (non-Javadoc)
     * 
     * @see com.thunisoft.icase.system.BaseDaoI#saveForLong(java.lang.
     * String, java.util.List)
     */
    @Override
    public long saveForLong(final String sql, final List<Object> params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatementSetter preparedStatementSetter = new ArgumentPreparedStatementSetter(
                        (params.toArray()));
                if (preparedStatementSetter != null) {
                    preparedStatementSetter.setValues(preparedStatement);
                }
                return preparedStatement;
            }
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thunisoft.icase.system.BaseDaoI#save(java.lang.String,
     * java.util.List)
     */
    @Override
    public int save(String sql, List<Object> params) {
        if (params == null) {
            params = new LinkedList<Object>();
        }
        return jdbcTemplate.update(sql, params.toArray(new Object[params.size()]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.thunisoft.icase.system.BaseDaoI#batchSave(java.lang.String,
     * java.util.List)
     */
    @Override
    public int[] batchSave(String sql, List<List<Object>> params) {
        final List<Object[]> jdbcParams = new LinkedList<Object[]>();
        if (params != null) {
            for (List<Object> tempList : params) {
                jdbcParams.add(tempList.toArray(new Object[tempList.size()]));
            }
        }
        return jdbcTemplate.batchUpdate(sql, jdbcParams);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.thunisoft.icase.system.BaseDaoI#delete(java.lang.String,
     * java.util.List)
     */
    @Override
    public int delete(String sql, List<Object> params) {
        if (params == null) {
            params = new LinkedList<Object>();
        }
        return jdbcTemplate.update(sql, params.toArray(new Object[params.size()]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.thunisoft.icase.system.BaseDaoI#update(java.lang.String,
     * java.util.List)
     */
    @Override
    public int update(String sql, List<Object> params) {
        if (params == null) {
            params = new LinkedList<Object>();
        }
        return jdbcTemplate.update(sql, params.toArray(new Object[params.size()]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thunisoft.icase.system.BaseDaoI#find(java.lang.String,
     * java.util.List, java.lang.Class)
     */
    @Override
    public List<T> find(String sql, List<Object> params, Class<T> clazz) {
        if (params == null) {
            params = new LinkedList<Object>();
        }
        List<T> list = null;
        if (clazz == String.class) {
            list = jdbcTemplate.queryForList(sql, clazz, params.toArray(new Object[params.size()]));
        } else {
            list = jdbcTemplate.query(sql, params.toArray(new Object[params.size()]),
                BeanPropertyRowMapper.newInstance(clazz));
        }
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<T>();
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thunisoft.icase.system.BaseDaoI#count(java.lang.String,
     * java.util.List)
     */
    @Override
    public long count(String sql, List<Object> params) {
        if (params == null) {
            params = new LinkedList<Object>();
        }
        return (long) jdbcTemplate.queryForObject(sql, Integer.class, params.toArray(new Object[params.size()]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.thunisoft.icase.system.BaseDaoI#findOne(java.lang.String,
     * java.util.List, java.lang.Class)
     */
    @Override
    public T findOne(String sql, List<Object> params, Class<T> clazz) {
        if (params == null) {
            params = new LinkedList<Object>();
        }
        List<T> list = null;
        if (clazz == String.class) {
            list = jdbcTemplate.queryForList(sql, clazz, params.toArray(new Object[params.size()]));
        } else {
            list = jdbcTemplate.query(sql, params.toArray(new Object[params.size()]),
                BeanPropertyRowMapper.newInstance(clazz));
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.thunisoft.icase.system.BaseDaoI#sumForLong(java.lang.String,
     * java.util.List)
     */
    @Override
    public long sumForLong(String sql, List<Object> params) {
        return jdbcTemplate.queryForObject(sql, params.toArray(new Object[params.size()]), new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong(1);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thunisoft.icase.system.BaseDaoI#sumForDouble(java.lang.
     * String, java.util.List)
     */
    @Override
    public double sumForDouble(String sql, List<Object> params) {
        return jdbcTemplate.queryForObject(sql, params.toArray(new Object[params.size()]), new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getDouble(1);
            }
        });
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
