import packageM.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Encode {
    private ArrayList<Node<Character, Integer>> __frequencies = new ArrayList<Node<Character, Integer>>();

    public static void main(String[] args) throws IOException {
        Encode encode = new Encode();
//        String line = "aaabbbccdddeeerrttaaabbbccdddeeercxvxcvcxvrcxvxcvcxvcxvxcvcxvcxvxcvcxvttaaabbbccdddeeerrttaaabbbccdddeeerrtt";
        String line = "";

        Scanner sc = new Scanner(System.in, "utf-8");
        int ans;
        System.out.println("1. Ввод строки\n2. Посимвольно");
        do {
            System.out.print("Выберите пункт меню (1/2)\n---> ");
            while (!sc.hasNextInt()) {
                System.out.print("Это не число, повторите ввод\n---> ");
                sc.next();
            }
            ans = sc.nextInt();
        } while ((ans != 1 && ans != 2));

        if (ans == 1) {
            System.out.print("Введите строку: ");
            Scanner in = new Scanner(System.in, "windows-1251");
            line = in.nextLine();

        } else {

            System.out.print("Укажите количество символов: ");
            Scanner symb = new Scanner(System.in);
            int sym;

            class FreqHelper {
                char symbol;
                double probability;

                public FreqHelper(char C, double D) {
                    this.symbol = C;
                    this.probability = D;
                }

                public void setProb(double D) {
                    this.probability = D;
                }
            }

            ArrayList<FreqHelper> __f_helpers = new ArrayList<FreqHelper>();

            do {
                while (!symb.hasNextInt()) {
                    System.out.print("Это не число. Укажите количество символов: ");
                    symb.next();
                }
                sym = symb.nextInt();
            } while (sym <= 1);
            for (int i = 0; i < sym; i++) {
                Scanner symb_freq = new Scanner(System.in, "windows-1251");
                System.out.print("Введите символ -> ");
                Character symbol = symb_freq.nextLine().charAt(0);
                System.out.print("Введите его вероятность -> ");
                Double freq = symb_freq.nextDouble();
                FreqHelper buffer = new FreqHelper(symbol, freq);
                __f_helpers.add(buffer);
            }

            for (int i = 0; i < __f_helpers.size(); i++) {
                __f_helpers.get(i).setProb(__f_helpers.get(i).probability * 100);
                for (int j = 0; j < __f_helpers.get(i).probability; j++) {
                    line += __f_helpers.get(i).symbol;
                }
            }

        }

        encode.__calculateFrequency(line);

        encode.__sort();
        encode.__calculatePossibilities();
        encode.__generateCodes(encode.__frequencies);
        System.out.print("\nКодирование Шеннона-Фано\n-----------------------------------\n");
        for (int i = 0; i < encode.__frequencies.toArray().length; i++) {
            System.out.println(encode.__frequencies.get(i).getKey() + "(" + encode.__frequencies.get(i).getPossibility() + "):" + encode.__frequencies.get(i).getCode());
        }
    }

    private void __calculateFrequency(String line) {

        for (int i = 0; i < line.length(); i++) {
            boolean flag = true;

            for (Node<Character, Integer> n : this.__frequencies) {
                if (n.getKey().equals(line.charAt(i))) {
                    n.setValue(n.getValue() + 1);

                    flag = false;
                    break;
                }
            }

            if (flag) {
                this.__frequencies.add(new Node<Character, Integer>(line.charAt(i), 1));
            }
        }
    }

    private void __sort() {
        Collections.sort
                (
                        this.__frequencies,

                        new Comparator<Node<Character, Integer>>() {
                            @Override
                            public int compare(Node<Character, Integer> first, Node<Character, Integer> second) {
                                if (first.getValue() > second.getValue()) {
                                    return -1;
                                } else if (first.getValue() < second.getValue()) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        }
                );
    }

    private void __calculatePossibilities() {
        int amount_of_letter = 0;
        boolean counter = true;

        for (int i = 0; i < 2; i++) {
            for (Node<Character, Integer> n : this.__frequencies) {
                if (counter) {
                    amount_of_letter += n.getValue();
                } else {
                    n.setPossibility((double) n.getValue() / amount_of_letter);
                }
            }

            counter = false;
        }
    }

    private void __generateCodes(ArrayList<Node<Character, Integer>> list) {
        if (list.size() > 1) {
            ArrayList<Node<Character, Integer>> left = new ArrayList<Node<Character, Integer>>();
            ArrayList<Node<Character, Integer>> right = new ArrayList<Node<Character, Integer>>();

            class Helper {
                public double sum(ArrayList<Node<Character, Integer>> rest_of_list, int start) {
                    double result = 0d;

                    for (int i = start; i < rest_of_list.size(); i++) {
                        result += rest_of_list.get(i).getPossibility();
                    }

                    return result;
                }
            }

            Helper helper = new Helper();

            double weight = 0, min_weight = 1, total_possibility = 0;
            int min_weight_index = -1;

            for (int i = 0; i < list.size(); i++) {
                total_possibility += list.get(i).getPossibility();
            }

            for (int i = 0; i < list.size(); i++) {
                weight += list.get(i).getPossibility();
                if (Math.abs(weight - (total_possibility - weight)) < min_weight) {
                    min_weight = Math.abs(weight - (total_possibility - weight));
                    min_weight_index = i;
                }
                if (i >= Math.floor(list.size() / 2) - 1) {
                    break;
                }
            }

            for (int i = 0; i < list.size(); i++) {

                if (i <= min_weight_index) {
                    list.get(i).setCode(list.get(i).getCode() + '1');
                    left.add(list.get(i));
                } else {
                    list.get(i).setCode(list.get(i).getCode() + '0');
                    right.add(list.get(i));
                }

            }

            this.__generateCodes(left);
            this.__generateCodes(right);
        }
    }

    private String __encoding(String line) {
        /**
         * This function encodes line according as codes that generated before
         * and return enocded line for writing to the encoded file.
         */

        String encoded = "";

        for (int i = 0; i < line.length(); i++) {
            for (Node<Character, Integer> n : this.__frequencies) {
                if (line.charAt(i) == n.getKey()) {
                    encoded += n.getCode();
                }
            }
        }

        return encoded;
    }
}