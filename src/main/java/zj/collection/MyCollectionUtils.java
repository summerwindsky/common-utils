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

    /**
     * 将一个大的列表拆分为多个子列表。
     *
     * @param list      待拆分列表
     * @param partition 拆分数目
     * @return 子列表集合。
     */
    public static <T> List<List<T>> partition(List<T> list, int partition) {
        List<List<T>> lists = new ArrayList<List<T>>();
        if (list == null || list.size() < 2 || partition < 2) {
            lists.add(list);
        } else {
            int oSize = list.size();
            int batch = oSize / partition;
            // 最小批处理量为1.
            batch = batch <= 0 ? 1 : batch;
            int index = 0;
            int endIndex = 0;
            for (int i = 0; i < partition - 1 && index < oSize; ++i) {
                endIndex = Math.min(list.size(), index + batch);
                lists.add(list.subList(index, endIndex));
                index += endIndex - index;
            }
            if (index < oSize) {
                lists.add(list.subList(index, oSize));
            }
        }
        return lists;
    }

    public static <T> List<List<T>> splitList(List<T> list, int commitSize) {
        List<List<T>> splistList = new ArrayList<List<T>>();
        if (list == null || list.isEmpty()) {
            return splistList;
        }
        int range = commitSize;
        if (list.size() < range) {
            splistList.add(list);
            return splistList;
        }
        int size = list.size();
        int num = size % range == 0 ? size / range : (size / range) + 1;
        for (int j = 0; j < num; j++) {
            int beg = range * j;
            int end = range * j + range;
            if ((j + 1) == num) {
                end = size;
            }
            splistList.add(list.subList(beg, end));
        }
        return splistList;
    }

    public static Object[] splitAry(int[] ary, int subSize) {
        int count = ary.length % subSize == 0 ? ary.length / subSize
                : ary.length / subSize + 1;

        List<List<Integer>> subAryList = new ArrayList<List<Integer>>();

        for (int i = 0; i < count; i++) {
            int index = i * subSize;

            List<Integer> list = new ArrayList<Integer>();
            int j = 0;
            while (j < subSize && index < ary.length) {
                list.add(ary[index++]);
                j++;
            }

            subAryList.add(list);
        }

        Object[] subAry = new Object[subAryList.size()];

        for (int i = 0; i < subAryList.size(); i++) {
            List<Integer> subList = subAryList.get(i);

            int[] subAryItem = new int[subList.size()];
            for (int j = 0; j < subList.size(); j++) {
                subAryItem[j] = subList.get(j).intValue();
            }

            subAry[i] = subAryItem;
        }

        return subAry;
    }

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
