package by.kovalski.numberconveyor;

/*
  Лабораторная работа №1 по дисциплине Модели решения задач в интеллектуальных системах
  Выполнена студентами гр. 221703 БГУИР Быльковым Даниилом Владимировичем, Аврукевичем Константином Сергеевичем
  Файл описывает класс результата выполнения этапа конвейера
  Вариант 13, 14
  26.03.25
 */

import lombok.AllArgsConstructor;

@AllArgsConstructor
class StageResult {
    int stageIndex;
    BinaryNumberPair pair;
    BinaryNumber partialProduct;
    int offset;

    @Override
    public String toString() {
        return String.format(
                "➤ Шаг: %d | Пара: %s | Частичное произведение: %s | Смещение: %d",
                stageIndex,
                pair != null ? pair : "Пусто",
                partialProduct != null ? partialProduct : "Пусто",
                offset
        );
    }
}
