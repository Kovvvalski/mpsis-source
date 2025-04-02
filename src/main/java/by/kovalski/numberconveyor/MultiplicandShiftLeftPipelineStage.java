package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает класс этапа конвейера со сдвигом множимого влево
  Вариант 13, 14
  26.03.25
 */

public class MultiplicandShiftLeftPipelineStage extends PipelineStage{

    MultiplicandShiftLeftPipelineStage(int stageIndex) {
        super(stageIndex);
    }

    @Override
    StageResult apply() {
        if (partialSum == null) {
            partialSum = new BinaryNumber(new boolean[16]);
            operands.first16 = get16bitBinary(operands.first.getBinary());
            operands.second16 = get16bitBinary(operands.second.getBinary());
        }
        int bits = operands.first16.getBinary().length;
        boolean multiplierBit = operands.second16.getBinary()[bits - 1 - offset];
        boolean[] partialProductArray = new boolean[bits * 2];
        BinaryNumber partialProduct = new BinaryNumber(partialProductArray);
        if (multiplierBit) {
            System.arraycopy(operands.first16.getBinary(), 0, partialProductArray, 0, bits);
        }
        partialSum.setBinary(calculatePartialSum(partialSum.getBinary(), partialProductArray));
        operands.first16.shift(-1);
        return new StageResult(stageIndex, operands, partialProduct, partialSum, offset);
    }

    private BinaryNumber get16bitBinary(boolean[] arr8) {
        boolean[] arr16 = new boolean[16];
        System.arraycopy(arr8, 0, arr16, 8 ,8);
        return new BinaryNumber(arr16);
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

        return result;
    }
}
