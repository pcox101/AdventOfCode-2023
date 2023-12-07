package org.example;

import java.util.*;

import static java.util.Map.entry;

public class Hand implements Comparable<Hand>
{
    static final Map<Character, Integer> CardRanks = Map.ofEntries(
            entry('A',1),
            entry('K',2),
            entry('Q',3),
            entry('J',4),
            entry('T',5),
            entry('9',6),
            entry('8',7),
            entry('7',8),
            entry('6',9),
            entry('5',10),
            entry('4',11),
            entry('3',12),
            entry('2',13));

    public Hand(String hand, int score) {
        for (int i = 0; i < 5; i++) {
            HandList.add(hand.charAt(i));
        }
        Score = score;
    }

    public List<Character> HandList = new ArrayList<>();
    public Integer Score;

    @Override
    public int compareTo(org.example.Hand o) {
        List<Integer> thisNumberOfCards = getNumberOfCards();
        List<Integer> otherNumberOfCards = o.getNumberOfCards();

        // If the number of sets of cards in our list is less
        // than the number in the other set, we win
        if (thisNumberOfCards.size() > otherNumberOfCards.size())
        {
            return -1;
        }
        // And vice versa
        else if (otherNumberOfCards.size() > thisNumberOfCards.size())
        {
            return 1;
        }

        // Same number, now we just compare in order
        for (Integer i = 0; i < thisNumberOfCards.size(); i++)
        {
            if (thisNumberOfCards.get(i) > otherNumberOfCards.get(i))
            {
                return 1;
            }
            else if (otherNumberOfCards.get(i) > thisNumberOfCards.get(i))
            {
                return -1;
            }
        }

        // Must be the same set
        return directCardMapping(o);
    }

    public Integer directCardMapping(Hand o)
    {
        for (int i = 0; i < 5; i++)
        {
            Character thisCard = HandList.get(i);
            Character otherCard = o.HandList.get(i);
            if (!thisCard.equals(otherCard))
            {
                return CardRanks.get(otherCard) - CardRanks.get(thisCard);
            }
        }
        return 0;
    }

    public List<Integer> getNumberOfCards()
    {
        Map<Character, Integer> mapOfCards = new HashMap<>();
        for (Character ch: HandList)
        {
            if (mapOfCards.containsKey(ch))
            {
                mapOfCards.put(ch, mapOfCards.get(ch) + 1);
            }
            else
            {
                mapOfCards.put(ch, 1);
            }
        }

        List<Integer> numberOfCards = new ArrayList<>();
        for (Map.Entry<Character, Integer> ent: mapOfCards.entrySet())
        {
            numberOfCards.add(ent.getValue());
        }
        Collections.sort(numberOfCards, Collections.reverseOrder());

        return numberOfCards;
    }
}
