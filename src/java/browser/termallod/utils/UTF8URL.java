/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;
import java.awt.* ;
import java.awt.event.* ;
/**
 *
 * @author elahi
 */
public class UTF8URL
extends Frame 
implements TextListener, WindowListener
{
  private TextField label;			// Output field
  private TextArea textarea;			// Input field
  private TextArea echo;			// Output field

  public UTF8URL () {
    super("UTF-8/URL encoding demo");
    LayoutManager layout = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();

    setLayout(layout);
    addWindowListener(this);

    textarea = new TextArea("", 4, 55, TextArea.SCROLLBARS_VERTICAL_ONLY);
    label = new TextField();
    label.setEditable(false);
    echo = new TextArea("", 4, 55, TextArea.SCROLLBARS_VERTICAL_ONLY);
    echo.setEditable(false);

    textarea.addTextListener(this);

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1.0;
    constraints.weighty = 0.0;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    add(new Label("Type here:", Label.LEFT), constraints);

    constraints.gridy = 1;
    constraints.weighty = 1.0;
    constraints.fill = GridBagConstraints.BOTH;
    add(textarea, constraints);

    constraints.gridy = 2;
    constraints.weighty = 0.0;
    add(new Label("Encoded:", Label.LEFT), constraints);

    constraints.gridy = 3;
    constraints.weighty = 0.0;
    add(label, constraints);

    constraints.gridy = 4;
    constraints.weighty = 0.0;
    add(new Label("Decoded (should be same):", Label.LEFT), constraints);

    constraints.gridy = 5;
    constraints.weighty = 1.0;
    add(echo, constraints);

    pack();
    setVisible(true);
    textarea.requestFocus();
  }

  private void recompute()
  {
    String input_text = textarea.getText();
    String encoded_text = URLUTF8Encoder.encode(input_text);
    label.setText(encoded_text);
    echo.setText(unescape(encoded_text));
    // doLayout();
  }

  public static String unescape(String s) {
    StringBuffer sbuf = new StringBuffer () ;
    int l  = s.length() ;
    int ch = -1 ;
    int b, sumb = 0;
    for (int i = 0, more = -1 ; i < l ; i++) {
      /* Get next byte b from URL segment s */
      switch (ch = s.charAt(i)) {
	case '%':
	  ch = s.charAt (++i) ;
	  int hb = (Character.isDigit ((char) ch) 
		    ? ch - '0'
		    : 10+Character.toLowerCase((char) ch) - 'a') & 0xF ;
	  ch = s.charAt (++i) ;
	  int lb = (Character.isDigit ((char) ch) 
		    ? ch - '0'
		    : 10+Character.toLowerCase((char) ch) - 'a') & 0xF ;
	  b = (hb << 4) | lb ;
	  break ;
	case '+':
	  b = ' ' ;
	  break ;
	default:
	  b = ch ;
      }
      /* Decode byte b as UTF-8, sumb collects incomplete chars */
      if ((b & 0xc0) == 0x80) {			// 10xxxxxx (continuation byte)
	sumb = (sumb << 6) | (b & 0x3f) ;	// Add 6 bits to sumb
	if (--more == 0) sbuf.append((char) sumb) ; // Add char to sbuf
      } else if ((b & 0x80) == 0x00) {		// 0xxxxxxx (yields 7 bits)
	sbuf.append((char) b) ;			// Store in sbuf
      } else if ((b & 0xe0) == 0xc0) {		// 110xxxxx (yields 5 bits)
	sumb = b & 0x1f;
	more = 1;				// Expect 1 more byte
      } else if ((b & 0xf0) == 0xe0) {		// 1110xxxx (yields 4 bits)
	sumb = b & 0x0f;
	more = 2;				// Expect 2 more bytes
      } else if ((b & 0xf8) == 0xf0) {		// 11110xxx (yields 3 bits)
	sumb = b & 0x07;
	more = 3;				// Expect 3 more bytes
      } else if ((b & 0xfc) == 0xf8) {		// 111110xx (yields 2 bits)
	sumb = b & 0x03;
	more = 4;				// Expect 4 more bytes
      } else /*if ((b & 0xfe) == 0xfc)*/ {	// 1111110x (yields 1 bit)
	sumb = b & 0x01;
	more = 5;				// Expect 5 more bytes
      }
      /* No need to test if the UTF-8 encoding is well-formed */
    }
    return sbuf.toString() ;
  }

  /* WindowListener interface */

  public void windowClosing(WindowEvent e) {System.exit(0);}
  public void windowOpened(WindowEvent e) {}
  public void windowClosed(WindowEvent e) {}
  public void windowIconified(WindowEvent e) {}
  public void windowDeiconified(WindowEvent e) {}
  public void windowActivated(WindowEvent e) {}
  public void windowDeactivated(WindowEvent e) {}


  /* TextListener interface */
  public void textValueChanged(TextEvent te) {recompute();}

  /* Main body */
  public static void main (String[] args) {
    UTF8URL udemo = new UTF8URL();
  }
}
