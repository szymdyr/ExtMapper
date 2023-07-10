package com.smp.extmapper.internals.writers

import com.squareup.kotlinpoet.FileSpec
import java.io.OutputStream

internal interface FileWriter {
    fun write(file: OutputStream, fileSpec: FileSpec)
}

internal class FileWriterImpl: FileWriter {
    override fun write(file: OutputStream, fileSpec: FileSpec) =
        file.writer()
            .use { fileSpec.writeTo(it) }

}