package by.kovalski.numberconveyor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BinaryNumber {
    private boolean[] binary;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean bit : binary) {
            sb.append(bit ? '1' : '0');
        }
        return sb.append("==(").append(toDecimal()).append(')').toString();
    }

    public int toDecimal() {
        int value = 0;
        for (boolean bit : binary) {
            value = (value << 1) | (bit ? 1 : 0);
        }
        return value;
    }
}