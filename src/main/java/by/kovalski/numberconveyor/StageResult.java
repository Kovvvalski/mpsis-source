package by.kovalski.numberconveyor;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class StageResult {
    int stageIndex;
    BinaryNumberPair pair;
    BinaryNumber partialProduct;
    BinaryNumber partialSum;
    int offset;

    @Override
    public String toString() {
        return "StageResult{" +
                "stageIndex=" + stageIndex +
                ", pair=" + pair +
                ", partialProduct=" + partialProduct +
                ", partialSum=" + partialSum +
                ", offset=" + offset +
                '}';
    }
}
