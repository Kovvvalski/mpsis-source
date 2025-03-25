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
        return String.format(
                "➤ Шаг: %d | Пара: %s | Частичное произведение: %s | Частичная сумма: %s | Смещение: %d",
                stageIndex,
                pair != null ? pair : "Пусто",
                partialProduct != null ? partialProduct : "Пусто",
                partialSum != null ? partialSum : "Пусто",
                offset
        );
    }
}
