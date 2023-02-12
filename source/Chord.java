public class Chord {
    public Note note1;
    public Note note2;
    public Note note3;

    public int x;

    public void addNote(Note note) {
        if (note1 == null) {
            note1 = note;
        } else if (note2 == null) {
            note2 = note;
        } else if (note3 == null) {
            note3 = note;
        }
    }

    public void removeNote(Note note) {
        if (note1 == note) {
            note1 = null;
        } else if (note2 == note) {
            note2 = null;
        } else if (note3 == note) {
            note3 = null;
        }
    }

    public void setX(int xPos) {
        x = xPos;
    }
}
