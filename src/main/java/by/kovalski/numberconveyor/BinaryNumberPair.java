package by.kovalski.numberconveyor;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class BinaryNumberPair {
    int pairIndex;
    BinaryNumber first;
    BinaryNumber second;
    BinaryNumber first16 = null;
    BinaryNumber second16 = null;

    public BinaryNumberPair(int id, BinaryNumber first, BinaryNumber second) {
        this.pairIndex = id;
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("Pair #%d: [%s] * [%s]",
                pairIndex,
                first.toString(),
                second.toString()
        );
    }
}
