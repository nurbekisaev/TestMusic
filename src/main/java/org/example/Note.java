package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Note {
    // Define a class for representing a musical note
    private int octave;
    private int noteNumber;

    @Override
    public String toString() {
        return "[" + octave + "," + noteNumber + "]";
    }
}
