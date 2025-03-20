package by.kovalski.numberconveyor;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class BinaryNumberPair {
    int pairIndex; // уникальный индекс пары
    BinaryNumber first;
    BinaryNumber second;

    @Override
    public String toString() {
        return String.format("Pair #%d: [%s (%d)] && [%s (%d)]",
                pairIndex,
                first.toString(), first.toDecimal(),
                second.toString(), second.toDecimal()
        );
    }
}
