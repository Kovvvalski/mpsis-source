package by.kovalski.main;

import by.kovalski.numberconveyor.BinaryNumber;
import by.kovalski.numberconveyor.BinaryNumberPair;
import by.kovalski.numberconveyor.PartialSumShiftPipelineStage;
import by.kovalski.numberconveyor.Pipeline;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class NumberConveyorMain {
    public static void main(String[] args) {
        int bitQuantity = 15;      // Длина каждого числа — 15 бит
        int threadQuantity = 3;   // Количество потоков для обработки
        int pairCount = 10000;      // Количество входных пар

        // Очередь пар чисел для обработки
        Queue<BinaryNumberPair> inputQueue = new LinkedList<>();
        Random random = new Random();

        // Генерация тысяч входных пар
        for (int i = 1; i <= pairCount; i++) {
            boolean[] num1 = generateRandomBinary(bitQuantity, random);
            boolean[] num2 = generateRandomBinary(bitQuantity, random);
            inputQueue.add(new BinaryNumberPair(i, new BinaryNumber(num1), new BinaryNumber(num2)));
        }

        // Засекаем время перед выполнением

        // Создаём и запускаем конвейер обработки
        Pipeline<PartialSumShiftPipelineStage> pipeline = new Pipeline<>(inputQueue, bitQuantity, threadQuantity,
                PartialSumShiftPipelineStage.class);
        long startTime = System.nanoTime();
        Map<BinaryNumberPair, BinaryNumber> result = pipeline.process();
        // Засекаем время после выполнения
        long endTime = System.nanoTime();
        long executionTime = (endTime - startTime) / 1_000_000; // Время в миллисекундах

        // Вывод результатов (ограничим, чтобы не засорять консоль)
        System.out.println("\nРезультаты обработки (первые 10 пар):");
        result.entrySet().stream().limit(10).forEach(entry ->
                System.out.println(entry.getKey() + " -> " + entry.getValue())
        );

        // Вывод времени выполнения
        System.out.println("\n⏱ Время выполнения программы: " + executionTime + " мс");
    }

    // Метод для генерации случайного 15-битного числа
    private static boolean[] generateRandomBinary(int length, Random random) {
        boolean[] binary = new boolean[length];
        for (int i = 0; i < length; i++) {
            binary[i] = random.nextBoolean();
        }
        return binary;
    }
}
