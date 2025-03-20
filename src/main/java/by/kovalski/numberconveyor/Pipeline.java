package by.kovalski.numberconveyor;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Pipeline<T extends PipelineStage> {

    private List<T> stages;
    private int threadQuantity;
    private Queue<BinaryNumberPair> inputQueue;
    private Map<BinaryNumberPair, BinaryNumber> outputMap;
    int bitQuantity;


    public Pipeline(Queue<BinaryNumberPair> pairs, int bitQuantity, int threadQuantity, Class<T> stageType) {
        this.threadQuantity = threadQuantity;
        this.inputQueue = pairs;
        this.bitQuantity = bitQuantity;
        outputMap = new HashMap<>();
        initializeStages(stageType);
    }

    public Map<BinaryNumberPair, BinaryNumber> process() {
        ExecutorService executor = Executors.newFixedThreadPool(threadQuantity);
        int movementsQuantity = bitQuantity + inputQueue.size();
        try {
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

}
