package zj.trans.money;

import org.apache.commons.lang.StringUtils;

public class TransUtil {

    public static void main(String[] args) {
        System.out.println(TransUtil.format("1000"));
        System.out.println(TransUtil.format("1000.00元"));
        System.out.println(TransUtil.format("1000元18角"));
        System.out.println(TransUtil.format("1000元18角65分"));
        System.out.println(TransUtil.format("1000元角65分"));
        System.out.println(TransUtil.format("1000元18角分"));
        System.out.println(TransUtil.format("18角"));
        System.out.println(TransUtil.format("18角65分"));
        System.out.println(TransUtil.format("65分"));
    }

    public static String format(String money) {
        if (StringUtils.isEmpty(money)) {
            return null;
        }
        money = money.trim();
        if (StringUtils.isEmpty(money)) {
            return null;
        }
//        logger.info("金额:{}", money);
        Double total = null;
        String formated = money;
        String dw = money.substring(money.length() - 1);
        money = money.substring(0, money.length() - 1);

        String yuan = null;
        String jiao = null;
        String fen = null;
        if ("元".equals(dw)) {
            total = Double.valueOf(money);
        } else if ("角".equals(dw)) {
            if (money.contains("元")) {
                String[] yuanSplit = money.split("元", 2);
                yuan = yuanSplit[0];
                jiao = yuanSplit[1];
            } else {
                jiao = money;
            }
            if (StringUtils.isNotEmpty(yuan) && StringUtils.isNotEmpty(jiao)) {
                total = Double.valueOf(yuan) + Double.valueOf(jiao) / 10;
            } else if (StringUtils.isNotEmpty(yuan)) {
                total = Double.valueOf(yuan);
            } else {
                total = Double.valueOf(jiao) / 10;
            }

        } else if ("分".equals(dw)) {
            fen = money;
            if (money.contains("元")) {
                String[] yuanSplit = money.split("元", 2);
                yuan = yuanSplit[0];
                money = yuanSplit[1];
            }
            if (StringUtils.isNotEmpty(money) && money.contains("角")) {
                String[] jiaoSplit = money.split("角", 2);
                jiao = jiaoSplit[0];
                fen = jiaoSplit[1];
            }

            if (StringUtils.isNotEmpty(yuan) && StringUtils.isNotEmpty(jiao) && StringUtils.isNotEmpty(fen)) {
                total = Double.valueOf(yuan) + Double.valueOf(jiao) / 10 + Double.valueOf(fen) / 100;
            } else if (StringUtils.isNotEmpty(yuan) && StringUtils.isNotEmpty(jiao)) {
                total = Double.valueOf(yuan) + Double.valueOf(jiao) / 10;
            } else if (StringUtils.isNotEmpty(yuan) && StringUtils.isNotEmpty(fen)) {
                total = Double.valueOf(yuan) +Double.valueOf(fen) / 100;
            } else if (StringUtils.isNotEmpty(jiao) && StringUtils.isNotEmpty(fen)) {
                total = Double.valueOf(jiao) / 10 + Double.valueOf(fen) / 100;
            } else if (StringUtils.isNotEmpty(jiao)) {
                total = Double.valueOf(jiao) / 10;
            } else if (StringUtils.isNotEmpty(fen)) {
                total = Double.valueOf(fen) / 100;
            } else if (StringUtils.isNotEmpty(yuan)) {
                total = Double.valueOf(yuan);
            }
        }

        if (total != null) {
            formated = String.format("%.2f", total);
        }

        return formated.trim();
    }

}
