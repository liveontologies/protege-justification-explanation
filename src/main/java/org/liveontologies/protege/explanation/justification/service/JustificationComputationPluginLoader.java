package org.liveontologies.protege.explanation.justification.service;

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

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractPluginLoader;
import org.protege.editor.owl.OWLEditorKit;

/**
 * Load the available specified {@link JustificationComputationService} plugins
 * 
 * @author Alexander Stupnikov Date: 08-02-2017
 */

public class JustificationComputationPluginLoader
		extends AbstractPluginLoader<JustificationComputationPlugin> {

	private static final String KEY_ = "org.liveontologies.protege.explanation.justification";
	private static final String ID_ = "JustificationComputationService";

	private final OWLEditorKit kit_;

	/**
	 * Constructs JustificationComputationPluginLoader
	 * 
	 * @param kit
	 *            OWLEditorKit which is necessary to instantiate a
	 *            JustificationComputationPlugin
	 */
	public JustificationComputationPluginLoader(OWLEditorKit kit) {
		super(KEY_, ID_);
		kit_ = kit;
	}

	@Override
	protected JustificationComputationPlugin createInstance(
			IExtension extension) {
		return new JustificationComputationPlugin(kit_, extension);
	}
}