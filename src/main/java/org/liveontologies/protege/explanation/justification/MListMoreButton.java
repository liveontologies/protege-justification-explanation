package org.liveontologies.protege.explanation.justification;

/*-
 * #%L
 * Protege Proof-Based Explanation
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2014 - 2016 Live Ontologies Project
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import org.protege.editor.core.ui.list.MListButton;

public class MListMoreButton extends MListButton {

	public static String NAME = "Display more justifications";

	public MListMoreButton(ActionListener actionListener) {
		super(NAME, Color.GREEN.darker(), actionListener);
	}

	@Override
	public void paintButtonContent(Graphics2D g) {
		Rectangle bounds = getBounds();
		int w = bounds.width;
		int h = bounds.height;
		int x = bounds.x + w / 2;
		int y = bounds.y + h / 2;
		g.drawLine(x, y, x, y);
		x = x - w / 4;
		g.drawLine(x, y, x, y);
		x = x + w / 2;
		g.drawLine(x, y, x, y);
	}

}
