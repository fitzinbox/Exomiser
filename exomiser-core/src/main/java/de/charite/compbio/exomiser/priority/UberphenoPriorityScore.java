package de.charite.compbio.exomiser.priority;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter Variants on the basis of Uberpheno semantic similarity measure between
 * the HPO clinical phenotypes associated with the disease being sequenced and
 * MP annotated MGI mouse models and/or Zebrafish phenotypes.
 *
 * @author Sebastian Koehler
 * @version 0.02 (April 2, 2013).
 */
public class UberphenoPriorityScore implements PriorityScore {

    private double uberphenoScore;

    /**
     * @param uberphenoSemSimScore
     */
    public UberphenoPriorityScore(double uberphenoSemSimScore) {
        this.uberphenoScore = uberphenoSemSimScore;
    }

    /* (non-Javadoc)
     * @see exomizer.priority.Priority#getScore
     */
    @Override
    public float getScore() {
        return (float) uberphenoScore;
    }

    /* (non-Javadoc)
     * @see exomizer.filter.Triage#getHTMLCode()
     */
    @Override
    public String getHTMLCode() {
        return "";
    }

    @Override
    public void setScore(float newscore) { /* not implemented */ }

    /**
     * @return A list with detailed results of filtering. Not yet implemented
     * for gene wanderer.
     */
    @Override
    public List<String> getFilterResultList() {
        return new ArrayList<>();
    }

}