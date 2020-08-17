package com.scdf.elk;

import org.eclipse.elk.alg.layered.options.LayeredMetaDataProvider;
import org.eclipse.elk.alg.layered.options.LayeredOptions;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.data.LayoutMetaDataService;
import org.eclipse.elk.core.math.ElkPadding;
import org.eclipse.elk.core.util.BasicProgressMonitor;
import org.eclipse.elk.graph.ElkNode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElkController {
	
	private RecursiveGraphLayoutEngine layoutEngine = new RecursiveGraphLayoutEngine();
	
	{
		LayoutMetaDataService.getInstance().registerLayoutMetaDataProviders(new LayeredMetaDataProvider());
	}
	
	@CrossOrigin
	@RequestMapping(path="/elk", consumes = "application/json", produces = "application/json")
	public ElkNode layout(@RequestBody ElkNode graph) {
		layoutEngine.layout(graph, new BasicProgressMonitor());
		
		// Clear out properties as they seem to be internal to the layout without any useful info for the resultant graph
//		ElkUtil.applyVisitors(graph, element -> {
//			element.getProperties().clear();
//		});
		
		graph.setProperty(LayeredOptions.PADDING, new ElkPadding(30, 30, 30, 30));
		return graph;
	}

}
