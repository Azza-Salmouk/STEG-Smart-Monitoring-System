package com.steg.smartmonitoring.transformateur.excel;

import com.steg.smartmonitoring.common.exception.ResourceNotFoundException;
import com.steg.smartmonitoring.common.excel.ExcelImportResult;
import com.steg.smartmonitoring.poste.entity.Poste;
import com.steg.smartmonitoring.poste.repository.PosteRepository;
import com.steg.smartmonitoring.transformateur.entity.RiskLevel;
import com.steg.smartmonitoring.transformateur.entity.StatusTransformateur;
import com.steg.smartmonitoring.transformateur.entity.Transformateur;
import com.steg.smartmonitoring.transformateur.repository.TransformateurRepository;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour importer les transformateurs depuis Excel
 */
@Service
@Transactional
public class TransformateurExcelImporter {

    private static final Logger log = LoggerFactory.getLogger(TransformateurExcelImporter.class);

    private final TransformateurRepository transformateurRepository;
    private final PosteRepository posteRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TransformateurExcelImporter(TransformateurRepository transformateurRepository, PosteRepository posteRepository) {
        this.transformateurRepository = transformateurRepository;
        this.posteRepository = posteRepository;
    }

    public ExcelImportResult importTransformateurs(MultipartFile file) throws IOException {
        log.info("Starting transformateur import from file: {}", file.getOriginalFilename());

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
                    String reference = getCellValue(row, 0);
                    String serialNumber = getCellValue(row, 1);
                    String manufacturer = getCellValue(row, 2);
                    String powerStr = getCellValue(row, 3);
                    String installationDateStr = getCellValue(row, 4);
                    String posteCode = getCellValue(row, 5);
                    String status = getCellValue(row, 6);
                    String healthScoreStr = getCellValue(row, 7);
                    String riskLevel = getCellValue(row, 8);

                    // Validation
                    if (reference == null || reference.trim().isEmpty()) {
                        errors.add("Row " + rowNum + ": Reference is required");
                        continue;
                    }

                    // Check for duplicates
                    if (transformateurRepository.findByReference(reference).isPresent()) {
                        log.debug("Transformateur {} already exists, skipping", reference);
                        skipped++;
                        continue;
                    }

                    // Find Poste
                    Poste poste = null;
                    if (posteCode != null && !posteCode.trim().isEmpty()) {
                        poste = posteRepository.findByCode(posteCode.trim())
                                .orElseThrow(() -> new ResourceNotFoundException("Poste not found: " + posteCode));
                    } else {
                        errors.add("Row " + rowNum + ": Poste code is required");
                        continue;
                    }

                    // Parse power
                    Double power = null;
                    try {
                        if (powerStr != null && !powerStr.trim().isEmpty()) {
                            power = Double.parseDouble(powerStr);
                        }
                    } catch (NumberFormatException e) {
                        errors.add("Row " + rowNum + ": Invalid power format");
                        continue;
                    }

                    // Parse installation date
                    LocalDate installationDate = null;
                    try {
                        if (installationDateStr != null && !installationDateStr.trim().isEmpty()) {
                            installationDate = LocalDate.parse(installationDateStr.trim(), DATE_FORMATTER);
                        } else {
                            installationDate = LocalDate.now();
                        }
                    } catch (Exception e) {
                        errors.add("Row " + rowNum + ": Invalid date format (use yyyy-MM-dd)");
                        continue;
                    }

                    // Parse health score
                    Integer healthScore = 50;
                    try {
                        if (healthScoreStr != null && !healthScoreStr.trim().isEmpty()) {
                            healthScore = Integer.parseInt(healthScoreStr.trim());
                        }
                    } catch (NumberFormatException e) {
                        errors.add("Row " + rowNum + ": Invalid health score");
                        continue;
                    }

                    StatusTransformateur statusEnum = StatusTransformateur.ACTIF;
                    if (status != null) {
                        try {
                            statusEnum = StatusTransformateur.valueOf(status.trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            log.debug("Invalid status, using ACTIF: {}", status);
                        }
                    }

                    RiskLevel riskEnum = RiskLevel.LOW;
                    if (riskLevel != null) {
                        try {
                            riskEnum = RiskLevel.valueOf(riskLevel.trim().toUpperCase());
                        } catch (IllegalArgumentException e) {
                            log.debug("Invalid risk level, using LOW: {}", riskLevel);
                        }
                    }

                    Transformateur transformateur = Transformateur.builder()
                            .reference(reference.trim())
                            .serialNumber(serialNumber != null ? serialNumber.trim() : "SN-" + System.nanoTime())
                            .manufacturer(manufacturer != null ? manufacturer.trim() : "Unknown")
                            .model("Imported")
                            .power(power != null ? power : 1000.0)
                            .installationDate(installationDate)
                            .status(statusEnum)
                            .healthScore(healthScore)
                            .riskLevel(riskEnum)
                            .description("Imported from Excel")
                            .poste(poste)
                            .build();

                    transformateurRepository.save(transformateur);
                    imported++;
                    log.debug("Imported transformateur: {}", reference);

                } catch (ResourceNotFoundException e) {
                    errors.add("Row " + rowNum + ": " + e.getMessage());
                    log.error("Poste not found: {}", e.getMessage());
                } catch (Exception e) {
                    errors.add("Row " + rowNum + ": " + e.getMessage());
                    log.error("Error importing row {}: {}", rowNum, e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("Error reading Excel file: {}", e.getMessage());
            errors.add("Error reading file: " + e.getMessage());
        }

        log.info("Transformateur import completed: imported={}, skipped={}, errors={}", imported, skipped, errors.size());

        return ExcelImportResult.withErrors(imported, skipped, errors.size(), errors);
    }

    private String getCellValue(Row row, int columnIndex) {
        if (row == null) return null;
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue()).split("\\.")[0];
        }
        return null;
    }
}
