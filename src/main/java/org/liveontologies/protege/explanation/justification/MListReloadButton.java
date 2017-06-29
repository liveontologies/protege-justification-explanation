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
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

import org.protege.editor.core.ui.list.MListButton;

public class MListReloadButton extends MListButton {

	private static final int ANGLE_START_ = 10, ANGLE_EXTENT_ = 330;

	private final GeneralPath arrowTip_ = new GeneralPath(
			GeneralPath.WIND_EVEN_ODD, 3);
	private final Arc2D arcBorder_ = new Arc2D.Float();

	protected MListReloadButton(ActionListener actionListener) {
		super("Reload", Color.BLUE.brighter(), actionListener);
	}

	@Override
	public void paintButtonContent(Graphics2D g) {

		float sizeX = getBounds().width;
		float sizeY = getBounds().height;

		float height = sizeY / 2;
		float width = sizeX / 2;

		float xCenter = getBounds().x + height;
		float yCenter = getBounds().y + width;

		int quarterSize = (Math.round(width / 4.0f) / 2) * 2;

		float radius = (Math.min(width, height) - quarterSize) / 2;

		double radians = Math.toRadians(ANGLE_START_);
		// the tip of the arrow
		double tipX = xCenter + (radius + quarterSize) * Math.cos(radians);
		double tipY = yCenter - radius * Math.sin(radians);
		float tipWidth = 3 * radius / 2;
		float tipHeight = 3 * radius / 2;

		arrowTip_.reset();
		arrowTip_.moveTo(tipX, tipY);
		arrowTip_.lineTo(tipX - tipWidth, tipY);
		arrowTip_.lineTo(tipX, tipY - tipHeight);
		arrowTip_.closePath();
		g.fill(arrowTip_);
		arcBorder_.setArcByCenter(xCenter, yCenter, radius + quarterSize,
				ANGLE_START_, ANGLE_EXTENT_, Arc2D.PIE);
		Area area = new Area(arcBorder_);
		arcBorder_.setArcByCenter(xCenter, yCenter, radius, ANGLE_START_,
				ANGLE_EXTENT_, Arc2D.PIE);
		area.subtract(new Area(arcBorder_));
		g.fill(area);
	}

}