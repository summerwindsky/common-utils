package zj.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:
 * Description:
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author zhangjing
 * @version 1.0
 * @date 2019-07-19 13:12
 */
public class MyCollectionUtils {

//    public static List<List<Integer>> subsets(int[] nums) {
//        List<List<Integer>> specialRuleAllCombination = new ArrayList<>();
//        specialRuleAllCombination.add(new ArrayList<Integer>());
//        long max1 = 1 << nums.length;
//        for (int i = 1; i < max1; i++) {
//            List<Integer> combination = new ArrayList<>();
//            for (int j = 0; j < nums.length; j++) {
//                if ((i & (1 << j)) != 0) {
//                    combination.add(nums[j]);
//                }
//            }
//            specialRuleAllCombination.add(combination);
//        }
//        return specialRuleAllCombination;
//    }

    /**
     * 获取集合的 所有子集
     * @param nums
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> subsets(List<T> nums) {
        List<List<T>> result = new ArrayList<>();
        long max1 = 1 << nums.size();
        for (int i = 1; i < max1; i++) {
            List<T> combination = new ArrayList<>();
            for (int j = 0; j < nums.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(nums.get(j));
                }
            }
            result.add(combination);
        }
        return result;
    }

    public static <T> List<List<T>> subsets(T[] nums) {
        List<List<T>> result = new ArrayList<>();
        long max1 = 1 << nums.length;
        for (int i = 1; i < max1; i++) {
            List<T> combination = new ArrayList<>();
            for (int j = 0; j < nums.length; j++) {
                if ((i & (1 << j)) != 0) {
                    combination.add(nums[j]);
                }
            }
            result.add(combination);
        }
        return result;
    }

    public static void main(String[] args) {

        String[] specialRule = new String[]{
//            "cutPrefixSuffixLazy",
//            "cutPrefixSuffix",
//            "formatLssws",
                "replaceSymbol1",
                "replaceSymbol2",
                "replaceShengShi",
                "fsSymbol"
        };

        long max1 = 1 << specialRule.length;
        for (int i = 1; i < max1; i++) {
            String combination = "";
            for (int j = 0; j < specialRule.length; j++) {
                int i1 = 1 << j;
                int i2 = i & i1;
                if (i2 != 0) {
//                    System.out.print(specialRule[j] + ", ");
                    combination = combination + specialRule[j] + ",";
                }
            }
            System.out.println(combination);
        }

        final int[] numSet = {1, 2, 3, 4};
        long max = 1 << numSet.length;
        for (int i = 1; i < max; i++) {
            for (int j = 0; j < numSet.length; j++) {
                if ((i & (1 << j)) != 0) {
                    System.out.print(numSet[j] + ", ");
                }
            }
            System.out.println();
        }

    }

}
