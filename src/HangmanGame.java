import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HangmanGame {
    public static void main(String[] args) {
        HangmanGame game = new HangmanGame();
        game.play();
    }

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

    public HangmanGame(){
        loadScore();
    }

    public void startNewGame() {
        Random random = new Random();
        secretWord = WORDS[random.nextInt(WORDS.length)];
        guessedLetters = new char[secretWord.length()];
        Arrays.fill(guessedLetters, '_');
        usedLetters = new HashSet<>();
        attempts = MAX_ATTEMPTS;
        hintUsed = false;

        System.out.println("Новая игра!");
        System.out.println("Слово содержит " + secretWord.length() + " букв");

        printGameState();
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Начать игру");
            System.out.println("2. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                startNewGame();
                gameLoop(scanner);
            } else if (choice.equals("2")) {
                saveScore();
                System.out.println("До свидания!");
                break;
            } else {
                System.out.println("Выберете 1 или 2 действие");
            }
        }
    }

    public void gameLoop (Scanner scanner) {
        while (true) {
            System.out.println("Введите букву или команду (/? - помощь): ");
            String input = scanner.nextLine().toLowerCase();

            if (input.isEmpty()) {
                continue;
            }

            if (input.equals("/?")) {
                printHelp();
                continue;
            }

            if (input.equals("/") && !hintUsed) {
                useHint();
                continue;
            }

            if (input.length() != 1) {
                System.out.println("Введите одну букву");
                continue;
            }

            char letter = input.charAt(0);
            if (!Character.isLetter(letter)) {
                System.out.println("Введите букву русского алфавита");
                continue;
            }

            if (usedLetters.contains(letter)) {
                System.out.println("Вы вводили эту букву");
                continue;
            }

            usedLetters.add(letter);

            if (secretWord.indexOf(letter) >= 0) {
                System.out.println("Буква '" + letter + "' есть в слове:)");
                updateGuessedLetters(letter);
            } else {
                System.out.println("Такой буквы '" + letter + "' нет в слове:(");
                attempts--;
            }
            printGameState();

            if (isWordGuessed()) {
                System.out.println("Поздравляем! Вы угадали слово: " + secretWord);
                gamesWon++;
                break;
            }

            if (attempts == 0) {
                System.out.println("К сожалению, вы не угали слово:( Загаданное слово: " + secretWord);
                gamesLost++;
                break;
            }
        }
        gamesPlayed++;
    }

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
        System.out.println("/ - подсказка, открывает одну букву (уменьшает на 2 попытки)");
    }

    private void loadScore(){
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

    private void saveScore(){
        try (PrintWriter writer = new PrintWriter(SCORES_FILE)){
            writer.println(gamesPlayed);
            writer.println(gamesWon);
            writer.println(gamesLost);
        } catch (IOException e) {
            System.err.println("Не удалось сохранить результаты:(");
        }
    }
}