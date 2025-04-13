package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает класс конвейера
  Вариант 13, 14
  26.03.25
 */

import java.util.*;

public class Pipeline<T extends PipelineStage> {

    private List<T> stages;
    private Queue<BinaryNumberPair> inputQueue;
    private Map<BinaryNumberPair, BinaryNumber> outputMap;
    private int bitQuantity;
    private int stageQuantity;

    public Pipeline(Queue<BinaryNumberPair> pairs, int bitQuantity, int stageQuantity, Class<T> stageType) {
        this.inputQueue = pairs;
        this.bitQuantity = bitQuantity;
        this.outputMap = new HashMap<>();
        this.stageQuantity = stageQuantity;
        initializeStages(stageType);
    }

    public Map<BinaryNumberPair, BinaryNumber> process() {
        printState(0, null); // нет результатов на старте

        int pairsQuantity = inputQueue.size();
        for (int i = 0; outputMap.size() != pairsQuantity; i++) {
            stages.get(stages.size() - 1).move(inputQueue, outputMap);

            List<StageResult> results = new ArrayList<>();
            for (PipelineStage stage : stages) {
                if (stage.operands != null) {
                    StageResult result = stage.apply();
                    results.add(result);
                } else {
                    results.add(null);
                }
            }

            printState(i + 1, results);
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


    public void printState(int cycleNumber, List<StageResult> stageResults) {
        System.out.println("\n=== Такт " + cycleNumber + " ===");

        System.out.println("Входная очередь:");
        inputQueue.forEach(pair -> System.out.println(pair));

        System.out.println("\nСостояние конвейера:");
        for (int i = 0; i < stages.size(); i++) {
            PipelineStage stage = stages.get(i);
            StageResult result = stageResults != null ? stageResults.get(i) : null;

            String pairStr = result != null && result.pair != null ? result.pair.toString() : "Пусто";
            String productStr = result != null && result.partialProduct != null ? result.partialProduct.toString() : "Пусто";

            System.out.printf("Стадия %d - %s - %s%n", stage.stageIndex, pairStr, productStr);
        }

        System.out.println("\nВыход конвейера:");
        outputMap.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().pairIndex))
                .forEach(e -> System.out.println(
                        e.getKey().pairIndex + ": " + e.getKey() + " -> " + e.getValue()
                ));

        System.out.println("\nНажмите Enter, чтобы продолжить...");
        new Scanner(System.in).nextLine();
    }
}
