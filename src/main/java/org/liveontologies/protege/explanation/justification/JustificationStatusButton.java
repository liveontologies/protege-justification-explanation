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


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionListener;

import org.protege.editor.core.ui.list.MListButton;

public class JustificationStatusButton extends MListButton {

	protected JustificationStatusButton(ActionListener actionListener) {
		super("", Color.BLUE.brighter(), actionListener);
	}

	@Override
	public void paintButtonContent(Graphics2D g) {
		float sizeX = getBounds().width;
		float sizeY = getBounds().height;

		float xCenter = getBounds().x + sizeX / 2;
		float yCenter = getBounds().y + sizeY / 2;

		float height = sizeY * 2 / 3;
		float width = sizeX * 2 / 3;

		Stroke stroke = g.getStroke();
		g.setStroke(new BasicStroke(Math.min(width, height) / 7));

		g.drawArc((int) (xCenter - width / 2), (int) (yCenter - height / 2),
				(int) (width), (int) (height), 0, -270);
		float trWidth = width / 3;
		float trHeight = height / 2;
		float trX = xCenter;
		float trY = yCenter - height / 2 - trHeight / 2;
		g.drawPolygon(
				new int[] { (int) (trX), (int) (trX), (int) (trX + trWidth) },
				new int[] { (int) (trY), (int) (trY + trHeight),
						(int) (trY + trHeight / 2) },
				3);
		g.fillPolygon(
				new int[] { (int) (trX), (int) (trX), (int) (trX + trWidth) },
				new int[] { (int) (trY), (int) (trY + trHeight),
						(int) (trY + trHeight / 2) },
				3);
		g.setStroke(stroke);
	}
}