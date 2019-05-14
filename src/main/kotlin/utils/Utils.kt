package utils

import java.nio.file.InvalidPathException
import java.nio.file.Paths

fun String.fileExists(): Boolean {
    try {
        //TODO this is not working
        Paths.get(this);
    } catch (e: InvalidPathException) {
        println("$this is not a valid path: $e")
        return false
    }
    return true
}