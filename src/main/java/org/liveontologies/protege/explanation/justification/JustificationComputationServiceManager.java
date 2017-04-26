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
import java.util.HashMap;
import java.util.Map;

import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationPlugin;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationPluginLoader;
import org.protege.editor.core.Disposable;
import org.protege.editor.owl.OWLEditorKit;

/**
 * Keeps track of the available specified {@link ComputationService} plugins.
 * 
 * @author Pavel Klinov pavel.klinov@uni-ulm.de
 * 
 * @author Yevgeny Kazakov
 */

public class JustificationComputationServiceManager implements Disposable {

	public static String LAST_CHOOSEN_SERVICE_ID = null;
	private static final String KEY_ = "org.liveontologies.protege.explanation.justification.services";

	private final OWLEditorKit kit_;
	private final Collection<ComputationService> services_;
	private final Map<ComputationService, String> serviceIds_;
	private ComputationService selectedService_ = null;

	private JustificationComputationServiceManager(OWLEditorKit kit)
			throws Exception {
		kit_ = kit;
		services_ = new ArrayList<ComputationService>();
		serviceIds_ = new HashMap<ComputationService, String>();
		JustificationComputationPluginLoader loader = new JustificationComputationPluginLoader(
				kit_);
		for (JustificationComputationPlugin plugin : loader.getPlugins()) {
			ComputationService service = plugin.newInstance();
			service.initialise();
			services_.add(service);
			serviceIds_.put(service,
					plugin.getIExtension().getUniqueIdentifier());
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
		for (ComputationService service : services_) {
			service.dispose();
		}
	}

	public OWLEditorKit getOWLEditorKit() {
		return kit_;
	}

	public Collection<ComputationService> getServices() {
		return services_;
	}

	public ComputationService getSelectedService() {
		return selectedService_;
	}

	public void selectService(ComputationService service) {
		selectedService_ = service;
		LAST_CHOOSEN_SERVICE_ID = getIdForService(service);
	}

	public String getIdForService(ComputationService service) {
		return serviceIds_.get(service);
	}
}