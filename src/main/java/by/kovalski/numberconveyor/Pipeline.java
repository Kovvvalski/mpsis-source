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
    private Queue<BinaryNumberPair> inputQueue;
    private Map<BinaryNumberPair, BinaryNumber> outputMap;
    private int bitQuantity;
    private int stageQuantity;
    private StringBuilder logBuffer; // Буфер логов

    public Pipeline(Queue<BinaryNumberPair> pairs, int bitQuantity, int stageQuantity, Class<T> stageType) {
        this.inputQueue = pairs;
        this.bitQuantity = bitQuantity;
        this.outputMap = new HashMap<>();
        this.logBuffer = new StringBuilder();
        this.stageQuantity = stageQuantity;
        initializeStages(stageType);
    }

    public Map<BinaryNumberPair, BinaryNumber> process() {
            pushState();
            int pairsQuantity = inputQueue.size();
            for (int i = 0; outputMap.size() != pairsQuantity; i++) {
                stages.get(stages.size() - 1).move(inputQueue, outputMap);

                for (PipelineStage stage : stages) {
                    if (stage.operands != null) {
                        StageResult result = stage.apply();
                    }
                }
                logBuffer.append("\n=== Такт ").append(i + 1).append(" ===\n");
                pushState();
            }
        return outputMap;
    }

    private void initializeStages(Class<T> stageClass) {
        stages = new ArrayList<>();
        try {
            for (int i = 0; i < stageQuantity; i++) {
                T stage = stageClass.getDeclaredConstructor(int.class).newInstance(i + 1);
                stage.bitQuantity = bitQuantity;
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
                    .append("Операнды: ").append(stage.operands != null ? stage.operands : "Пусто").append('\n');
                    //.append("Частичная сумма: ").append(stage.partialSum != null ? stage.partialSum : "Пусто").append(" |\n");
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
