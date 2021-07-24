package io.github.theriverelder.exception

class LoadFailedException(val loadObject: String) : Exception("载入 $loadObject 失败！") {
}