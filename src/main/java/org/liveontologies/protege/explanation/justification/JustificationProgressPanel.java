package org.liveontologies.protege.explanation.justification;

/*-
 * #%L
 * Protege Justification Explanation
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 - 2017 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import org.liveontologies.protege.explanation.justification.service.JustificationComputation.InterruptMonitor;
import org.liveontologies.protege.explanation.justification.service.JustificationListener;
/*
 * Copyright (C) 2009, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge The University of Manchester Information Management
 * Group Date: 14-Oct-2009
 * 
 * @author Yevgeny Kazakov
 */
public class JustificationProgressPanel extends JPanel
		implements JustificationListener, InterruptMonitor, ActionListener {

	private static final long serialVersionUID = 5548156306411811469L;

	private static final String MESSAGE = "Computing justifications. Found ";

	private final JLabel messageLabel_;
	private final Action cancelAction_;
	private final Timer timer_;
	private boolean isInterrupted_ = false;
	private int nFound_ = 0;

	/**
	 * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
	 */
	public JustificationProgressPanel() {
		setLayout(new BorderLayout(12, 12));
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		setPreferredSize(new Dimension(400, 100));
		JPanel progressPanel = new JPanel(new BorderLayout(3, 3));
		add(progressPanel, BorderLayout.NORTH);
		messageLabel_ = new JLabel(MESSAGE + "0  ");
		progressPanel.add(messageLabel_, BorderLayout.NORTH);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressPanel.add(progressBar, BorderLayout.SOUTH);
		cancelAction_ = new AbstractAction("Stop searching") {
			private static final long serialVersionUID = 1784308350971019508L;

			@Override
			public void actionPerformed(ActionEvent e) {
				interrupt();
				setEnabled(false);
			}
		};
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(new JButton(cancelAction_));
		timer_ = new Timer(20, this);
		timer_.start();
	}

	public void reset() {
		nFound_ = 0;
		cancelAction_.setEnabled(true);
	}

	public synchronized int getNoFoundJustifications() {
		return nFound_;
	}

	@Override
	public synchronized void justificationFound(Set<OWLAxiom> justification) {
		nFound_++;
	}

	@Override
	public synchronized boolean isInterrupted() {
		return isInterrupted_;
	}

	private synchronized void interrupt() {
		isInterrupted_ = true;
	}

	public synchronized void clearInterrupt() {
		isInterrupted_ = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		messageLabel_.setText(MESSAGE + nFound_);
		timer_.restart();
	}

}