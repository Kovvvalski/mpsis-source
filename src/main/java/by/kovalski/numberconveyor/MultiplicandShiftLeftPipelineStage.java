package by.kovalski.numberconveyor;

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

    private BinaryNumber get16bitBinary(boolean[] arr_8bit) {
        boolean[] arr_16bit = new boolean[16];
        System.arraycopy(arr_8bit, 0, arr_16bit, 8 ,8);
        return new BinaryNumber(arr_16bit);
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
