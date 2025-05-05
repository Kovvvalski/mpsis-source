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
        printState(0, null);
        int stepCounter = 0;
        int pairsQuantity = inputQueue.size();
        for (int i = 0; true; i++) {
            stages.get(stages.size() - 1).move(inputQueue, outputMap);
            if (outputMap.size() == pairsQuantity) {
                printOutputMap(stepCounter);
                break;
            }
            List<StageResult> results = new ArrayList<>();
            for (int j = 0; j < bitQuantity / stageQuantity; j++) {
                for (PipelineStage stage : stages) {
                    results.add(stage.operands != null ? stage.apply() : null);
                }
                printState(++stepCounter, results);
            }
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
        clearConsole();
        System.out.println("\n=== Такт " + cycleNumber + " ===");

        System.out.println("Входная очередь:");
        inputQueue.forEach(pair -> System.out.println(pair));

        System.out.println("\nСостояние конвейера:");
//        if (stageQuantity == bitQuantity) {
//            for (int i = 0; i < stages.size(); i++) {
//                PipelineStage stage = stages.get(i);
//                StageResult result = stageResults != null ? stageResults.get(i) : null;
//
//                String pairStr = result != null && result.pair != null ? result.pair.toString() : "Пусто";
//                String sumStr = result != null && result.pair != null ? result.pair.partialSum.toString() : "Пусто";
//                String productStr = result != null && result.partialProduct != null ? result.partialProduct.toString() : "Пусто";
//
//                System.out.printf("Стадия %d || %s || Ч.П. %s || Ч.С. %s %n", stage.stageIndex, pairStr, productStr, sumStr);
//            }
//        } else {
        for (PipelineStage stage : stages) {
            String pairStr = stage.operands != null ? stage.operands.toString() : "Пусто";
            String sumStr = stage.operands != null ? stage.operands.partialSum.toString() : "Пусто";
            System.out.printf("Этап %d || %s || Ч.С. %s %n", stage.stageIndex, pairStr, sumStr);
        }
//        }

        System.out.println("\nВыход конвейера:");
        outputMap.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().pairIndex))
                .forEach(e -> System.out.println(
                        e.getKey().pairIndex + ": " + e.getKey() + " -> " + e.getValue()
                ));

        System.out.println("\nНажмите Enter, чтобы продолжить...");
        new Scanner(System.in).nextLine();
    }

    private void printOutputMap(int stages) {
        clearConsole();
        System.out.println("Результат работы конвейера:");
        System.out.printf("Количество тактов: %d%n", stages);
        outputMap.entrySet().stream()
                .sorted(Comparator.comparingInt(o -> o.getKey().pairIndex))
                .forEach(o -> System.out.printf("%s = %s = %d%n", o.getKey().toString(),
                        o.getValue().toString(), o.getValue().toDecimal()));

    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
