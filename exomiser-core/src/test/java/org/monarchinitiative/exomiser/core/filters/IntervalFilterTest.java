/*
 * The Exomiser - A tool to annotate and prioritize genomic variants
 *
 * Copyright (c) 2016-2019 Queen Mary University of London.
 * Copyright (c) 2012-2016 Charité Universitätsmedizin Berlin and Genome Research Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.monarchinitiative.exomiser.core.filters;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.exomiser.core.model.ChromosomalRegion;
import org.monarchinitiative.exomiser.core.model.GeneticInterval;
import org.monarchinitiative.exomiser.core.model.VariantEvaluation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class IntervalFilterTest {

    IntervalFilter instance = new IntervalFilter(SEARCH_INTERVAL);
    
    private static final byte RIGHT_CHR = 7;
    private static final byte WRONG_CHR = 3;
    private static final int START_REGION = 155595590;
    private static final int END_REGION = 155604810;
    
    private static final int INSIDE_REGION = START_REGION + 20;
    private static final int BEFORE_REGION = START_REGION - 20;
    private static final int AFTER_REGION = END_REGION + 20;

    
    private static final GeneticInterval SEARCH_INTERVAL = new GeneticInterval(RIGHT_CHR, START_REGION, END_REGION);

    private final VariantEvaluation rightChromosomeRightPosition = VariantEvaluation.builder(RIGHT_CHR, INSIDE_REGION, "A", "T")
            .build();
    private final VariantEvaluation rightChromosomeWrongPosition = VariantEvaluation.builder(RIGHT_CHR, BEFORE_REGION, "A", "T")
            .build();
    private final VariantEvaluation wrongChromosomeRightPosition = VariantEvaluation.builder(WRONG_CHR, INSIDE_REGION, "A", "T")
            .build();
    private final VariantEvaluation wrongChromosomeWrongPosition = VariantEvaluation.builder(RIGHT_CHR, AFTER_REGION, "A", "T")
            .build();

    @Test
    public void testThatRightChromosomeRightPositionPassesFilter() {
        FilterResult filterResult = instance.runFilter(rightChromosomeRightPosition);
        FilterTestHelper.assertPassed(filterResult);
    }

    @Test
    public void testThatRightChromosomeWrongPositionFailsFilter() {
        FilterResult filterResult = instance.runFilter(rightChromosomeWrongPosition);
        FilterTestHelper.assertFailed(filterResult);
    }

    @Test
    public void testThatWrongChromosomeRightPositionFailsFilter() {
        FilterResult filterResult = instance.runFilter(wrongChromosomeRightPosition);
        FilterTestHelper.assertFailed(filterResult);
    }

    @Test
    public void multipleIntervals() {
        ChromosomalRegion interval1 = new GeneticInterval(1, 20, 30);
        ChromosomalRegion interval2 = new GeneticInterval(1, 25, 40);
        ChromosomalRegion interval3 = new GeneticInterval(3, 50, 60);
        ChromosomalRegion interval4 = new GeneticInterval(3, 51, 55);

        IntervalFilter multiIntervalFilter = new IntervalFilter(ImmutableList.of(interval3, interval2, interval3, interval1, interval4));

        FilterTestHelper.assertFailed(multiIntervalFilter.runFilter(VariantEvaluation.builder(1, 19, "A", "T").build()));
        FilterTestHelper.assertPassed(multiIntervalFilter.runFilter(VariantEvaluation.builder(1, 20, "A", "T").build()));
        FilterTestHelper.assertPassed(multiIntervalFilter.runFilter(VariantEvaluation.builder(1, 27, "A", "T").build()));
        FilterTestHelper.assertPassed(multiIntervalFilter.runFilter(VariantEvaluation.builder(1, 39, "A", "T").build()));
        FilterTestHelper.assertPassed(multiIntervalFilter.runFilter(VariantEvaluation.builder(3, 51, "A", "T").build()));
        FilterTestHelper.assertFailed(multiIntervalFilter.runFilter(VariantEvaluation.builder(3, 61, "A", "T").build()));

        FilterTestHelper.assertFailed(multiIntervalFilter.runFilter(VariantEvaluation.builder(2, 233, "A", "T").build()));

        assertThat(multiIntervalFilter.getChromosomalRegions(), equalTo(ImmutableList.of(interval1, interval2, interval3, interval4)));
        System.out.println(multiIntervalFilter);
    }

    @Test
    public void throwsExceptionWithEmptyInputList() {
        assertThrows(IllegalStateException.class, () -> new IntervalFilter(ImmutableList.of()));
    }

    @Test
    public void testThatWrongChromosomeWrongPositionFailsFilter() {
        FilterResult filterResult = instance.runFilter(wrongChromosomeWrongPosition);
        FilterTestHelper.assertFailed(filterResult);
    }

    @Test
    public void testGetFilterType() {
        assertThat(instance.getFilterType(), equalTo(FilterType.INTERVAL_FILTER));
    }

    @Test
    public void testHashCode() {
        IntervalFilter otherFilter = new IntervalFilter(SEARCH_INTERVAL);
        assertThat(instance.hashCode(), equalTo(otherFilter.hashCode()));
    }

    @Test
    public void testNotEqualsIntervalDifferent() {
        IntervalFilter otherFilter = new IntervalFilter(GeneticInterval.parseString("chr3:12334-67850"));
        assertThat(instance.equals(otherFilter), is(false));
    }

    @Test
    public void testIsEquals() {
        IntervalFilter otherFilter = new IntervalFilter(SEARCH_INTERVAL);
        assertThat(instance.equals(otherFilter), is(true));
    }
    
    @Test
    public void testToString() {
        System.out.println(instance.toString());
    }
    
}
