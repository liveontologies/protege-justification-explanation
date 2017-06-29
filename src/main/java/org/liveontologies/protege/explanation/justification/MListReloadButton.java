package org.liveontologies.protege.explanation.justification;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

import org.protege.editor.core.ui.list.MListButton;

public class MListReloadButton extends MListButton {

	private static final int ANGLE_START_ = 10, ANGLE_EXTENT_ = 330;

	private final GeneralPath arrow_ = new GeneralPath(
			GeneralPath.WIND_EVEN_ODD, 6);
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

		arrow_.reset();
		arrow_.moveTo(tipX, tipY);
		arrow_.lineTo(tipX - tipWidth, tipY);
		arrow_.lineTo(tipX - tipWidth + quarterSize, tipY - quarterSize);
		arrow_.lineTo(tipX - quarterSize, tipY - quarterSize);
		arrow_.lineTo(tipX - quarterSize, tipY - tipHeight + quarterSize);
		arrow_.lineTo(tipX, tipY - tipHeight);
		arrow_.closePath();
		g.fill(arrow_);
		arcBorder_.setArcByCenter(xCenter, yCenter, radius + quarterSize,
				ANGLE_START_, ANGLE_EXTENT_, Arc2D.PIE);
		Area area = new Area(arcBorder_);
		arcBorder_.setArcByCenter(xCenter, yCenter, radius, ANGLE_START_,
				ANGLE_EXTENT_, Arc2D.PIE);
		area.subtract(new Area(arcBorder_));
		g.fill(area);
	}

}