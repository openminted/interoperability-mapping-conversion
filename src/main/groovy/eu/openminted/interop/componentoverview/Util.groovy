package eu.openminted.interop.componentoverview;

import java.text.BreakIterator

public class Util
{
    static def shortName(name) {
        if (name.contains('.')) {
            return name.tokenize('.')[-1];
        }
        return name;
    }
        
    static def shortDesc(description) {
        if (description) {
            BreakIterator tokenizer = BreakIterator.getSentenceInstance(Locale.US);
            tokenizer.setText(description);
            def start = tokenizer.first();
            def end = tokenizer.next();
            if (start > -1 && end > -1) {
                description = description.substring(start, end);
            }
            description = description
                // Remove HTML tags in tables
                .replaceAll(/<.+?>/, '')
                // Make sure the text doesn't cluse the passthrough block
                .replaceAll(']', '{endsb}')
                .trim();
        }
        return description ? "pass:[${description}]" : '__No description__';
    }
}
