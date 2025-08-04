import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HangmanGame {
    private static final String[] WORDS = {"программирование", "приключение", "гитара", "космос", "путешествие", "телефон", "зеркало", "ключи", "помада"};
    private static final int MAX_ATTEMPTS = 7;
    private static final String SCORES_FILE = "hangman_scores.txt";

    private char[] guessedLetters;
    private String secretWord;
    private Set<Character> usedLetters;
    private int attempts;
    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private boolean hintUsed = false;

    //методы
    private void updateGuessedLetters(char letter){
        for (int i = 0; i <secretWord.length(); i++){
            if (secretWord.charAt(i) == letter) {
                guessedLetters[i] = letter;
            }
        }
    }

    private boolean isWordGuessed(){
        return String.valueOf(guessedLetters).equals(secretWord);
    }

    private void printGameState(){
        System.out.println("Слово: " + String.valueOf(guessedLetters));
        System.out.println("Осталось попыток: "+attempts);
        System.out.println("Использованные буквы: "+ usedLetters);
    }

    private void useHint(){
        for(int i = 0; i < secretWord.length(); i++){
            if (guessedLetters[i] == '_'){
                guessedLetters[i] = secretWord.charAt(i);
                usedLetters.add(secretWord.charAt(i));
                attempts = Math.max(1, attempts - 2);//за подсказку -2 попытки
                hintUsed = true;
                System.out.println("Вы воспользовались подсказкой. Буква: "+ secretWord.charAt(i));
                printGameState();
                return;
            }
        }
        System.out.println("Все буквы угаданы");
    }

    private void printHelp(){
        System.out.println("Доступные команды:");
        System.out.println("/? - помощь");
        System.out.println("/ - подсказка, открывает одну букву (уменьшает на 2 попытки)");
    }

    private void loadScores(){
        try {
            Scanner fileScanner = new Scanner(new File(SCORES_FILE));
            gamesPlayed = fileScanner.nextInt();
            gamesWon = fileScanner.nextInt();
            gamesLost = fileScanner.nextInt();

            fileScanner.close();
        } catch (IOException e) {
            //файл не найден
            gamesPlayed = 0;
            gamesWon = 0;
            gamesLost = 0;
        }
    }

    private void saveScores(){
        try (PrintWriter writer = new PrintWriter(SCORES_FILE)){
            writer.println("Количесво игр: "+gamesPlayed);
            writer.println("Выиграл: "+gamesWon);
            writer.println("Проиграл: "+gamesLost);
        } catch (IOException e) {
            System.err.println("Не удалось сохранить результаты:(");
        }
    }
}