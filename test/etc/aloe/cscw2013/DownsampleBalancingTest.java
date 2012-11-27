/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etc.aloe.cscw2013;

import etc.aloe.data.Segment;
import etc.aloe.data.SegmentSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class DownsampleBalancingTest {

    public DownsampleBalancingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    private SegmentSet generateTestSegments(int numPositive, int numNegative) {
        List<Segment> segments = new ArrayList<Segment>();
        for (int i = 0; i < numPositive; i++) {
            segments.add(new Segment(true, null));
        }
        for (int i = 0; i < numNegative; i++) {
            segments.add(new Segment(false, null));
        }

        Collections.shuffle(segments);

        SegmentSet segmentSet = new SegmentSet();
        segmentSet.setSegments(segments);
        return segmentSet;
    }

    private List<SegmentSet> generateTestSegments(int numToGenerate) {
        Random random = new Random(24344);

        List<SegmentSet> segmentSets = new ArrayList<SegmentSet>();

        for (int i = 0; i < numToGenerate; i++) {
            int numPositive = random.nextInt(200) + 10;
            int numNegative = random.nextInt(200) + 10;

            segmentSets.add(generateTestSegments(numPositive, numNegative));
        }

        return segmentSets;
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of balance method, of class DownsampleBalancing.
     */
    @Test
    public void testBalance() {
        System.out.println("balance equally");

        List<SegmentSet> segmentSets = generateTestSegments(10);
        for (SegmentSet segmentSet : segmentSets) {
            DownsampleBalancing instance = new DownsampleBalancing(1, 1);

            SegmentSet result = instance.balance(segmentSet);

            int actualTrue = result.getCountWithTrueLabel(true);
            int actualFalse = result.getCountWithTrueLabel(false);

            //Both are more than 0
            assertTrue(actualTrue > 0);
            assertTrue(actualFalse > 0);

            //Should have an equal number of true and false examples
            assertEquals(actualTrue, actualFalse);
        }
    }

    /**
     * Test of balance method, of class DownsampleBalancing.
     */
    @Test
    public void testBalance_withUnlabeled() {
        System.out.println("balance with unlabeled");

        SegmentSet segmentSet = generateTestSegments(20, 110);
        DownsampleBalancing instance = new DownsampleBalancing(1, 1);

        segmentSet.add(new Segment(null, null));
        try {
            SegmentSet result = instance.balance(segmentSet);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test of balance method, of class DownsampleBalancing.
     */
    @Test
    public void testBalance_againstFalsePositive() {
        System.out.println("balance with high false positive cost");

        List<SegmentSet> segmentSets = generateTestSegments(10);
        for (SegmentSet segmentSet : segmentSets) {
            DownsampleBalancing instance = new DownsampleBalancing(2, 1);

            SegmentSet result = instance.balance(segmentSet);

            int actualTrue = result.getCountWithTrueLabel(true);
            int actualFalse = result.getCountWithTrueLabel(false);

            System.out.println("Balanced (" + segmentSet.getCountWithTrueLabel(true) + ", " + segmentSet.getCountWithTrueLabel(false) + ") to (" + actualTrue + ", " + actualFalse + ")");

            //Both are more than 0
            assertTrue(actualTrue > 0);
            assertTrue(actualFalse > 0);

            //The ratio of false/true should be 2:1
            assertEquals(2.0, (double) actualFalse / actualTrue, 0.1);
        }
    }

    /**
     * Test of balance method, of class DownsampleBalancing.
     */
    @Test
    public void testBalance_againstFalseNegative() {
        System.out.println("balance with high false negative cost");

        List<SegmentSet> segmentSets = generateTestSegments(10);
        for (SegmentSet segmentSet : segmentSets) {
            DownsampleBalancing instance = new DownsampleBalancing(1, 2);

            SegmentSet result = instance.balance(segmentSet);

            int actualTrue = result.getCountWithTrueLabel(true);
            int actualFalse = result.getCountWithTrueLabel(false);

            System.out.println("Balanced (" + segmentSet.getCountWithTrueLabel(true) + ", " + segmentSet.getCountWithTrueLabel(false) + ") to (" + actualTrue + ", " + actualFalse + ")");

            //Both are more than 0
            assertTrue(actualTrue > 0);
            assertTrue(actualFalse > 0);

            //The ratio of false/true should be 1:2
            assertEquals(0.5, (double) actualFalse / actualTrue, 0.1);
        }
    }
}
