package htron.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListenerV2 implements ActionListener {
    
    SearchTextArea textbox;
    SearchResults searchResults;

    public ActionListenerV2(SearchTextArea textbox, SearchResults searchResults) {
        this.textbox = textbox;
        this.searchResults = searchResults;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.textbox.field.setText("");    
        this.searchResults.clearResults();
    }
}
