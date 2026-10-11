package com.steg.smartmonitoring.common.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Résultat d'une importation Excel
 */
public class ExcelImportResult {

    private int totalRows;
    private int importedRows;
    private int skippedRows;
    private int errorRows;
    private List<String> errors;
    private String message;

    public ExcelImportResult() {
    }

    public ExcelImportResult(int totalRows, int importedRows, int skippedRows, int errorRows, List<String> errors, String message) {
        this.totalRows = totalRows;
        this.importedRows = importedRows;
        this.skippedRows = skippedRows;
        this.errorRows = errorRows;
        this.errors = errors;
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int totalRows;
        private int importedRows;
        private int skippedRows;
        private int errorRows;
        private List<String> errors;
        private String message;

        public Builder totalRows(int totalRows) {
            this.totalRows = totalRows;
            return this;
        }

        public Builder importedRows(int importedRows) {
            this.importedRows = importedRows;
            return this;
        }

        public Builder skippedRows(int skippedRows) {
            this.skippedRows = skippedRows;
            return this;
        }

        public Builder errorRows(int errorRows) {
            this.errorRows = errorRows;
            return this;
        }

        public Builder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ExcelImportResult build() {
            return new ExcelImportResult(totalRows, importedRows, skippedRows, errorRows, errors, message);
        }
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getImportedRows() {
        return importedRows;
    }

    public void setImportedRows(int importedRows) {
        this.importedRows = importedRows;
    }

    public int getSkippedRows() {
        return skippedRows;
    }

    public void setSkippedRows(int skippedRows) {
        this.skippedRows = skippedRows;
    }

    public int getErrorRows() {
        return errorRows;
    }

    public void setErrorRows(int errorRows) {
        this.errorRows = errorRows;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ExcelImportResult success(int imported, int skipped, int total) {
        return ExcelImportResult.builder()
                .totalRows(total)
                .importedRows(imported)
                .skippedRows(skipped)
                .errorRows(0)
                .errors(new ArrayList<>())
                .message(String.format("Import successful: %d imported, %d skipped", imported, skipped))
                .build();
    }

    public static ExcelImportResult withErrors(int imported, int skipped, int errors, List<String> errorList) {
        return ExcelImportResult.builder()
                .importedRows(imported)
                .skippedRows(skipped)
                .errorRows(errors)
                .errors(errorList)
                .message(String.format("Import completed with errors: %d imported, %d skipped, %d errors", imported, skipped, errors))
                .build();
    }

    public void addError(String error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }
}
