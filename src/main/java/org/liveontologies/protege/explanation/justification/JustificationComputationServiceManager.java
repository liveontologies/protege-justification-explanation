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

import java.util.ArrayList;
import java.util.Collection;

import org.liveontologies.protege.explanation.justification.service.JustificationComputationPlugin;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationPluginLoader;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationService;
import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;

/**
 * Keeps track of the available {@link JustificationComputationService} plugins.
 * 
 * @author Pavel Klinov pavel.klinov@uni-ulm.de
 * 
 * @author Yevgeny Kazakov
 */
public class JustificationComputationServiceManager implements Disposable {

	private static final String KEY_ = "org.liveontologies.protege.explanation.justification.services";

	private final OWLEditorKit kit_;

	private final Collection<JustificationComputationService> services_;

	private JustificationComputationService defaultService_;

	private JustificationComputationServiceManager(OWLEditorKit kit)
			throws Exception {
		kit_ = kit;
		services_ = new ArrayList<JustificationComputationService>();
		JustificationComputationPluginLoader loader = new JustificationComputationPluginLoader(
				kit_);
		for (JustificationComputationPlugin plugin : loader.getPlugins()) {
			JustificationComputationService service = plugin.newInstance();
			service.initialise();
			services_.add(service);
		}
	}

	public static synchronized JustificationComputationServiceManager get(
			OWLEditorKit kit) throws Exception {
		JustificationComputationServiceManager manager = kit.getModelManager()
				.get(KEY_);
		if (manager == null) {
			manager = new JustificationComputationServiceManager(kit);
			kit.getModelManager().put(KEY_, manager);
		}
		return manager;
	}

	@Override
	public void dispose() throws Exception {
		for (JustificationComputationService service : services_) {
			service.dispose();
		}
	}

	public OWLEditorKit getOwlEditorKit() {
		return kit_;
	}

	public Collection<JustificationComputationService> getServices() {
		return services_;
	}

	public JustificationComputationService getDefaultService() {
		return defaultService_;
	}

	public void setDefaultService(JustificationComputationService service) {
		this.defaultService_ = service;
	}

}