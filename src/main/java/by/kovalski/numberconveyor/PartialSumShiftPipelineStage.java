package by.kovalski.numberconveyor;

public class PartialSumShiftPipelineStage extends PipelineStage {

    public PartialSumShiftPipelineStage(int stageIndex) {
        super(stageIndex);
    }

    @Override
    StageResult apply() {
        int bits = operands.first.getBinary().length;
        if (partialSum == null) {
            partialSum = new BinaryNumber(new boolean[bits * 2]);
        }
        boolean multiplierBit = operands.second.getBinary()[bits - 1 - offset];
        boolean[] partialProductArray = new boolean[bits * 2];
        BinaryNumber partialProduct = new BinaryNumber(partialProductArray);
        if (multiplierBit) {
            System.arraycopy(operands.first.getBinary(), 0, partialProductArray, 0, bits);
        }
        partialSum.setBinary(calculatePartialSum(partialSum.getBinary(), partialProductArray));
        return new StageResult(stageIndex, operands, partialProduct, partialSum, offset);
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
