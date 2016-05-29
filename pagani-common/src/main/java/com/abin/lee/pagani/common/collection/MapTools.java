package com.abin.lee.pagani.common.collection;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tinkpad
 * Date: 16-5-29
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
public class MapTools {
    public static void main(String[] args) {
        ImmutableMap.Builder<String ,String> rmb = ImmutableMap.builder();
        rmb.put("abin" , "100");
        rmb.put("lee", "200");
        System.out.println("rmb="+rmb.build());
        rmb.orderEntriesByValue(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);  //To change body of implemented methods use File | Settings | File Templates.
            }

        });
        System.out.println("rmb="+rmb.build());
        Map<String, BigDecimal> dollar = Maps.transformValues(rmb.build(), new Function<String, BigDecimal>() {
            BigDecimal middle = new BigDecimal(6);
            @Override
            public BigDecimal apply(String input) {
                return middle.multiply(new BigDecimal(input));
            }
        });
        System.out.println("dollar="+dollar);
    }
}
