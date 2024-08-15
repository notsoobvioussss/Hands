package com.timur.hands.data

fun generateRandomCell(): CellState {
    return if ((0..1).random() == 0) CellState.ALIVE else CellState.DEAD
}

fun checkLifeCreationOrDeath(cells: List<CellState>): String? {
    if (cells.size < 3) return null

    val lastThree = cells.takeLast(3)

    return when {
        lastThree.all { it == CellState.ALIVE } -> "Новая жизнь!"
        lastThree.all { it == CellState.DEAD } -> "Жизнь умирает..."
        else -> null
    }
}