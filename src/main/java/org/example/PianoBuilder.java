package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PianoBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PianoBuilder.class);
    private static final int[] FULL_OCTAVES = new int[]{-2, -1, 0, 1, 2, 3, 4};
    private static final Integer INIT_OCTAVE_NUM = -3;
    private static final Integer LAST_OCTAVE_NUM = 5;

    private static final Map<Integer, Octave> octaveNotes = new LinkedHashMap<>();

    public PianoBuilder() {
        //init piano notes
        buildPianoNotes();
    }

    private void buildPianoNotes() {
        //FIRST octave notes
        Octave initOctave = getInitialOctave();
        octaveNotes.put(INIT_OCTAVE_NUM, initOctave);

        //FULL octave notes
        for (int octNum : FULL_OCTAVES) {
            Octave octFull = getFullOctave(octNum);
            octaveNotes.put(octNum, octFull);
        }

        //LAST octave notes
        Octave lastOctave = getLastOctave();
        octaveNotes.put(LAST_OCTAVE_NUM, lastOctave);
    }


    public boolean checkAllNotes(List<Note> notes) {
        for (Note note : notes) {
            if (!isCorrectNote(note))
                return false;
        }

        return true;
    }

    private boolean isCorrectNote(Note note) {
        int octaveNum = note.getOctave();
        int noteNumber = note.getNoteNumber();
        Octave octaveObj = octaveNotes.get(octaveNum);
        if (octaveObj == null)
            throw new IllegalArgumentException("Given octave number is incorrect " + octaveNum);

        String noteValue = octaveObj.getNotes().get(noteNumber);
        if (noteValue == null)
            throw new IllegalArgumentException("Given octave, note pair is incorrect [" + octaveNum + "," + noteNumber + "]");

        return true;
    }

    public List<Note> transposeNotes(List<Note> notes, int semitones) {
        List<Note> transposedNotes = new ArrayList<>();

        final int MAX_NOTES_12 = 12;
        int octToMove = Math.abs(semitones) >= MAX_NOTES_12 ? semitones / MAX_NOTES_12 : 0;
        int noteToMove = octToMove != 0 ? semitones % MAX_NOTES_12 : semitones;
        for (Note note : notes) {
            final int curOctaveNum = note.getOctave();
            final int curNoteNumber = note.getNoteNumber();

            int newNoteNumber = curNoteNumber + semitones;
            int newOctave = curOctaveNum;

            //if new note exceeds 12
            if (newNoteNumber > MAX_NOTES_12) {
                newOctave = curOctaveNum + (octToMove != 0 ? octToMove : 1);
                newNoteNumber = (newNoteNumber % MAX_NOTES_12);
            }

            //if new note less than 1
            if (newNoteNumber < 1) {
                newOctave = curOctaveNum + (octToMove != 0 ? octToMove : -1);
                newNoteNumber = (noteToMove != 0 ? (curNoteNumber + noteToMove) : (MAX_NOTES_12 + newNoteNumber));
            }

            //for negative values
            if (newNoteNumber <= 0) {
                newOctave--;
                newNoteNumber = MAX_NOTES_12 + newNoteNumber;
            }

            Note newNote = new Note(newOctave, newNoteNumber);

            if (isCorrectNote(newNote)) {
                transposedNotes.add(newNote);
            } else {
                LOGGER.error("Orig octave and note: " + note.getOctave() + ", " + note.getNoteNumber());
                throw new IllegalArgumentException("Incorrect octave and note: " + newNote.getOctave() + ", " + newNote.getNoteNumber());
            }
        }

        return transposedNotes;

    }

    private Octave getFullOctave(int octaveNum) {
        Map<Integer, String> notes = new LinkedHashMap<>();
        for (int i = 1; i < 13; i++) {
            if (i == 1 || i == 3 || i == 5 || i == 6 || i == 8 || i == 10 || i == 12) {
                notes.put(i, i + "-WHITE");
            } else {
                notes.put(i, i + "-BLACK");
            }

        }
        return new Octave(octaveNum, notes);
    }

    private Octave getInitialOctave() {
        Map<Integer, String> notes = new LinkedHashMap<>();
        notes.put(10, 10 + "-WHITE");
        notes.put(11, 11 + "-BLACK");
        notes.put(12, 12 + "-WHITE");
        return new Octave(INIT_OCTAVE_NUM, notes);
    }

    private Octave getLastOctave() {
        Map<Integer, String> notes = new LinkedHashMap<>();
        notes.put(1, 1 + "-WHITE");
        return new Octave(LAST_OCTAVE_NUM, notes);
    }

}
