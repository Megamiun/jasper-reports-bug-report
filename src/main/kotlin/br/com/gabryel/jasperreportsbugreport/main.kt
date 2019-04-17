package br.com.gabryel.jasperreportsbugreport

import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import java.io.File

fun main() {
    File("out").mkdir()

    buildPDF(getItems(17)).write("firstPage")
    buildPDF(getItems(18)).write("error")
    buildPDF(getItems(19)).write("secondPage")
}

private fun ByteArray.write(value: String) {
    File("out/$value.pdf").writeBytes(this)
}

private fun buildPDF(values: List<ValueHolder>): ByteArray {

    val parameters = mutableMapOf("set" to JRBeanCollectionDataSource(values) as Any)

    val stream = ValueHolder::class.java.getResourceAsStream("/test_A4.jrxml")

    val jasperReport = JasperCompileManager.compileReport(stream)

    val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, JREmptyDataSource())

    return JasperExportManager.exportReportToPdf(jasperPrint)
}

private fun getItems(items: Int): List<ValueHolder> {
    return generateSequence(1, Int::inc)
        .map { ValueHolder("Lorem Ipsum - $it") }
        .take(items)
        .toList()
}

class ValueHolder(val value: String)