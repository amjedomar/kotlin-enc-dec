package encryptdecrypt

import java.io.File
import java.io.IOException

fun shiftLetter(l: Char, step: Int): Char {
    val start = if (step >= 0) {
        if (l.isUpperCase()) 'A' else 'a'
    } else {
        if (l.isUpperCase()) 'Z' else 'z'
    }

    return start + ((l.code + step - start.code) % 26)
}

fun enc(alg: String, msg: String, key: Int): String =
    msg.map {
        if (alg == "shift")
            if (it.isLetter()) shiftLetter(it, key) else it
        else it + key
    }.joinToString("")

fun dec(alg: String, msg: String, key: Int): String =
    msg.map {
        if (alg == "shift")
            if (it.isLetter()) shiftLetter(it, key * -1) else it
        else it - key
    }.joinToString("")

fun main(args: Array<String>) {
    var alg = "shift"
    var mode = "enc"
    var key = 0
    var data = ""
    var out: String? = null

    for (i in args.indices) {
        when (args[i]) {
            "-alg" -> alg = args[i + 1]
            "-mode" -> mode = args[i + 1]
            "-key" -> key = args[i + 1].toInt()
            "-data" -> data = args[i + 1]
            "-in" -> if (!args.contains("-data")) {
                val file = File(args[i + 1])
                try {
                    data = file.readText()
                } catch (e: IOException) {
                    return println("Error: input file path is invalid or file not exist")
                }
            }

            "-out" -> out = args[i + 1]
        }
    }

    val result = when (mode) {
        "enc" -> enc(alg, data, key)
        "dec" -> dec(alg, data, key)
        else -> null
    }

    if (result != null) {
        if (out != null) {
            val file = File(out)
            try {
                file.writeText(result)
            } catch (e: IOException) {
                println("Error: out file path is invalid or file not exist")
            }
        } else {
            println(result)
        }
    }
}
