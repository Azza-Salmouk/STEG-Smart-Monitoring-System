package com.steg.smartmonitoring.poste.service;

/**
 * DTO pour retourner les statistiques des postes
 */
public class PosteStatisticsResponse {
    private Long totalPostes;
    private Long posteActifs;
    private Long posteInactifs;
    private Long posteMaintenance;

    public PosteStatisticsResponse() {
    }

    public PosteStatisticsResponse(Long totalPostes, Long posteActifs, Long posteInactifs, Long posteMaintenance) {
        this.totalPostes = totalPostes;
        this.posteActifs = posteActifs;
        this.posteInactifs = posteInactifs;
        this.posteMaintenance = posteMaintenance;
    }

    public Long getTotalPostes() { return totalPostes; }
    public void setTotalPostes(Long totalPostes) { this.totalPostes = totalPostes; }
    public Long getPosteActifs() { return posteActifs; }
    public void setPosteActifs(Long posteActifs) { this.posteActifs = posteActifs; }
    public Long getPosteInactifs() { return posteInactifs; }
    public void setPosteInactifs(Long posteInactifs) { this.posteInactifs = posteInactifs; }
    public Long getPosteMaintenance() { return posteMaintenance; }
    public void setPosteMaintenance(Long posteMaintenance) { this.posteMaintenance = posteMaintenance; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalPostes;
        private Long posteActifs;
        private Long posteInactifs;
        private Long posteMaintenance;

        public Builder totalPostes(Long totalPostes) { this.totalPostes = totalPostes; return this; }
        public Builder posteActifs(Long posteActifs) { this.posteActifs = posteActifs; return this; }
        public Builder posteInactifs(Long posteInactifs) { this.posteInactifs = posteInactifs; return this; }
        public Builder posteMaintenance(Long posteMaintenance) { this.posteMaintenance = posteMaintenance; return this; }

        public PosteStatisticsResponse build() {
            return new PosteStatisticsResponse(totalPostes, posteActifs, posteInactifs, posteMaintenance);
        }
    }
}
