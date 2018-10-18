package com.sun;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Sun
 * @date : 2018/10/11 16:16
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ListToMap {

    private Long id;
    private String name;

    public static void main(String[] args) {

        List<ListToMap> emps = Lists.newArrayList(new ListToMap(1L, "A"));

        Map<Long, String> collect = emps.stream().collect(Collectors.toMap(ListToMap::getId, ListToMap::getName));
        System.out.println(collect.entrySet());

        Map<Long, ListToMap> collect2 = emps.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        System.out.println(collect2.entrySet());
    }
}
