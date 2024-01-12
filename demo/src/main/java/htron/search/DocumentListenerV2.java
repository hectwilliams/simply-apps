package htron.search;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class DocumentListenerV2 implements DocumentListener {
    
    DebounceLast<Object> debounce = null; // debounce textField "onchange" updates 
    StringBuilder s =  new StringBuilder(); 

    public DocumentListenerV2 ( SearchResults searchResults)  {

        this.debounce = new DebounceLast<>( 300, (Object obj ) -> {
                String ss = (String) obj; 
                if (ss.length() > 0) { 
                    searchResults.searchtree(ss);
                }
            }
        );

    }

    @Override
    public void changedUpdate(DocumentEvent event) {
        this.updateHanlder(event);
    } 

    @Override 
    public void insertUpdate(DocumentEvent event ) {
        this.updateHanlder(event);
    }

    @Override 
    public void removeUpdate(DocumentEvent event) {
        this.updateHanlder(event);
    }
    
    private void updateHanlder(DocumentEvent event) {
        this.s.delete(0, this.s.length());

        try {
            
            s.append(event.getDocument().getText(0,event.getDocument().getLength()) );
            
            this.debounce.call( s.toString() ); 

        } catch (BadLocationException  e) {
            
            e.printStackTrace();

        }

    }
               
}

