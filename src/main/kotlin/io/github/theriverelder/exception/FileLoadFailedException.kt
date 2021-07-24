package io.github.theriverelder.exception

import java.io.File

class FileLoadFailedException(val file: File) : Exception(file.path) {
}