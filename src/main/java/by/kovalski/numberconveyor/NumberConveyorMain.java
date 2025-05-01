package by.kovalski.numberconveyor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class NumberConveyorMain {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Укажите путь к входному файлу в качестве аргумента.");
            return;
        }

        String inputFile = args[0]; // Путь к файлу передаётся как аргумент
        int stageQuantity;
        int bitQuantity;
        Class<?> pipelineStageClass;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))) {
            stageQuantity = Integer.parseInt(br.readLine().trim());
            bitQuantity = Integer.parseInt(br.readLine().trim());
            String stageClassName = br.readLine().trim();

            try {
                pipelineStageClass = Class.forName(stageClassName);
            } catch (ClassNotFoundException e) {
                System.err.println("Ошибка: Класс шага конвейера не найден - " + stageClassName);
                return;
            }

            Queue<BinaryNumberPair> inputQueue = new LinkedList<>();
            String line;
            int id = 1;

            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length == 2) {
                    int num1 = Integer.parseInt(parts[0]);
                    int num2 = Integer.parseInt(parts[1]);

                    BinaryNumberPair pair = new BinaryNumberPair(
                            id++,
                            new BinaryNumber(num1, bitQuantity),
                            new BinaryNumber(num2, bitQuantity),
                            new BinaryNumber(new boolean[bitQuantity * 2]),
                            0
                    );

                    inputQueue.add(pair);
                }
            }

            @SuppressWarnings("unchecked")
            Pipeline<?> pipeline = new Pipeline<>(
                    inputQueue,
                    bitQuantity,
                    stageQuantity,
                    (Class<? extends PipelineStage>) pipelineStageClass
            );

            pipeline.process();

        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
