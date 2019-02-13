package zj.neo4j;

import com.thunisoft.hyyd.system.config.Constant;
import com.thunisoft.hyyd.system.config.SysConfig;
import org.neo4j.driver.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Title: DealWritRelationHandler
 * Description:
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author tianxiupeng
 * @version 1.0
 * @date 2018/11/23
 */
public class Neo4jUtil {
	private static final Logger logger = LoggerFactory.getLogger(Neo4jUtil.class);
	public static Driver driver;

	public static Driver init() {
		try {
			Config.ConfigBuilder builder = Config.build();
			builder.withMaxConnectionPoolSize(3000).withMaxConnectionLifetime(30, TimeUnit.MINUTES).withConnectionTimeout(5, TimeUnit.MINUTES);
			driver = GraphDatabase.driver(SysConfig.getProp(Constant.NEO4JURL), AuthTokens.basic(SysConfig.getProp(Constant.NEO4JUSER), SysConfig.getProp(Constant.NEO4JPWD)), builder.toConfig());
//			driver = GraphDatabase.driver("bolt://172.16.124.8:7687", AuthTokens.basic("neo4j", "rhtp"), builder.toConfig());
		} catch (Exception e) {
			logger.error("init driver error");
		}
		return driver;
	}

	public static Driver getDriver() {
		if (null == driver) {
			synchronized (Driver.class) {
				if (null == driver) {
					driver = init();
				}
			}
		}
		return driver;
	}


	/**
	 * 执行cypher语句
	 *
	 * @param cypher
	 */
	public static void execute(String cypher) {
		execute(cypher, null);
	}

	/**
	 * 执行占位符语句
	 *
	 * @param cypher
	 * @param mapValue
	 */
	public static void execute(String cypher, Map<String, Object> mapValue) {
		Session session = null;
		try {
			session = getDriver().session();
			session.run(cypher, mapValue);
		} catch (Exception e) {
			logger.error("操作图数据库失败！语句为：" + cypher + "mapValue: " + mapValue, e);
		} finally {
			session.close();
		}
	}


	/**
	 * 执行cypher语句
	 *
	 * @param cypher
	 */
	public static StatementResult executeQuery(String cypher) {
		StatementResult statementResult = executeQuery(cypher, null);
		return statementResult;
	}

	/**
	 * 执行查询带占位符的语句
	 *
	 * @param cypher
	 * @param mapValue
	 * @return
	 */
	public static StatementResult executeQuery(String cypher, Map<String, Object> mapValue) {
		Session session = null;
		StatementResult statementResult = null;
		try {
			session = getDriver().session();

			statementResult = session.run(cypher, mapValue);
		} catch (Exception e) {
			logger.error("操作图数据库失败！语句为：" + cypher + "mapValue: " + mapValue, e);
		} finally {
			session.close();
		}
		return statementResult;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Neo4jUtil.execute("CREATE (le:Person1 {name:\"Euler\"})");
			if (i % 1000 == 0) {
				System.out.println(System.currentTimeMillis() - start);
				start = System.currentTimeMillis();
			}
		}
		System.out.println("----------" + (System.currentTimeMillis() - start1));
	}
}
