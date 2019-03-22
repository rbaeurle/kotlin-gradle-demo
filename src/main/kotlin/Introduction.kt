import java.util.*

/**
 * https://kotlinlang.org/docs/reference/lambdas.html
 * https://play.kotlinlang.org/koans/Introduction
 */
fun main(args: Array<String>) {
    println(start())
    println(joinOptions(mutableListOf("A","B","C")))
    println(funcWithDefault("Rolf"))
    println(funcWithDefault("Rolf", 51, true))
    println(containsEven(mutableListOf(1,3,5,7)))
    println(getPattern().toRegex().matches("12 JUN 1967"))
    println(getPeople())
    sendMessageToClient(Client(PersonalInfo("r.b@x.x")), "Test message", object : Mailer {
            override fun sendMessage(email: String, message: String) {
                println( "Send : '" + message + "' to : " + email)
            }

        }
    )
    println(eval(Num(7)))
    println(eval(Sum(Num(4),Num(5))))
    val i = 5
    println(i.r())
    val p: Pair<Int,Int> = Pair(7,3)
    println(p.r())
    println( getList())
    println( getListSAM())
    println( getListExt())
}

// function syntax
fun start(): String = "OK"

// named arguments
fun joinOptions(options: Collection<String>) =
    options.joinToString(prefix = "[", postfix = "]" )

// default arguments
fun funcWithDefault(name: String, number: Int = 42, toUpperCase: Boolean = false) : String {
    return (if (toUpperCase) name.toUpperCase() else name) + ',' + number
}

// lambdas
fun containsEven(collection: Collection<Int>): Boolean =
    // collection.any { it % 2 == 0 }
    collection.any { e -> e % 2 == 0 }

//https://kotlinlang.org/docs/reference/basic-types.html#string-literals
// Strings
val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)"
fun getPattern(): String = """\d{2}\s${month}\s\d{4}"""

// data classes
data class Person (val name: String, val age: Int)
fun getPeople(): List<Person> {
    return listOf(Person("Alice", 29), Person("Bob", 31))
}


// Nullable types
fun sendMessageToClient(
    client: Client?, message: String?, mailer: Mailer
){
    val email = client?.personalInfo?.email
    if (email != null && message != null) {
        mailer.sendMessage(email, message)
    }
}

class Client (val personalInfo: PersonalInfo?)
class PersonalInfo (val email: String?)
interface Mailer {
    fun sendMessage(email: String, message: String)
}

//
// smart cast
//
fun eval(expr: Expr): Int =
    when (expr) {
        is Num -> expr.value
        is Sum -> eval(expr.left) + eval(expr.right)
        else -> throw IllegalArgumentException("Unknown expression")
    }

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

//
// extension functions
//
fun Int.r(): RationalNumber = RationalNumber(this,1)
fun Pair<Int, Int>.r(): RationalNumber = RationalNumber(first,second)

data class RationalNumber(val numerator: Int, val denominator: Int)

//
// object expression
//
fun getList(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    Collections.sort(arrayList, object : Comparator<Int> {
        override fun compare(x: Int, y: Int) = y - x
    })
    return arrayList
}

//
// SAM conversion
//
fun getListSAM(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    Collections.sort(arrayList, { x, y -> y - x })
    return arrayList
}

//
// Extension functions on collections
//
fun getListExt(): List<Int> {
    return arrayListOf(1, 5, 2).sortedDescending()
}