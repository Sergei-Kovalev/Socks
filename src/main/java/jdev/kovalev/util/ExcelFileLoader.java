package jdev.kovalev.util;

import jdev.kovalev.dto.request.RequestDto;
import jdev.kovalev.exception.LoadFileDataException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelFileLoader {

    public List<RequestDto> loadExcelFile(String filePath){
        List<RequestDto> requestDtoList = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(filePath)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                RequestDto requestDto = new RequestDto();
                requestDto.setColor(row.getCell(0).getStringCellValue());
                requestDto.setCottonPercentage((int)row.getCell(1).getNumericCellValue());
                requestDto.setNumber((int)row.getCell(2).getNumericCellValue());
                requestDtoList.add(requestDto);
            }
        } catch (IOException e) {
            throw new LoadFileDataException();
        }
        return requestDtoList;
    }
}
