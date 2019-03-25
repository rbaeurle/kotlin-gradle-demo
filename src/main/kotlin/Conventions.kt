/**
 * https://play.kotlinlang.org/koans/Conventions/
 */
fun main(args: Array<String>) {

    println( compare(MyDate(1967,10,26), MyDate(1970,5,14)))
    println( checkInRange(MyDate(2002,9,15),
        MyDate(1967,10,26),
        MyDate(1970,5,14)))
    println( checkInRange2(MyDate(2002,9,15),
        MyDate(1967,10,26),
        MyDate(1970,5,14)))
    for ( d in  MyDate(2019,3,19)..MyDate(2019,3,22)) {
        println(d)
    }
    println(task1(MyDate(1967,10,26)))
    println(task2(MyDate(1967,10,26)))
    println(isLeapDay(MyDate(2000,2,29)))
    println(invokeTwice(Invokable()))
}

//
// Comparison
//
//Read about operator overloading
// https://kotlinlang.org/docs/reference/operator-overloading.html
//
data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {

    override fun compareTo(other: MyDate) =
            when {
                year != other.year -> year - other.year
                month != other.month -> month - other.month
                else -> dayOfMonth - other.dayOfMonth
            }
}

fun compare(date1: MyDate, date2: MyDate) = date1 < date2

//
// In range
//
class DateRange(override val start: MyDate, override val endInclusive: MyDate) : ClosedRange<MyDate>, Iterable<MyDate> {

    override operator fun contains(value: MyDate) = start <= value && value <= endInclusive

    override fun iterator(): Iterator<MyDate> = DateIterator(this)
}

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in DateRange(first, last)
}

//
// rangeTo
//
operator fun MyDate.rangeTo(other: MyDate) = DateRange(this, other)

//class DateRange2(override val start: MyDate, override val endInclusive: MyDate): ClosedRange<MyDate>

fun checkInRange2(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in first..last
}

//
// for loop
//
class DateIterator(val dateRange:DateRange) : Iterator<MyDate> {
    var current: MyDate = dateRange.start
    override fun next(): MyDate {
        val result = current
        current = current.nextDay()
        return result
    }
    override fun hasNext(): Boolean = current <= dateRange.endInclusive
}

//
// operator overloading
//
operator fun MyDate.plus(timeInterval: TimeInterval): MyDate = addTimeIntervals(timeInterval,1)

fun task1(today: MyDate): MyDate {
    return today + TimeInterval.YEAR + TimeInterval.WEEK
}

class RepeatedTimeInterval(val timeInterval: TimeInterval, val number: Int)

operator fun TimeInterval.times(number: Int) = RepeatedTimeInterval(this, number)

operator fun MyDate.plus(timeIntervals: RepeatedTimeInterval) = addTimeIntervals(timeIntervals.timeInterval, timeIntervals.number)

fun task2(today: MyDate): MyDate {
    return today + TimeInterval.YEAR * 2 + TimeInterval.WEEK * 3 + TimeInterval.DAY * 5
}

//
// destructuring declarations
//
fun isLeapDay(date: MyDate): Boolean {
    val (year, month, dayOfMonth) = date
    // 29 February of a leap year
    return year % 4 == 0 && month == 2 && dayOfMonth == 29
}

//
// Invoke
//
class Invokable {
    private var numberOfInvocations: Int = 0
    operator fun invoke(): Invokable {
        numberOfInvocations++
        return this
    }

    override fun toString() = numberOfInvocations.toString()
}

fun invokeTwice(invokable: Invokable) = invokable()()()()()