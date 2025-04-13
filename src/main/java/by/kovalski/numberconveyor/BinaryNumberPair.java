package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает класс пары двоичных чисел
  Вариант 13, 14
  26.03.25
 */

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class BinaryNumberPair {
    int pairIndex;
    BinaryNumber first;
    BinaryNumber second;
    int offset;

    @Override
    public String toString() {
        return String.format("Pair #%d: [%s] * [%s]",
                pairIndex,
                first.toString(),
                second.toString()
        );
    }
}
