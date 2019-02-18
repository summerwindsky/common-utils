package zj.xml;

/**
 * Title:
 * Description:
 *
 * @author zhangjing
 * @version 1.0
 * @date 2018-07-13 17:04
 */
public interface XPathConstant {

    /**
     * 取值
     */
    String VALUE_PATH = "/@value";

    /**
     * 起诉日期
     */
    String QSRQ = "/writ/QW/SSJL/QSRQ";

    /**
     * 案件类别
     */
    String AJLB = "/writ/QW/WS/AJLB";

    /**
     * 审判程序
     */
    String SPCX = "/writ/QW/WS/SPCX";

    /**
     * 代理人
     */
    String DLR = "/writ/QW/DSR/DLR";
    /**
     * 律师成员
     */
    String CUS_LSCY = "/writ/QW/DSR/CUS_LSCY_RY/CUS_LSCY";

    /**
     * 律师成员冗余
     */
    String CUS_LSCY_RY = "/writ/QW/DSR/CUS_LSCY_RY";

    /**
     * 律师姓名
     */
    String CUS_LSXM = "/writ/QW/DSR/CUS_LSCY_RY/CUS_LSCY/CUS_LSXM";

    /**
     * 律师单位
     */
    String CUS_LSDW = "/writ/QW/DSR/CUS_LSCY_RY/CUS_LSCY/CUS_LSDW";

    /**
     * 法院级别
     */
    String FYJB = "/writ/QW/WS/JBFY/FYJB";

    /**
     * 法官成员姓名
     */
    String CUS_FGCY_FGRYXM = "/writ/QW/WW/CUS_FGCY/FGRYXM";

    /**
     * 法条
     */
    String FT = "/writ/FT";

    /**
     * 承担人地位
     */
    String CDRDW = "/writ/QW/PJJG/SSFCD/SSFCDJL/SSFCDFZ/CDR/CDRDW";

    /**
     * 民事-合计金额
     */
    String HJJE = "/writ/QW/PJJG/PJJGNR/HJJE";

    /**
     * 民事-判决金额
     */
    String PJJE = "/writ/QW/PJJG/PJJGNR/PJJE/JE";

    /**
     * 行政-赔偿总金额
     */
    String PCZJE = "/writ/QW/PJJG/PCAJPJJGJL/PCAJPJJGFZ/PCZJE";

    /**
     * 诉讼参与人
     */
    String SSCYR = "/writ/QW/DSR/DLR/SSCYR";

    /**
     * 文书种类
     */
    String WSZL = "/writ/QW/WS/WSZL";

}
