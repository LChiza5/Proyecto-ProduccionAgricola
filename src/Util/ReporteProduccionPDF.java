/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import dto.ProduccionDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author ilope
 */

public class ReporteProduccionPDF {

    private static final String RUTA_LOGO = "src/recursos/logo.png"; // Ajusta la ruta si lo usas

    public static void generar(ProduccionDTO dto, File archivo) throws Exception {

        if (dto == null) {
            throw new IllegalArgumentException("Producción no puede ser nula.");
        }
        if (archivo == null) {
            throw new IllegalArgumentException("Archivo destino no puede ser nulo.");
        }

        // Documento tamaño carta A4 con márgenes cómodos
        Document document = new Document(PageSize.A4, 50, 50, 60, 50);
        PdfWriter.getInstance(document, new FileOutputStream(archivo));
        document.open();

        // ========= 1. ENCABEZADO (LOGO + TÍTULO) =========
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1.2f, 3.8f});

        // Logo (si existe)
        try {
            Image logo = Image.getInstance(RUTA_LOGO);
            logo.scaleToFit(70, 70);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerTable.addCell(logoCell);
        } catch (Exception e) {
            // Si no hay logo o hay problema al cargarlo, solo dejamos la celda vacía
            PdfPCell emptyLogoCell = new PdfPCell(new Phrase(""));
            emptyLogoCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyLogoCell);
        }

        // Título
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("Reporte de Producción Agrícola", tituloFont);
        titulo.setAlignment(Element.ALIGN_RIGHT);

        // Subtítulo
        Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        Paragraph subtitulo = new Paragraph("Sistema de Gestión de Producción Agrícola", subtituloFont);
        subtitulo.setAlignment(Element.ALIGN_RIGHT);

        Paragraph tituloCompuesto = new Paragraph();
        tituloCompuesto.add(titulo);
        tituloCompuesto.add(Chunk.NEWLINE);
        tituloCompuesto.add(subtitulo);

        PdfPCell tituloCell = new PdfPCell(tituloCompuesto);
        tituloCell.setBorder(Rectangle.NO_BORDER);
        tituloCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tituloCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headerTable.addCell(tituloCell);

        document.add(headerTable);

        // Línea separadora
        LineSeparator ls = new LineSeparator();
        ls.setLineWidth(1f);
        document.add(new Chunk(ls));
        document.add(Chunk.NEWLINE);

        // ========= 2. INFORMACIÓN GENERAL =========
        Font seccionFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
        Paragraph seccionDatos = new Paragraph("Datos generales de la producción", seccionFont);
        seccionDatos.setSpacingAfter(8f);
        document.add(seccionDatos);

        Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        PdfPTable datosTable = new PdfPTable(2);
        datosTable.setWidthPercentage(100);
        datosTable.setSpacingBefore(5f);
        datosTable.setSpacingAfter(10f);
        datosTable.setWidths(new float[]{2f, 4f});

        // Helper para agregar fila "label: valor"
        agregarFila(datosTable, "ID de Producción:", String.valueOf(dto.getId()), labelFont, valueFont);
        agregarFila(datosTable, "Cultivo:", dto.getIdCultivo(), labelFont, valueFont);
        agregarFila(datosTable, "Fecha de cosecha:", dto.getFecha() != null ? dto.getFecha().toString() : "", labelFont, valueFont);
        agregarFila(datosTable, "Cantidad recolectada en campo:", String.valueOf(dto.getCantProducto()) + " unidades", labelFont, valueFont);
        agregarFila(datosTable, "Producto final (calidad):", String.valueOf(dto.getCalidad()) + " unidades", labelFont, valueFont);
        agregarFila(datosTable, "Destino:", dto.getDestino(), labelFont, valueFont);

        document.add(datosTable);

        // ========= 3. BLOQUE DE PRODUCTIVIDAD =========
        Font seccionProdFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
        Paragraph seccionProd = new Paragraph("Indicador de productividad", seccionProdFont);
        seccionProd.setSpacingBefore(10f);
        seccionProd.setSpacingAfter(8f);
        document.add(seccionProd);

        PdfPTable prodTable = new PdfPTable(1);
        prodTable.setWidthPercentage(100);

        // Celda con fondo suave para destacar
        PdfPCell prodCell = new PdfPCell();
        prodCell.setPadding(10f);
        prodCell.setBackgroundColor(new BaseColor(230, 240, 255)); // Azul muy suave

        Font prodValorFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Phrase pProdValor = new Phrase("Productividad: " + dto.getProductividad() + " %", prodValorFont);

        prodCell.addElement(pProdValor);

        // Explicación corta
        Font prodExpFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        String explicacion = "La productividad representa el porcentaje del producto recolectado que "
                + "resulta apto para la comercialización (producto final de calidad) en relación con la "
                + "cantidad total cosechada en campo.";
        Paragraph pExplicacion = new Paragraph(explicacion, prodExpFont);
        pExplicacion.setSpacingBefore(5f);
        prodCell.addElement(pExplicacion);

        // Clasificación simple de productividad
        String categoria;
        int prod = dto.getProductividad();
        if (prod >= 85) {
            categoria = "Nivel de productividad: ALTA";
        } else if (prod >= 60) {
            categoria = "Nivel de productividad: MEDIA";
        } else {
            categoria = "Nivel de productividad: BAJA";
        }
        Paragraph pCategoria = new Paragraph(categoria, prodExpFont);
        pCategoria.setSpacingBefore(5f);
        prodCell.addElement(pCategoria);

        prodTable.addCell(prodCell);
        document.add(prodTable);

        // ========= 4. PIE DE PÁGINA / METAINFO =========
        document.add(Chunk.NEWLINE);

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Font pieFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.DARK_GRAY);
        Paragraph pie = new Paragraph(
                "Reporte generado automáticamente por el sistema de Gestión de Producción Agrícola. "
                        + "Fecha y hora de generación: " + ahora.format(formatter),
                pieFont
        );
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(20f);
        document.add(pie);

        document.close();
    }

    /**
     * Agrega una fila de "etiqueta : valor" a la tabla de datos.
     */
    private static void agregarFila(PdfPTable tabla,
                                    String label,
                                    String value,
                                    Font labelFont,
                                    Font valueFont) {

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.BOX);
        labelCell.setPadding(6f);
        labelCell.setBackgroundColor(new BaseColor(245, 245, 245)); // gris muy suave
        tabla.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", valueFont));
        valueCell.setBorder(Rectangle.BOX);
        valueCell.setPadding(6f);
        tabla.addCell(valueCell);
    }
    public static void generarListado(java.util.List<ProduccionDTO> producciones, File archivo) throws Exception {

    if (producciones == null || producciones.isEmpty()) {
        throw new IllegalArgumentException("La lista de producciones está vacía.");
    }
    if (archivo == null) {
        throw new IllegalArgumentException("Archivo destino no puede ser nulo.");
    }

    // Usamos A4 horizontal para que la tabla tenga más espacio
    Document document = new Document(PageSize.A4.rotate(), 50, 50, 60, 50);
    PdfWriter.getInstance(document, new FileOutputStream(archivo));
    document.open();

    // ========= 1. ENCABEZADO (MISMO ESTILO QUE REPORTE INDIVIDUAL) =========
    PdfPTable headerTable = new PdfPTable(2);
    headerTable.setWidthPercentage(100);
    headerTable.setWidths(new float[]{1.2f, 3.8f});

    // Logo (si existe)
    try {
        Image logo = Image.getInstance(RUTA_LOGO);
        logo.scaleToFit(70, 70);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerTable.addCell(logoCell);
    } catch (Exception e) {
        PdfPCell emptyLogoCell = new PdfPCell(new Phrase(""));
        emptyLogoCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(emptyLogoCell);
    }

    // Título (mismo estilo, texto adaptado a "general")
    Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    Paragraph titulo = new Paragraph("Reporte General de Producciones Agrícolas", tituloFont);
    titulo.setAlignment(Element.ALIGN_RIGHT);

    Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    Paragraph subtitulo = new Paragraph("Sistema de Gestión de Producción Agrícola", subtituloFont);
    subtitulo.setAlignment(Element.ALIGN_RIGHT);

    Paragraph tituloCompuesto = new Paragraph();
    tituloCompuesto.add(titulo);
    tituloCompuesto.add(Chunk.NEWLINE);
    tituloCompuesto.add(subtitulo);

    PdfPCell tituloCell = new PdfPCell(tituloCompuesto);
    tituloCell.setBorder(Rectangle.NO_BORDER);
    tituloCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    tituloCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    headerTable.addCell(tituloCell);

    document.add(headerTable);

    // Línea separadora (igual que en el reporte individual)
    LineSeparator ls = new LineSeparator();
    ls.setLineWidth(1f);
    document.add(new Chunk(ls));
    document.add(Chunk.NEWLINE);

    // ========= 2. TABLA DE PRODUCCIONES (MISMA GAMA DE COLORES) =========
    Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    Font cellFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

    PdfPTable tabla = new PdfPTable(7); // ID, Cultivo, Fecha, Cant., Calidad, Destino, Prod
    tabla.setWidthPercentage(100);
    tabla.setSpacingBefore(5f);
    tabla.setSpacingAfter(10f);
    tabla.setWidths(new float[]{1f, 2f, 2f, 2f, 2f, 2f, 2f});

    // Cabeceras (usa el mismo helper agregarHeaderTabla)
    agregarHeaderTabla(tabla, "ID", headerFont);
    agregarHeaderTabla(tabla, "Cultivo", headerFont);
    agregarHeaderTabla(tabla, "Fecha", headerFont);
    agregarHeaderTabla(tabla, "Cant. campo", headerFont);
    agregarHeaderTabla(tabla, "Calidad", headerFont);
    agregarHeaderTabla(tabla, "Destino", headerFont);
    agregarHeaderTabla(tabla, "Productividad (%)", headerFont);

    int totalRegistros = 0;
    int sumaProductividad = 0;

    for (ProduccionDTO dto : producciones) {
        totalRegistros++;

        tabla.addCell(crearCeldaDato(String.valueOf(dto.getId()), cellFont));
        tabla.addCell(crearCeldaDato(dto.getIdCultivo(), cellFont));
        tabla.addCell(crearCeldaDato(dto.getFecha() != null ? dto.getFecha().toString() : "", cellFont));
        tabla.addCell(crearCeldaDato(String.valueOf(dto.getCantProducto()), cellFont));
        tabla.addCell(crearCeldaDato(String.valueOf(dto.getCalidad()), cellFont));
        tabla.addCell(crearCeldaDato(dto.getDestino(), cellFont));
        tabla.addCell(crearCeldaDato(String.valueOf(dto.getProductividad()), cellFont));

        sumaProductividad += dto.getProductividad();
    }

    document.add(tabla);

    // ========= 3. RESUMEN ESTADÍSTICO (MISMOS COLORES / TIPOS) =========
    double promedioProd = totalRegistros > 0 ? (double) sumaProductividad / totalRegistros : 0.0;

    Font seccionFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
    Paragraph resumenTitulo = new Paragraph("Resumen del listado", seccionFont);
    resumenTitulo.setSpacingBefore(10f);
    resumenTitulo.setSpacingAfter(5f);
    document.add(resumenTitulo);

    Font resumenFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    Paragraph resumenTexto = new Paragraph(
            "Total de registros incluidos en el reporte: " + totalRegistros
                    + "\nProductividad promedio del conjunto: " + String.format("%.2f", promedioProd) + " %",
            resumenFont
    );
    resumenTexto.setSpacingAfter(10f);
    document.add(resumenTexto);

    // ========= 4. PIE (MISMO ESTILO QUE REPORTE INDIVIDUAL) =========
    java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
    java.time.format.DateTimeFormatter formatter =
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    Font pieFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.DARK_GRAY);
    Paragraph pie = new Paragraph(
            "Reporte general generado automáticamente por el sistema de Gestión de Producción Agrícola. "
                    + "Fecha y hora de generación: " + ahora.format(formatter),
            pieFont
    );
    pie.setAlignment(Element.ALIGN_CENTER);
    pie.setSpacingBefore(20f);
    document.add(pie);

    document.close();
}
    private static void agregarHeaderTabla(PdfPTable tabla, String texto, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(texto, font));
    cell.setBackgroundColor(new BaseColor(60, 120, 200)); 
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPadding(5f);
    tabla.addCell(cell);
}

private static PdfPCell crearCeldaDato(String texto, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(texto != null ? texto : "", font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setPadding(4f);
    return cell;
}
}
