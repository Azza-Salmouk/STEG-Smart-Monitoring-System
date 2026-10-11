package com.steg.smartmonitoring.poste.excel;

import com.steg.smartmonitoring.common.excel.ExcelImportResult;
import com.steg.smartmonitoring.poste.entity.EtatPoste;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.poste.entity.TypePoste;
import com.steg.smartmonitoring.poste.repository.PosteRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour importer les postes depuis Excel
 */
@Service
@Transactional
public class PosteExcelImporter {

    private static final Logger log = LoggerFactory.getLogger(PosteExcelImporter.class);

    private final PosteRepository posteRepository;

    public PosteExcelImporter(PosteRepository posteRepository) {
        this.posteRepository = posteRepository;
    }

    public ExcelImportResult importPostes(MultipartFile file) throws IOException {
        log.info("Starting poste import from file: {}", file.getOriginalFilename());

        List<String> errors = new ArrayList<>();
        int imported = 0;
        int skipped = 0;
        int rowNum = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                rowNum = row.getRowNum() + 1;

                // Skip header row
                if (rowNum == 1) continue;

                try {
                    String code = getCellValue(row, 0);
                    String nom = getCellValue(row, 1);
                    String region = getCellValue(row, 2);
                    String latStr = getCellValue(row, 3);
                    String lonStr = getCellValue(row, 4);
                    String etat = getCellValue(row, 5);
                    String typePoste = getCellValue(row, 6);

                    // Validation
                    if (code == null || code.trim().isEmpty()) {
                        errors.add("Row " + rowNum + ": Code is required");
                        continue;
                    }

                    if (nom == null || nom.trim().isEmpty()) {
                        errors.add("Row " + rowNum + ": Nom is required");
                        continue;
                    }

                    // Check for duplicates
                    if (posteRepository.findByCode(code).isPresent()) {
                        log.debug("Poste {} already exists, skipping", code);
                        skipped++;
                        continue;
                    }

                    Double latitude = null;
                    Double longitude = null;

                    try {
                        if (latStr != null && !latStr.trim().isEmpty()) {
                            latitude = Double.parseDouble(latStr);
                        }
                        if (lonStr != null && !lonStr.trim().isEmpty()) {
                            longitude = Double.parseDouble(lonStr);
                        }
                    } catch (NumberFormatException e) {
                        errors.add("Row " + rowNum + ": Invalid latitude or longitude format");
                        continue;
                    }

                    Poste poste = Poste.builder()
                            .code(code.trim())
                            .nom(nom.trim())
                            .region(region != null ? region.trim() : "")
                            .adresse("")
                            .latitude(latitude != null ? latitude : 0.0)
                            .longitude(longitude != null ? longitude : 0.0)
                            .etat(convertToEtatPoste(etat))
                            .typePoste(convertToTypePoste(typePoste))
                            .build();

                    posteRepository.save(poste);
                    imported++;
                    log.debug("Imported poste: {}", code);

                } catch (Exception e) {
                    errors.add("Row " + rowNum + ": " + e.getMessage());
                    log.error("Error importing row {}: {}", rowNum, e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Error reading Excel file: {}", e.getMessage());
            errors.add("Error reading file: " + e.getMessage());
        }

        log.info("Poste import completed: imported={}, skipped={}, errors={}", imported, skipped, errors.size());

        return ExcelImportResult.withErrors(imported, skipped, errors.size(), errors);
    }

    private String getCellValue(Row row, int columnIndex) {
        if (row == null) return null;
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return null;
    }

    private EtatPoste convertToEtatPoste(String value) {
        if (value == null || value.trim().isEmpty()) {
            return EtatPoste.ACTIF;
        }
        try {
            return EtatPoste.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return EtatPoste.ACTIF;
        }
    }

    private TypePoste convertToTypePoste(String value) {
        if (value == null || value.trim().isEmpty()) {
            return TypePoste.DISTRIBUTION;
        }
        try {
            return TypePoste.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return TypePoste.DISTRIBUTION;
        }
    }
}
