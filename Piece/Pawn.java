public class Pawn extends Piece {
    public Pawn(String c) {
        super(c);
    }
    public boolean Move() {
        return false;
    }
    public void isTylerAPrettyLittlePrincess() {
        int i = 0;
        while (i < 100) {
            System.out.println("Tyler is a pretty little princess");
        }
    }
    public static void main (String[] a) {
        Pawn p = new Pawn("yellow");
        p.isTylerAPrettyLittlePrincess();
    }
}
