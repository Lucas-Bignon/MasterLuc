package lucas.simplon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


//--------------------------
// DOC A REVOIR
// hashmap | Illegalargument | algo avancé mstermind tuto | Arrays | Arrayslist
//---------------------------

// bien on voit la classmaster min qui resulte d un objet avec des variables propre
public class Mastermind {
    private int essais;
    private int length;
    private int min;
    private int max;
    private int result;

// un tuto internet sur les hashmap
    private HashMap<Integer, HashSet<Integer>> code;
// mon petit constructeur    
    public Mastermind() {
        length = 4;
        min = 1;
        max = 6;
        essais = 10;
        result = 0;
    }
    // Truc en ligne pour IllegalArgumentException
    // avec u quelque notion de promesse
    public Mastermind(int numbTest) {
        if (numbTest < 1) {
             System.out.println("Tu peus faire seulement un seul essais :D");
        }
        essais = numbTest;
    }
    // pareil test unitaire pour tester les argument illegal
    public Mastermind(int numbTest, int codeLength) {
        this(numbTest);
        if (codeLength < 1) {
             System.out.println("Met au moins quelque chose !");
        }
        length = codeLength;
    }
    
    // je génère le code secret :D
    private void generateCode() {
    	// on donne un min et un max
        int codeRangeSize = max - min;
        // on test que sa marche bien
        if (codeRangeSize < length) {
             System.out.println(("La valeur de ton code secret n'est pas bonne !!! tu dois avoir une valeur max plus grande que la min :/ et pas les même ^^");
        }
     // le code secret 
    code = new HashMap<>();
    for (int i = 0; i < length; i++) {
    	// [ThreadLocalRandom.current()] c'est juste pour créer un nombre aléatoire XDDD
    	// mais on dirait un truc de ouf super dur XD
        int number = ThreadLocalRandom.current().nextInt(min, max + 1);
            // on evite de duppliquer le code secret
            while (code.containsKey(number)) {
                number = ThreadLocalRandom.current().nextInt(min, max + 1);
            }
            
            // tuto hashset et petit add dans l object
        HashSet<Integer> indices = code.getOrDefault(number, new HashSet<>());
        indices.add(i);
        code.put(number, indices);
    }
}
    
    
    
    
    public void play() {
    	// je joue avec mon petit menu
        generateCode();
        System.out.println("Salut dans le marvin-lulu-mastermind le meilleure jeu de la planète !!!");
        System.out.printf("Je pense a un code de"+ length+" chiffres,avec des chiffres entre "+min+" and "+max+".\n");
        System.out.printf("Peut tu le faire en "+essais+" essais ?\n");

        
        // flaggy
        boolean winner = false;
        // scan clavier
        Scanner input = new Scanner(System.in);
        // condition de nexturn le guess es l input donc %d la var 
        // pointeur C %d ? je diverge algo C ou java j'ai envie de faire les deux en meme temp
        while (result < essais) {
            System.out.printf("Guess %d: ", result + 1);
            String guess = input.nextLine();

            // on check si le resultat est bon si c ok on sort de la capsule
            Result result = submitUser(guess);
            if (result.Winner()) {
                winner = true;
                break;
            }

            // mes getteur pour les result et oui POO go je dois doc
            if (!result.getScore().isEmpty()) {
                System.out.println(result.getScore());
            } else if (!result.getMessage().isEmpty()) {
                System.out.println(result.getMessage());
            }
        }
        if(winner == true)
        {
        	System.out.println("Bien joué");
        }else{
        	System.out.println("Perdue :(");
        }
    }
    // je mate se que user me dit
    private Result submitUser(String guess) {
        result++;
        // si il faut de la d genre pas la longeur requise du code je tape dessus
        if (guess.length() != length) {
        	
            String errorMessage = String.format("Faut que ton essais fasse %d nombre de long!", length);
            
            return new Result(errorMessage);
        }
// ------------------ verif en cour
        try {
            return getScore(guess);
        } catch (IllegalArgumentException e) {
            String errorMessage = String.format("Mauvais essais: %s", e.getMessage());
            return new Result(errorMessage);
        }

      
    }
//  -------- La on rigole plus terminer, on s marre plus go go go doc on vas faire pl1 de théorique
    
    
    
    
    
    // on es sur la grosse partie méchante
    private Result getScore(String guess) {
    	// on doit verifier si le code est correct
        int scorePluses = 0;
        int scoreMinuses = 0;
        boolean perfectGuess = true;

        // 1 - On use la doc hashmap puis on fait une boucle 
        // 2
        // 3
        // 4
        HashMap<Integer, Integer> matches = new HashMap<>();
        for (int i = 0; i < guess.length(); i++) {
            char guessValue = guess.charAt(i);
            // tuto
            int number = Character.getNumericValue(guessValue);
            if (isValidGuessNumber(number)) {
                // dans notre hashset on laisse tomber la valeur si on a choper la bonne tout simplement
                int numberMatchCount = matches.getOrDefault(number, 0);
                int actualNumberCount = code.getOrDefault(number, new HashSet<>()).size();
                if (actualNumberCount > 0 && numberMatchCount == actualNumberCount) {
                    perfectGuess = false;
                    // continue c dla doc
                    continue;
                }

                // update de score
                switch (checkGuessValue(number, i)) {
                    case '+':
                        scorePluses++;
                        matches.put(number, matches.getOrDefault(number, 0) + 1);
                        break;
                    case '-':
                        scoreMinuses++;
                        matches.put(number, matches.getOrDefault(number, 0) + 1);
                        // sinon c pas bon
                    default:
                        perfectGuess = false;
                }
            } else {
            	// j adore les pointeur j vais en mettre partout
                 System.out.println(String.format("Guess values must be numbers between %d and %d!", min, max));
            }
        }

        return new Result(getScoreString(scorePluses, scoreMinuses), perfectGuess);
    }
    // blaba test
    private boolean isValidGuessNumber(int number) {
        return number >= min && number <= max;
    }
    
    // simple uttilisation du hashset dans une fonction qui check
    private char checkGuessValue(int number, int index) {
        char score = '\0';

        if (code.containsKey(number)) {
            HashSet<Integer> indices = code.get(number);
            if (indices.contains(index)) {
                score = '+';
            } else {
                score = '-';
            }
        }

        return score;
    }
    // exercice joli getteur
    private String getScoreString(int pluses, int minuses) {
        char[] score = new char[pluses + minuses];
        Arrays.fill(score, 0, pluses, '+');
        Arrays.fill(score, pluses, score.length, '-');
        return new String(score);
    }
    // le resultat et tt ses guetteur
    private class Result {
        private String score;
        private boolean returnEssai;
        private String message;
        public Result() {
            score = "";
            returnEssai = false;
            message = "";
        }
        public Result(String message) {
            this();
            message = message;
        }
        public Result(String scoreValue, boolean perfectGuess) {
            this();
            score = scoreValue;
            returnEssai = perfectGuess;
        }
        public String getScore() {
            return score;
        }
        public boolean Winner() {
            return returnEssai;
        }
        public String getMessage() {
            return message;
        }
    }
}



