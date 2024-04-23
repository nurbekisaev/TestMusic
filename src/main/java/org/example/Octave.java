package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public class Octave {
    private Integer octaveNum;
    private Map<Integer, String> notes;
}
