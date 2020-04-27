package ar.nex.equipo.util;

import ar.nex.entity.equipo.Equipo;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Renzo
 */
public class EquipoToExel {

    Workbook workbook = new XSSFWorkbook();

    public void export(List<Equipo> list, String filtro) {

        try {
            Sheet sheet = workbook.createSheet("Equipos");
            String[] columns = {"Equipo ID", "Categoria", "Tipo", "Modelo", "Marca", "Año",
                "Chisis", "Motor", "Patente", "Empresa"};

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Create a Row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Create Other rows and cells with data
            int rowNum = 1;
            for (Equipo item : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getIdEquipo());
                row.createCell(1).setCellValue(item.getCategoria().getNombre());
                row.createCell(2).setCellValue(item.getTipo().getNombre());
                row.createCell(3).setCellValue(item.getModelo().getNombre());
                row.createCell(4).setCellValue(item.getMarca().getNombre());
                row.createCell(5).setCellValue(item.getAnio());
                row.createCell(6).setCellValue(item.getChasis());
                row.createCell(7).setCellValue(item.getMotor());
                row.createCell(8).setCellValue(item.getPatente());
                row.createCell(9).setCellValue(item.getEmpresa().getNombre());
            }

            // Resize all columns to fit the content size
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            new ExportUtil().save(workbook, "Lista Equipos - Empresa " + filtro);

            workbook.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
