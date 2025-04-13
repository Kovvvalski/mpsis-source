package by.kovalski.numberconveyor;

public class MultiplicandShiftLeftPipelineStage extends PipelineStage {

    MultiplicandShiftLeftPipelineStage(int stageIndex) {
        super(stageIndex);
    }

    @Override
    StageResult apply() {
        if (operands.offset == 0) {
            boolean[] op1_16bit = new boolean[16];
            boolean[] op2_16bit = new boolean[16];
            System.arraycopy(operands.first.getBinary(), 0, op1_16bit, 8, 8);
            System.arraycopy(operands.second.getBinary(), 0, op2_16bit, 8, 8);
            operands.first.setBinary(op1_16bit);
            operands.second.setBinary(op2_16bit);
        }
        if (operands.offset < bitQuantity) {
            int bits = operands.first.getBinary().length;
            boolean multiplierBit = operands.second.getBinary()[bits - 1 - operands.offset];
            boolean[] partialProductArray = new boolean[bits];
            BinaryNumber partialProduct = new BinaryNumber(partialProductArray);
            if (multiplierBit) {
                System.arraycopy(operands.first.getBinary(), 0, partialProductArray, 0, bits);
            }
            operands.partialSum.setBinary(calculatePartialSum(operands.partialSum.getBinary(), partialProductArray));
            operands.first.shift(-1);
            return new StageResult(stageIndex, operands, partialProduct, operands.offset++);
        }
        return new StageResult(stageIndex, operands, null, operands.offset++);
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
