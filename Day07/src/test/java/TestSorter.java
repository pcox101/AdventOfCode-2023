import org.example.Hand;
import org.example.HandWithJoker;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestSorter {
    @Test
    public void ValidateFiveOfAKind()
    {
        Hand hand = new Hand("KKKKK",1);
        Hand otherHand1 = new Hand("KKKKQ",1);
        Hand otherHand2 = new Hand("KKKQQ",1);
        Hand otherHand3 = new Hand("KKQQQ",1);
        Hand otherHand4 = new Hand("KQQQQ",1);

        assertEquals(hand.compareTo(otherHand1) > 0, true);
        assertEquals(hand.compareTo(otherHand2) > 0, true);
        assertEquals(hand.compareTo(otherHand3) > 0, true);
        assertEquals(hand.compareTo(otherHand4) > 0, true);
    }

    @Test
    public void ValidateDirectRank()
    {
        Hand hand = new Hand("KQJT9",1);
        Hand otherHand = new Hand("QTJ98",1);

        Integer value = hand.compareTo(otherHand);
        assertEquals(value > 0, true);
    }

    @Test
    public void ValidateInputSet() {
        Hand hand1 = new Hand("32T3K", 765);
        Hand hand2 = new Hand("T55J5", 684);
        Hand hand3 = new Hand("KK677", 28);
        Hand hand4 = new Hand("KTJJT", 220);
        Hand hand5 = new Hand("QQQJA", 483);

        // 32T3K loses to everything
        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand1.compareTo(hand3) < 0);
        assertTrue(hand1.compareTo(hand4) < 0);
        assertTrue(hand1.compareTo(hand5) < 0);

        // KTJJT loses to all but hand 1
        assertTrue(hand4.compareTo(hand2) < 0);
        assertTrue(hand4.compareTo(hand3) < 0);
        assertTrue(hand4.compareTo(hand5) < 0);

        // KK677 loses to all but hand 1 and hand 4
        assertEquals(hand3.compareTo(hand2) < 0, true);
        assertEquals(hand3.compareTo(hand5) < 0, true);

        // T55J5 loses to all but hand 1, hand 3, hand 4 (i.e. hand 5)
        assertEquals(hand2.compareTo(hand5) < 0, true);

    }

    @Test
    public void ValidateInputSetWithJokers() {
        HandWithJoker hand1 = new HandWithJoker("32T3K", 765);
        HandWithJoker hand2 = new HandWithJoker("T55J5", 684);
        HandWithJoker hand3 = new HandWithJoker("KK677", 28);
        HandWithJoker hand4 = new HandWithJoker("KTJJT", 220);
        HandWithJoker hand5 = new HandWithJoker("QQQJA", 483);

        // 32T3K loses to everything
        assertTrue(hand1.compareTo(hand2) < 0);
        assertTrue(hand1.compareTo(hand3) < 0);
        assertTrue(hand1.compareTo(hand4) < 0);
        assertTrue(hand1.compareTo(hand5) < 0);

        // KK677 loses to all but hand 1
        assertTrue(hand3.compareTo(hand2) < 0);
        assertTrue(hand3.compareTo(hand4) < 0);
        assertTrue(hand3.compareTo(hand5) < 0);

        // T55J5 loses to all but hand 1 and hand 3
        assertTrue(hand2.compareTo(hand4) < 0);
        assertTrue(hand2.compareTo(hand5) < 0);

        // QQQJA loses to all but hand 1, hand 3, hand 4 (i.e. hand 5)
        assertTrue(hand5.compareTo(hand4) < 0);

    }

    @Test
    public void SpecificCase1()
    {
        HandWithJoker hand1 = new HandWithJoker("32T3K", 1);
        HandWithJoker hand2 = new HandWithJoker("J345A", 2);

        assertTrue(hand2.compareTo(hand1) < 0);
    }
}
