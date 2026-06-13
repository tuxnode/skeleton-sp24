package ngrams;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import edu.princeton.cs.algs4.In;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private final Map<String, TimeSeries> wordsMap;
    private final TimeSeries totalCounts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordsMap = new HashMap<>();
        totalCounts = new TimeSeries();
        In wordsIn = new In(wordsFilename);
        while (!wordsIn.isEmpty()) {
            String line = wordsIn.readLine();
            String[] tokens = line.split("\t");

            String word = tokens[0];
            int year = Integer.parseInt(tokens[1]);
            double count = Double.parseDouble(tokens[2]);

            // 如果是新单词
            if (wordsMap != null && !wordsMap.containsKey(word)) {
                wordsMap.put(word, new TimeSeries());
            }
            wordsMap.get(word).put(year, count);
        }

        In countsIn = new In(countsFilename);
        while (!countsIn.isEmpty()) {
            String line = countsIn.readLine();
            String[] tokens = line.split(",");

            int year = Integer.parseInt(tokens[0]);
            double totalCount = Double.parseDouble(tokens[1]);

            if (this.totalCounts != null) {
                this.totalCounts.put(year, totalCount);
            }
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordsMap.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries originalTimeSeries = wordsMap.get(word);

        return new TimeSeries(originalTimeSeries, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordsMap.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries original = wordsMap.get(word);

        return new TimeSeries(original, original.firstKey(), original.lastKey());
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        if (totalCounts.isEmpty()) {
            return new TimeSeries();
        }

        return new TimeSeries(totalCounts, totalCounts.firstKey(), totalCounts.lastKey());
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     * 计算权重
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (wordsMap.isEmpty()) {
            return new TimeSeries();
        }

        TimeSeries wordCount = countHistory(word, startYear, endYear);

        return wordCount.dividedBy(totalCounts);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (wordsMap.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries wordCount = countHistory(word);

        return wordCount.dividedBy(totalCounts);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries sum = new TimeSeries();

        for (String word : words) {
            TimeSeries wordWeight = weightHistory(word, startYear, endYear);

            sum = sum.plus(wordWeight);
        }
        return sum;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries sum = new TimeSeries();

        for (String word : words) {
            sum = sum.plus(weightHistory(word));
        }

        return sum;
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
