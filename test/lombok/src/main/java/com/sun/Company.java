package com.sun;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
@Builder
public class Company {

    private Long id;
    private String name;


    public static void main(String[] args) {
        // of构造
        Company.of(10L,"xx");
        // 链式
        Company.of().setId(10L).setName("xx");
        // builder
        Company.builder().id(10L).name("xx").build();
    }
}
