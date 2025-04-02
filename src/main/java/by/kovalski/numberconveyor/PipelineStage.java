package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает абстрактный класс этапа конвейера
  Вариант 13, 14
  26.03.25
 */

import java.util.Map;
import java.util.Queue;

public abstract class PipelineStage {
    int stageIndex;
    PipelineStage next;
    PipelineStage prev;
    BinaryNumberPair operands;
    BinaryNumber partialSum;
    int offset; // sequential number of partial production

    PipelineStage(int stageIndex) {
        this.stageIndex = stageIndex;
    }

    abstract StageResult apply();

    void move(Queue<BinaryNumberPair> inputQueue, Map<BinaryNumberPair, BinaryNumber> outputMap) {
        if (prev != null) { // if current node is not first
            if (operands != null && next == null) { // if node is not empty and node is last
                outputMap.put(operands, partialSum);
            }
            operands = prev.operands;
            partialSum = prev.partialSum;
            this.offset = ++prev.offset;
            prev.move(inputQueue, outputMap);
        } else { // if current node is the first
            this.operands = inputQueue.poll();
            offset = 0;
            partialSum = null;
        }
    }
}
