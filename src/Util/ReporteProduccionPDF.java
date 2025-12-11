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
}
