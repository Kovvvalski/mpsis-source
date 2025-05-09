package by.kovalski.numberconveyor;


/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает класс двоичного числа
  Вариант 13, 14
  26.03.25
 */

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BinaryNumber {
    private boolean[] binary;

    public BinaryNumber(int decimal, int bitLength) {
        binary = new boolean[bitLength];
        for (int i = bitLength - 1; i >= 0; i--) {
            binary[i] = (decimal & 1) == 1;
            decimal >>= 1;
        }
    }

    public void shift(int offset) {
        boolean[] result = new boolean[binary.length];
        if (offset > 0) {
            System.arraycopy(binary, 0, result, 1, binary.length - 1);
            binary = result;
            shift(offset - 1);
        } else if (offset < 0) {
            System.arraycopy(binary, 1, result, 0, binary.length - 1);
            binary = result;
            shift(offset + 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean bit : binary) {
            sb.append(bit ? '1' : '0');
        }
        return sb.toString();
    }

    public int toDecimal() {
        int value = 0;
        for (boolean bit : binary) {
            value = (value << 1) | (bit ? 1 : 0);
        }
        return value;
    }
}
