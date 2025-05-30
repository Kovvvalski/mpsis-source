package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает класс этапа конвейера со сдвигом частичной суммы вправо
  Вариант 13, 14
  26.03.25
 */

public class PartialSumShiftRightPipelineStage extends PipelineStage {

    public PartialSumShiftRightPipelineStage(int stageIndex) {
        super(stageIndex);
    }

    @Override
    StageResult apply() {
        if (operands.offset < bitQuantity) { // if operands are not calculated
            boolean multiplierBit = operands.second.getBinary()[bitQuantity - 1 - operands.offset];
            boolean[] partialProductArray = new boolean[bitQuantity * 2];
            BinaryNumber partialProduct = new BinaryNumber(partialProductArray);
            if (multiplierBit) {
                System.arraycopy(operands.first.getBinary(), 0, partialProductArray, 0, bitQuantity);
            }
            operands.partialSum.setBinary(calculatePartialSum(operands.partialSum.getBinary(), partialProductArray));
            return new StageResult(stageIndex, operands, partialProduct, operands.offset++);
        }
        return new StageResult(stageIndex, operands, null, operands.offset);
    }

    private boolean[] calculatePartialSum(boolean[] binary1, boolean[] binary2) {
        int length = binary1.length;
        boolean[] result = new boolean[length];
        boolean carry = false;

        for (int i = length - 1; i >= 0; i--) {
            boolean sum = binary1[i] ^ binary2[i] ^ carry;
            carry = (binary1[i] && binary2[i]) || (binary1[i] && carry) || (binary2[i] && carry);
            result[i] = sum;
        }

        boolean[] newResult = new boolean[length];
        System.arraycopy(result, 0, newResult, 1, length - 1);
        newResult[0] = carry;

        return newResult;
    }
}
