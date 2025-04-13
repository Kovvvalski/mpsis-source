package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл является исполнительным файлом программы
  Вариант 13, 14
  26.03.25
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class NumberConveyorMain {
    public static void main(String[] args) {
        String inputFile = "src/main/resources/input.txt"; // Файл с входными данными
        int threadQuantity;
        int bitQuantity;
        Class<?> pipelineStageClass;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            threadQuantity = Integer.parseInt(br.readLine().trim());
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
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    int num1 = Integer.parseInt(parts[0]);
                    int num2 = Integer.parseInt(parts[1]);
                    inputQueue.add(new BinaryNumberPair(id++, new BinaryNumber(num1, bitQuantity), new BinaryNumber(num2, bitQuantity), 0));
                }
            }

            @SuppressWarnings("unchecked")
            Pipeline<?> pipeline = new Pipeline<>(inputQueue, bitQuantity, threadQuantity,
                    (Class<? extends by.kovalski.numberconveyor.PipelineStage>) pipelineStageClass);

            long startTime = System.nanoTime();
            pipeline.process();
            pipeline.flushLog();
            long endTime = System.nanoTime();

            long executionTime = (endTime - startTime) / 1_000_000;

            System.out.println("\n⏱ Время выполнения конвейера: " + executionTime + " мс");

        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
