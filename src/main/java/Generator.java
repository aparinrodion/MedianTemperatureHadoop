import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Generator {
    public static void main(String[] args) throws IOException {
        List<String> data = new LinkedList<>();
        Random r = new Random();
        int rangeMin = -10;
        int rangeMax = 20;
        double randomValue;
        FileWriter fileWriter = new FileWriter("data_temp.txt", false);
        for (int i = 1; i <= 100001; i++) {
            randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            data.add("Belarus " + String.format("%,.1f", randomValue) + "\n");
        }
        rangeMin = 0;
        rangeMax = 30;
        for (int i = 1; i <= 100001; i++) {
            randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            data.add("USA " + String.format("%,.1f", randomValue) + "\n");
        }
        rangeMin = 5;
        rangeMax = 25;
        for (int i = 1; i <= 100001; i++) {
            randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            data.add("Ukraine " + String.format("%,.1f", randomValue) + "\n");
        }
        rangeMin = 10;
        rangeMax = 40;
        for (int i = 1; i <= 100001; i++) {
            randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            data.add("Australia " + String.format("%,.1f", randomValue) + "\n");
        }
        rangeMin = 15;
        rangeMax = 30;
        for (int i = 1; i <= 100001; i++) {
            randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
            data.add("Italy " + String.format("%,.1f", randomValue) + "\n");
        }
        Collections.shuffle(data);
        for (int i = 0; i < data.size(); i++) {
            fileWriter.append(data.get(i));
        }
        fileWriter.close();
    }
}
