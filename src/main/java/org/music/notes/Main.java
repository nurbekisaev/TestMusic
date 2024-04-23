package org.music.notes;

import org.music.notes.dto.Note;
import org.music.notes.handler.NotesIOHandler;
import org.music.notes.handler.PianoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        /*if (args.length != 3) {
            System.err.println("Usage: java Main <input_file> <semitones> <output_file>");
            System.exit(1);
        }
        String inputFile = args[0];
        int semitones = Integer.parseInt(args[1]);
        String outputFile = args[2];
        */

        String inputFile = "./test_input.json";
        int semitones = 15;
        String outputFile = "./test_output.json";

        NotesIOHandler notesIOHandler = new NotesIOHandler();

        try {
            // Read input file
            List<Note> piece = notesIOHandler.readInputFile(inputFile);

            PianoBuilder pb = new PianoBuilder();

            //check if all notes correct from file
            boolean allCorrect = pb.checkAllNotes(piece);

            if (allCorrect) {
                //make transposition
                List<Note> newNotes = pb.transposeNotes(piece, semitones);

                //check if all new notes correct
                boolean outputCorrect = pb.checkAllNotes(newNotes);
                if (outputCorrect) {
                    notesIOHandler.writeOutputFile(outputFile, newNotes);

                    LOGGER.info("Transposition was successful!");
                }
            } else {
                LOGGER.error("Transposition was not successful!");
            }

        } catch (IOException e) {
            LOGGER.error("Error reading/writing files: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        }
    }
}