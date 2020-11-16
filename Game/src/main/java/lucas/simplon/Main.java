package lucas.simplon;
public class Main {
    public static void main(String[] args) {   
        // j pas enregitré.. donc j instancie la game
        Mastermind game;
        game = new Mastermind();
        // et je la lance via poo
        game.play();
    }
}
