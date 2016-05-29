package com.abin.lee.pagani.common.collection;

import com.abin.lee.pagani.common.json.JsonUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tinkpad
 * Date: 16-5-29
 * Time: 下午3:44
 * To change this template use File | Settings | File Templates.
 */
public class ListTools {
    public static void main(String[] args) {
        List<Integer> lists = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            lists.add(i);

        }
        System.out.println("lists=" + JsonUtil.toJson(lists));
        Collection<Integer> lists1 = Collections2.filter(lists, new Predicate<Integer>() {
            @Override
            public boolean apply(Integer input) {
                return input >= 5;
            }
        });
        List<Integer> response = Lists.newArrayList(lists1);
        System.out.println("response=" + JsonUtil.toJson(response));

    }
}
