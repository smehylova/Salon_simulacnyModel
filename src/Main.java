import Generators.*;
import Simulation.Salon;
import Statistics.StatAverage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        FormMain main = new FormMain();
        /*try {
            FileWriter myWriter = new FileWriter("filename.txt");

            ArrayList<EmpiricParameter> parameters = new ArrayList<>(3);
            parameters.add(new EmpiricParameter(50, 60, 0.2));
            parameters.add(new EmpiricParameter(61, 100, 0.3));
            parameters.add(new EmpiricParameter(101, 150, 0.5));
            GeneratorContinuousUniform generator = new GeneratorContinuousUniform(50, 100);
            //GeneratorExponential generator = new GeneratorExponential(1/5.0);

            for (int i = 0; i < 1000000; i++) {
                myWriter.write(generator.nextValue() + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }*/


        /*StatAverage stat;
        StatAverage statReplications = new StatAverage();
        for (int replication = 0; replication < 100000; replication++) {
            stat = new StatAverage();

            Random generatorHair = new Random();
            GeneratorDiscreteUniform generatorEasyHair = new GeneratorDiscreteUniform(10, 30);

            ArrayList<EmpiricParameter> arrayHardHair = new ArrayList<>();
            arrayHardHair.add(new EmpiricParameter(30, 60, 0.4));
            arrayHardHair.add(new EmpiricParameter(61, 120, 0.6));
            GeneratorEmpiric generatorHardHair = new GeneratorEmpiric(arrayHardHair);

            ArrayList<EmpiricParameter> arrayWeddingHair = new ArrayList<>();
            arrayWeddingHair.add(new EmpiricParameter(50, 60, 0.2));
            arrayWeddingHair.add(new EmpiricParameter(61, 100, 0.3));
            arrayWeddingHair.add(new EmpiricParameter(101, 150, 0.5));
            GeneratorEmpiric generatorWeddingHair = new GeneratorEmpiric(arrayWeddingHair);

            for (int i = 0; i < 1000; i++) {
                double randHair = generatorHair.nextDouble();
                int duration;
                if (randHair < 0.4) {
                    duration = generatorEasyHair.nextValueInt() * 60;
                } else if (randHair < 0.8) {
                    duration = generatorHardHair.nextValueInt() * 60;
                } else {
                    duration = generatorWeddingHair.nextValueInt() * 60;
                }
                stat.addStatistic(duration);
            }
            statReplications.addStatistic(stat.getResult());
        }
        System.out.println(statReplications.getResult());*/

    }
}
