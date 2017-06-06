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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import org.protege.editor.core.ui.list.MListButton;

public class LoadJustificationsButton extends MListButton {

	public LoadJustificationsButton(ActionListener actionListener) {
		super("Show next", Color.BLUE.brighter(), actionListener);
	}

	@Override
	public void paintButtonContent(Graphics2D g) {
		int size = getBounds().height;
		int thickness = (Math.round(size / 8.0f) / 2) * 2;

		int x = getBounds().x;
		int y = getBounds().y;

		int insetX = size / 4;
		int insetY = size / 4;
		int insetHeight = size / 2;
		int insetWidth = size / 2;
		g.fillRect(x + size / 2 - thickness / 2, y + insetY, thickness,
				insetHeight);
		g.fillRect(x + insetX, y + size / 2 - thickness / 2, insetWidth,
				thickness);
	}
}