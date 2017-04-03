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

	private final OWLEditorKit kit;

	private final Collection<ComputationService> services;
	private Map<ComputationService, String> serviceIds;
	private ComputationService selectedService = null;
	public static String lastChoosenServiceId = null;

	public JustificationComputationServiceManager(OWLEditorKit kit, String KEY, String ID) throws Exception {
		this.kit = kit;
		this.services = new ArrayList<ComputationService>();
		this.serviceIds = new HashMap<ComputationService, String>();
		JustificationComputationPluginLoader loader = new JustificationComputationPluginLoader(this.kit, KEY, ID);
		for (JustificationComputationPlugin plugin : loader.getPlugins()) {
			ComputationService service = plugin.newInstance();
			service.initialise();
			services.add(service);
			serviceIds.put(service, plugin.getIExtension().getUniqueIdentifier());
		}
	}

	@Override
	public void dispose() throws Exception {
		for (ComputationService service : services) {
			service.dispose();
		}
	}

	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}

	public Collection<ComputationService> getServices() {
		return services;
	}
	
	public ComputationService getSelectedService() {
		return selectedService;
	}
	
	public void selectService(ComputationService service) {
		selectedService = service;
		lastChoosenServiceId = getIdForService(service);
	}
	
	public String getIdForService(ComputationService service)
	{
		return serviceIds.get(service);
	}
}