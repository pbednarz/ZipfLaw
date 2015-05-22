/**
 * Created by pbednarz on 22/05/15.
 */

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ZipfLawUtils {
    public static List<Multiset.Entry> readFile(File file) throws IOException {
        Multiset<String> multiset = HashMultiset.create();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        TokenStream stream = analyzer.tokenStream("content", new FileReader(file));

        CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
        stream.reset();
        while (stream.incrementToken()) {
            multiset.add(cattr.toString());
        }
        stream.end();
        stream.close();
        //Sorting the multiset by number of occurrences using a comparator on the Entries of the multiset
        List<Multiset.Entry> list = new ArrayList(multiset.entrySet());
        Comparator<Multiset.Entry> occurence_comparator = new Comparator<Multiset.Entry>() {
            public int compare(Multiset.Entry e1, Multiset.Entry e2) {
                return e2.getCount() - e1.getCount();
            }
        };
        Collections.sort(list, occurence_comparator);

        int rank = 1;
        for (Multiset.Entry e : list) {
            System.out.println(e.getElement() + "-> " + rank + " - " + e.getCount());
            rank++;
        }

        return list;
    }
}