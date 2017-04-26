package org.liveontologies.protege.explanation.justification.preferences;

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
import org.protege.editor.core.editorkit.EditorKit;
import org.protege.editor.core.plugin.AbstractPluginLoader;

public class JustificationPreferencesPanelPluginLoader
		extends AbstractPluginLoader<JustificationPreferencesPanelPlugin> {

	private final EditorKit kit_;

	private static final String ID = "JustificationPreferences";
	private static final String KEY = "org.liveontologies.protege.explanation.justification";

	public JustificationPreferencesPanelPluginLoader(EditorKit kit) {
		super(KEY, ID);
		kit_ = kit;
	}

	@Override
	protected JustificationPreferencesPanelPlugin createInstance(
			IExtension extension) {
		return new JustificationPreferencesPanelPlugin(kit_, extension);
	}
}