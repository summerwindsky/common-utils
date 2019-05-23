package zj.sink.kafka.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thunisoft.es.ElasticSearchConnect;
import com.thunisoft.sync.model.Aj;
import com.thunisoft.sync.model.CourtVO;
import com.thunisoft.sync.model.DsrVO;
import com.thunisoft.sync.model.Yastml;
import com.thunisoft.data.domain.Document;
import com.thunisoft.sync.sink.kafka.IDocumentProducerService;
import com.thunisoft.sync.util.JBFYUtil;
import com.thunisoft.sync.util.SyncConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

/**
 * Title:
 * Description:
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author zhangjing
 * @version 1.0
 * @date 2019-05-15 16:20
 */
@Component
public class DocumentProducerServiceImpl implements IDocumentProducerService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentProducerImpl.class);

    @Autowired
    DocumentProducerImpl documentProducer;

    @Autowired
    private SyncConfig syncConfig;

    @Autowired
    private JBFYUtil JBFYUtil;

    /**
     * json转换对象
     */
    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    @Override
    public void insert(Aj aj) {
    }

    @Override
    public void blukInsert(List<Aj> ajList) {
        try {
            if (null == ajList || ajList.isEmpty()) {
                return;
            }

            long start = System.currentTimeMillis();

            long count = 0;
            for (Aj aj : ajList) {
                count++;
                if (!syncConfig.isYastml()) { //不同步与案实体名录
                    aj.setYastml(null);
                }

                //
                Yastml yastml = new Yastml();
                yastml.setXh(111);
                yastml.setSfzhm("23232323224242342424");
                yastml.setMc("當事人");
                DsrVO dsrVO = new DsrVO(yastml);
                aj.setDsr(dsrVO);

                // fy
                CourtVO courtVO = JBFYUtil.courtMap.get(aj.getJbfymc());
                if (courtVO != null) {
                    aj.setFycj(courtVO.getFycj());
                    aj.setXzqh_p(courtVO.getXzqh_p());
                    aj.setXzqh_c(courtVO.getXzqh_c());
                }

                //
                aj.setWscontent(null);
                aj.setWritList(null);
                aj.setAjcbr("张三");
                String json = gson.toJson(aj);
                Document document = new Document();
                document.setRowkey(aj.getAjbs());
                document.setOld_rowkey(aj.getBh());

                document.setTitle(aj.getAjmc());// ajmc
                document.setAjlb(aj.getAjlb());
                document.setContent(json);
                document.setNw("0");
//                document.setAhs(new HashSet<String>(){{add(aj.getBh());}});
                document.setDocDate(aj.getZhgxsj());
                document.setLx("fyj");

                // currentAH
                String ah = aj.getAh();
                String xzqh_p = aj.getXzqh_p();
                String jbfymc = aj.getJbfymc();
                String ajlb = aj.getAjlb();

                String ahKey = null;
                if (StringUtils.isNotBlank(jbfymc)) {
                    ahKey = ah + "_" + xzqh_p + "_" + jbfymc;
                } else {
                    ahKey = ah + "_" + xzqh_p;
                }
                if (StringUtils.isNotBlank(ajlb)) {
                    ahKey += "_" + ajlb;
                }
                document.setCurrentAH(ahKey);
                // currentAH  end

                // 入结构化数据

                documentProducer.produce(document);
                //
                if (count %3== 0) {
                    continue;
                }
                //
                document.setLx("fy");
                document.setNw("1");

//                // 入文书
//                List<String> wscontentList = aj.getWscontent();
//                if (CollectionUtils.isNotEmpty(wscontentList)) {
//                    wscontentList.forEach(content -> {
//                        document.setContent(content);
//                        document.setNw("1");
//                        documentProducer.produce(document);
//                    });
//                }

                // zaoshuju===========
                String content = "在我部和意大利环境国土部合作框架下，为推动中意两国在气候变化和减排领域的实质性合作，我部于2006年3月31日与意大利环境部签署了清洁发展机制（CDM）合作谅解备忘录工作计划书，商定从2006年至2008年期间在华共同开发100个CDM项目。<br> 　　清洁发展机制，简称CDM，是《京都议定书》中履约机制之一。CDM允许附件一缔约方与非附件一缔约方联合开展二氧化碳等温室气体减排项目。CDM提供了一种灵活的履约机制，发达国家可以将减排项目产生的减排数额作为履行他们所承诺的限排或减排量；而对于发展中国家，通过CDM项目可以获得部分资金援助和先进技术。<br> 　　我国一直在认真履行气候变化框架公约，积极促进《京都议定书》的生效，并于2002年8月批准了《京都议定书》。环境保护一直以来就是我国的基本国策。借鉴国外的经验教训，引进和吸收国外先进的技术和资金，走新型工业化道路，实现经济、社会和环境的协调和可持续发展是我国政府长期坚持的战略目标。此次与意大利政府合作的CDM项目，在帮助意方以较低的成本实现其减排温室气体承诺的同时，可以通过先进和适用技术的引进促进中国实施可持续发展战略，是一种双赢的活动。<br> 　　本项目要求在2008至2012年期间所产生的年减排量（CERs）不少于1000万吨；所有的减排量将由意大利环境国土部通过世行意大利碳基金或意大利其他买家购买。<br> 　　为了落实以上合作，现请你厅（委、局）尽快推荐符合以下条件的CDM项目建议";
                String content1 = "苏州市姑苏区人民法院 民事判决书 （2013）姑苏民特字第0074号 申请人周国瑜，男，1965年7月30日生。 申请人周国瑜要求宣告周汉瑜为无民事行为能力人一案，本院依法进行了审理，现已审理终结。 周汉瑜，男，1964年7月16日生。申请人周国瑜与周汉瑜系兄弟关系。周汉瑜的父母均已死亡，无配偶及子女。申请人周国瑜陈述，周汉瑜于2011年7月突发脑溢血，经苏州大学附属第二医院治疗，现处于植物人状态。申请人周国瑜认为周汉瑜目前无民事行为能力，申请本院予以宣告并指定申请人为其监护人。 苏州市广济医院司法鉴定所出具司法鉴定意见书，认为周汉瑜目前诊断为脑出血后精神障碍（中度），无法自主行使民事权利及承担相应民事义务，故无民事行为能力。 以上事实，有申请人周国瑜提供的户籍资料、出院记录、住院病案、周伟瑜情况说明、户表、苏广司鉴所（2013）司鉴字第413号精神医学司法鉴定意见书等证据证实。 本院认为，根据周汉瑜的病情及鉴定意见，其目前已不能辨认自己行为，申请人的申请有事实依据，应认定周汉瑜为无民事行为能力人。周汉瑜父母已死亡，且无配偶及子女，申请人周国瑜系周汉瑜的兄弟，应担任周汉瑜的监护人。依照《中华人民共和国民法通则》第十七条、《中华人民共和国民事诉讼法》第一百八十七条、第一百八十九条之规定，判决如下： 一、宣告周汉瑜为无民事行为能力人； 二、指定周国瑜为周汉瑜的监护人。 本判决为终审判决。 审判员吴倩 二〇一四年一月八日 书记员朱国荣 \" value=\"苏州市姑苏区人民法院 民事判决书 （2013）姑苏民特字第0074号 申请人周国瑜，男，1965年7月30日生。 申请人周国瑜要求宣告周汉瑜为无民事行为能力人一案，本院依法进行了审理，现已审理终结。 周汉瑜，男，1964年7月16日生。申请人周国瑜与周汉瑜系兄弟关系。周汉瑜的父母均已死亡，无配偶及子女。申请人周国瑜陈述，周汉瑜于2011年7月突发脑溢血，经苏州大学附属第二医院治疗，现处于植物人状态。申请人周国瑜认为周汉瑜目前无民事行为能力，申请本院予以宣告并指定申请人为其监护人。 苏州市广济医院司法鉴定所出具司法鉴定意见书，认为周汉瑜目前诊断为脑出血后精神障碍（中度），无法自主行使民事权利及承担相应民事义务，故无民事行为能力。 以上事实，有申请人周国瑜提供的户籍资料、出院记录、住院病案、周伟瑜情况说明、户表、苏广司鉴所（2013）司鉴字第413号精神医学司法鉴定意见书等证据证实。 本院认为，根据周汉瑜的病情及鉴定意见，其目前已不能辨认自己行为，申请人的申请有事实依据，应认定周汉瑜为无民事行为能力人。周汉瑜父母已死亡，且无配偶及子女，申请人周国瑜系周汉瑜的兄弟，应担任周汉瑜的监护人。依照《中华人民共和国民法通则》第十七条、《中华人民共和国民事诉讼法》第一百八十七条、第一百八十九条之规定，判决如下： 一、宣告周汉瑜为无民事行为能力人； 二、指定周国瑜为周汉瑜的监护人。 本判决为终审判决。 审判员吴倩 二〇一四年一月八日 书记员朱国荣 ";
                document.setRowkey(null);
                document.setUrlId(aj.getAjbs());

                document.setOld_rowkey(aj.getBh());
                document.setContent(content);
                document.setRowkey(count+"");
                document.setNw("1");
                //
                if (count % 3 == 1) {
                    documentProducer.produce(document);
                    continue;
                }

                if (count % 3 == 2) {
                    document.setContent(content1);
                    documentProducer.produce(document);
                }
            }
            logger.info(String.format("存储 %s 条记录到kafka耗时 %s ms", ajList.size(), (System.currentTimeMillis() - start)));
        } catch (Exception e) {
            logger.error("aj 入kafka异常!", e);
        }
    }
}
