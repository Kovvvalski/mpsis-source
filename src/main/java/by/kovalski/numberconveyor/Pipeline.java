package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает класс конвейера
  Вариант 13, 14
  26.03.25
 */

import java.util.*;
import java.util.concurrent.*;

public class Pipeline<T extends PipelineStage> {

    private List<T> stages;
    private int threadQuantity;
    private Queue<BinaryNumberPair> inputQueue;
    private Map<BinaryNumberPair, BinaryNumber> outputMap;
    private int bitQuantity;
    private StringBuilder logBuffer; // Буфер логов

    public Pipeline(Queue<BinaryNumberPair> pairs, int bitQuantity, int threadQuantity, Class<T> stageType) {
        this.threadQuantity = threadQuantity;
        this.inputQueue = pairs;
        this.bitQuantity = bitQuantity;
        this.outputMap = new HashMap<>();
        this.logBuffer = new StringBuilder();
        initializeStages(stageType);
    }

    public Map<BinaryNumberPair, BinaryNumber> process() {
        ExecutorService executor = Executors.newFixedThreadPool(threadQuantity);
        int movementsQuantity = bitQuantity + inputQueue.size();

        try {
            pushState();
            for (int i = 0; i < movementsQuantity; i++) {
                stages.get(stages.size() - 1).move(inputQueue, outputMap);
                List<Future<StageResult>> futures = new ArrayList<>();

                for (PipelineStage stage : stages) {
                    if (stage.operands != null) {
                        futures.add(executor.submit(stage::apply));
                    }
                }

                for (Future<StageResult> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                logBuffer.append("\n=== Такт ").append(i + 1).append(" ===\n");
                pushState();
            }
        } finally {
            executor.shutdown();
        }
        return outputMap;
    }

    private void initializeStages(Class<T> stageClass) {
        stages = new ArrayList<>();
        try {
            for (int i = 0; i < bitQuantity; i++) {
                T stage = stageClass.getDeclaredConstructor(int.class).newInstance(i + 1);
                stages.add(stage);
                if (i != 0) {
                    T previousStage = stages.get(i - 1);
                    previousStage.next = stage;
                    stage.prev = previousStage;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating pipeline stages", e);
        }
    }

    public void pushState() {
        logBuffer.append("Входная очередь:\n");
        inputQueue.forEach(pair -> logBuffer.append(pair).append("\n"));

        logBuffer.append("\nСостояние конвейера:\n");
        for (PipelineStage stage : stages) {
            logBuffer.append("Шаг ").append(stage.stageIndex).append(" | ")
                    .append("Операнды: ").append(stage.operands != null ? stage.operands : "Пусто").append(" | ")
                    .append("Частичная сумма: ").append(stage.partialSum != null ? stage.partialSum : "Пусто").append(" |\n");
        }

        logBuffer.append("\nВыход конвейера:\n");
        outputMap.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().pairIndex))
                .forEach(e -> logBuffer.append(e.getKey().pairIndex).append(": ").
                        append(e.getKey()).append(" -> ").append(e.getValue()).append("\n"));
    }

    public void flushLog() {
        System.out.print(logBuffer.toString());
        logBuffer.setLength(0);
    }
}
