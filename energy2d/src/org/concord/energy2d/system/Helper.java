/*
 *   Copyright (C) 2011  The Concord Consortium, Inc.,
 *   25 Love Lane, Concord, MA 01742
 *
 */

package org.concord.energy2d.system;

import java.awt.Desktop;
import java.awt.Frame;
import java.net.URI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author Charles Xie
 * 
 */
public class Helper {

	private static ScriptDialog scriptDialog;

	private Helper() {
	}

	public final static void showScriptDialog(System2D box) {
		if (scriptDialog != null && scriptDialog.isShowing()) {
			scriptDialog.toFront();
		} else {
			scriptDialog = new ScriptDialog(box);
			scriptDialog.pack();
			scriptDialog.setLocationRelativeTo(box.view);
			scriptDialog.setVisible(true);
		}
	}

	public final static void openBrowser(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String os = System.getProperty("os.name");
			try {
				if (os.startsWith("Windows")) {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				} else if (os.startsWith("Mac OS")) {
					Runtime.getRuntime().exec(new String[] { "open", url });
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public final static void showKeyboardShortcuts(Frame frame) {
		String s = "<html><h2>Keyboard Shortcuts</h2><hr>";
		s += "<br><font face=Courier>'R'</font> &mdash; Run or pause the simulation.";
		s += "<br><font face=Courier>'T'</font> &mdash; Reset the simulation.";
		s += "<br><font face=Courier>'L'</font> &mdash; Reload the initial configurations.";
		s += "<br><font face=Courier>'G'</font> &mdash; Show or hide the graph.";
		s += "<br><font face=Courier>'DELETE'</font> &mdash; Erase data in the graph when it is being shown.";
		s += "<br><font face=Courier>'S'</font> &mdash; Turn sunlight on or off.";
		s += "<br><font face=Courier>'Q'</font> &mdash; When sunlight is present, increase the sun angle (towards west).";
		s += "<br><font face=Courier>'W'</font> &mdash; When sunlight is present, decrease the sun angle (towards east).";
		s += "</html>";
		JOptionPane.showMessageDialog(frame, new JLabel(s), "Keyboard Shortcuts", JOptionPane.INFORMATION_MESSAGE);
	}

	public final static void showAbout(Frame frame) {
		String s = "<html><h2>" + System2D.BRAND_NAME + "</h2>";
		s += "<h4><i>Interactive Heat Transfer Simulations for Everyone</i></h4>";
		s += "http://energy.concord.org/energy2d<br>The Educational Modeling Laboratory, The Concord Consortium, 2011-2014";
		s += "<hr>";
		s += "<h4>Attribution & Acknowledgement:</h4>This program is created by Dr. Charles Xie. Funding is provided by the National Science Foundation<br>under grants 0918449, 1124281, and 1304485 for which Dr. Xie serves as the Principal Investigator.";
		s += "<h4>License:</h4>GNU Lesser General Public License V3.0";
		s += "</html>";
		JOptionPane.showMessageDialog(frame, new JLabel(s), "About Energy2D", JOptionPane.INFORMATION_MESSAGE);
	}

}
