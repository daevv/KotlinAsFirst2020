@file:Suppress("UNUSED_PARAMETER")

package lesson12.task1

import kotlin.math.abs

/**
 * Класс "табличная функция".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса хранит таблицу значений функции (y) от одного аргумента (x).
 * В таблицу можно добавлять и удалять пары (x, y),
 * найти в ней ближайшую пару (x, y) по заданному x,
 * найти (интерполяцией или экстраполяцией) значение y по заданному x.
 *
 * Класс должен иметь конструктор по умолчанию (без параметров).
 */
class TableFunction {

    private val table = mutableMapOf<Double, Double>()

    private val arguments = mutableSetOf<Double>()

    /**
     * Количество пар в таблице
     */
    val size: Int get() = table.size

    /**
     * Добавить новую пару.
     * Вернуть true, если пары с заданным x ещё нет,
     * или false, если она уже есть (в этом случае перезаписать значение y)
     */
    fun add(x: Double, y: Double): Boolean {
        val res = x !in table.keys
        table[x] = y
        arguments.add(x)
        return res
    }

    /**
     * Удалить пару с заданным значением x.
     * Вернуть true, если пара была удалена.
     */
    fun remove(x: Double): Boolean {
        if (x !in table.keys) return false
        table.remove(x)
        arguments.remove(x)
        return true
    }

    /**
     * Вернуть коллекцию из всех пар в таблице
     */
    fun getPairs(): Collection<Pair<Double, Double>> {
        val res = mutableSetOf<Pair<Double, Double>>()
        for (el in table) {
            res.add(el.toPair())
        }
        return res
    }

    /**
     * Вернуть пару, ближайшую к заданному x.
     * Если существует две ближайшие пары, вернуть пару с меньшим значением x.
     * Если таблица пуста, бросить IllegalStateException.
     */
    fun findPair(x: Double): Pair<Double, Double>? {
        if (table.isEmpty()) throw IllegalStateException()
        var min = Double.MAX_VALUE
        for (el in table.keys) {
            if (abs(x - el) < min) {
                min = el
            }
        }
        return Pair(min, table[min]!!)
    }

    /**
     * Вернуть значение y по заданному x.
     * Если в таблице есть пара с заданным x, взять значение y из неё.
     * Если в таблице есть всего одна пара, взять значение y из неё.
     * Если таблица пуста, бросить IllegalStateException.
     * Если существуют две пары, такие, что x1 < x < x2, использовать интерполяцию.
     * Если их нет, но существуют две пары, такие, что x1 < x2 < x или x > x2 > x1, использовать экстраполяцию.
     */
    fun getValue(x: Double): Double {
        if (table.isEmpty()) throw IllegalStateException()
        if (table[x] != null) return table[x]!!
        if (table.size == 1) return table[arguments.first()]!!

        val x1 = arguments.maxOrNull()!!
        arguments.remove(x1)
        val x2 = arguments.maxOrNull()!!
        arguments.add(x1)
        return table[x1]!! + (x - x1) / (x2 - x1) * (table[x2]!! - table[x1]!!)
    }


    /**
     * Таблицы равны, если в них одинаковое количество пар,
     * и любая пара из второй таблицы входит также и в первую
     */
    override fun equals(other: Any?): Boolean {
        if (other is TableFunction) {
            if (arguments.size != other.arguments.size) return false
            for (key in table.keys) {
                if (table[key] != other.table[key]) return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = table.hashCode()
        result = 31 * result + arguments.hashCode()
        return result
    }
}