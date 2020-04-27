package ar.nex.equipo.util;

import ar.nex.entity.equipo.Equipo;
import ar.nex.entity.equipo.gasto.Gasoil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
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
public class ExportToExel {

    private final DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");

    Workbook workbook = new XSSFWorkbook();

    public void exportGasoilTransporte(List<Gasoil> list) {
        try {
            if (list != null) {

                Sheet sheet = workbook.createSheet("Gasoil");
                String[] columns = {"Equipo", "Fecha", "KMS", "Litros", "Promedio"};

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
                double kmsAterior = 0.0;
                for (Gasoil item : list) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(item.getEquipo().getPatente());
                    row.createCell(1).setCellValue(fd.format(item.getFecha()));
                    row.createCell(2).setCellValue(item.getKms());
                    row.createCell(3).setCellValue(item.getLitros());
                    if (rowNum == 2) {
                        row.createCell(4).setCellValue(0.0);
                        kmsAterior = item.getKms();
                    } else {
                        row.createCell(4).setCellValue((item.getKms() - kmsAterior) / item.getLitros());
                        kmsAterior = item.getKms();
                    }
                }

                // Resize all columns to fit the content size
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                new ExportUtil().save(workbook, "Detalle Gasoil " + list.get(0).getEquipo().getPatente());
                workbook.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void exportEquipo(List<Equipo> list, String filtro) {

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
