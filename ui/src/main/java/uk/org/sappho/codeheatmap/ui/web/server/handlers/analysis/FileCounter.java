package uk.org.sappho.codeheatmap.ui.web.server.handlers.analysis;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectUnique;
import static ch.lambdaj.Lambda.sort;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Ordering;

public class FileCounter {

    public class Entry {

        private final String file;
        private int count;

        public Entry(String file, int count) {
            this.file = file;
            this.count = count;
        }

        public String getFile() {
            return this.file;
        }

        public int getCount() {
            return this.count;
        }

    }

    private final List<Entry> entries = new ArrayList<Entry>();

    public void addOne(String file) {
        Entry entry = selectUnique(entries, having(on(Entry.class).getFile(), is(file)));
        if (entry == null) {
            entry = new Entry(file, 0);
            entries.add(entry);
        }
        entry.count++;
    }

    public Iterable<Entry> topItems(int num) {
        List<Entry> sorted = sort(entries, on(Entry.class).getCount(), Ordering.natural().reverse());
        return sorted.subList(0, Math.min(num, sorted.size()));
    }
}
