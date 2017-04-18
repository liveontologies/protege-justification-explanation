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
import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.protege.editor.core.editorkit.EditorKit;
import org.protege.editor.core.plugin.AbstractProtegePlugin;
import org.protege.editor.core.ui.preferences.PreferencesPanel;
import org.protege.editor.core.ui.preferences.PreferencesPanelPlugin;
import org.protege.editor.owl.OWLEditorKit;

public class JustificationPreferencesPanelPlugin extends AbstractProtegePlugin<PreferencesPanel>
		implements PreferencesPanelPlugin {
	private final EditorKit kit;

	public static final String LABEL_PARAM = "label";
	
	public JustificationPreferencesPanelPlugin(EditorKit kit, IExtension extension) {
		super(extension);
		this.kit = kit;
	}
	
    public String getLabel() {
        return getPluginProperty(LABEL_PARAM);
    }

	@Override
	public PreferencesPanel newInstance()
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		PreferencesPanel panel = super.newInstance();
		panel.setup(getLabel(), kit);
		return panel;
	}
}