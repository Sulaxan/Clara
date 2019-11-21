package me.encast.clara.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Tuple<A, B> {

    private A a;
    private B b;
}
