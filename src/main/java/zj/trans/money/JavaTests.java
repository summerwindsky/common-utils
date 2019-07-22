package zj.trans.money;

import org.junit.Test;

/**
 *
 * @author dingpeng
 * @version 1.0
 * @date 2018/9/27 16:32
 *
 */
public class JavaTests {

    @Test
    public void testMoney(){
        System.out.println(MoneyUtils.format("一亿三千五百万零二百五十元"));
        System.out.println(MoneyUtils.format("一亿三千五百万零五十元"));
        System.out.println(MoneyUtils.format("一亿三千五百六十万零五十元"));
        System.out.println(MoneyUtils.format("三千五百六十万五千八百五十元"));
        System.out.println(MoneyUtils.format("三千五百六十万五千零五十元"));
        System.out.println(MoneyUtils.format("一千二百五十元"));
        System.out.println(MoneyUtils.format("一千零五十元"));
        System.out.println(MoneyUtils.format("一千五十元"));
        System.out.println(MoneyUtils.format("一千元"));
        System.out.println(MoneyUtils.format("二百五十元"));
        System.out.println(MoneyUtils.format("二百元"));
        System.out.println(MoneyUtils.format("五十元"));

        System.out.println("========");

        System.out.println(MoneyUtils.format("五分"));
        System.out.println(MoneyUtils.format("八角五分"));
        System.out.println(MoneyUtils.format("五角"));
        System.out.println("========");

        System.out.println(MoneyUtils.format("五十元五分"));
        System.out.println(MoneyUtils.format("五十元八角五分"));
        System.out.println(MoneyUtils.format("五十元五角"));
        System.out.println("========");

        System.out.println(MoneyUtils.format("五十五分"));
        System.out.println(MoneyUtils.format("二元五十五分"));
        System.out.println(MoneyUtils.format("二元五十五角"));
        System.out.println(MoneyUtils.format("五十八角五分"));
        System.out.println(MoneyUtils.format("五十五角"));

        System.out.println("========");
        System.out.println(MoneyUtils.format("一千五十元六角"));


        System.out.println(MoneyUtils.format("102元6角"));

        System.out.println(MoneyUtils.format("102元6角7分"));
        System.out.println(MoneyUtils.format("102元6角分"));

        System.out.println(MoneyUtils.format("102元7分"));

        System.out.println(MoneyUtils.format("十万一百"));
        System.out.println(MoneyUtils.format("1十万"));

        System.out.println(MoneyUtils.format("10十万二千"));
        System.out.println(MoneyUtils.format("1200.34"));
        System.out.println(MoneyUtils.format("1200.34.45"));
        System.out.println(MoneyUtils.format("四万零五十元"));
        System.out.println(MoneyUtils.format("四万零五十余元"));

    }
}
