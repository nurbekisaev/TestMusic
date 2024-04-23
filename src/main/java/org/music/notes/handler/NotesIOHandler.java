package org.music.notes.handler;

import org.music.notes.dto.Note;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotesIOHandler {
    public List<Note> readInputFile(String inputFile) throws IOException {
        // method to read input file
        List<Note> notes = new ArrayList<>();

        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                // Read each line containing pairs of integers
                String line;
                while ((line = reader.readLine()) != null) {
                    // Split the line into individual pairs
                    String[] pairsAsString = line.trim().split(", ");

                    // Parse each pair into two integers
                    for (String pair : pairsAsString) {
                        String[] pairValues = pair.replace("]", "").replace("[", "").split(",");

                        int octNum = Integer.parseInt(pairValues[0].trim());
                        int noteNum = Integer.parseInt(pairValues[1].trim());

                        Note note = new Note(octNum, noteNum);
                        notes.add(note);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public void writeOutputFile(String outputFile, List<Note> data) throws IOException {
        //method to write Notes to output file
        List<String> list = data.stream().map(Note::toString).collect(Collectors.toList());
        String result = "[" + String.join(", ", list) + "]";

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(result);
        }
    }
}