package by.kovalski.numberconveyor;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class BinaryNumberPair {
    int pairIndex;
    BinaryNumber first;
    BinaryNumber second;

    @Override
    public String toString() {
        return String.format("Pair #%d: [%s] * [%s]",
                pairIndex,
                first.toString(),
                second.toString()
        );
    }
}
