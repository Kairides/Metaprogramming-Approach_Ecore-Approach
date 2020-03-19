package rules;

import metaprogramming.extensionpoint.IRule;
import metaprogramming.extensionpoint.Message;
import metaprogramming.extensionpoint.Severity;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gemoc.dsl.Dsl;
import org.eclipse.gemoc.dsl.Entry;

public class EcoreRule implements IRule{ 
	
	public EcoreRule() {}
	
	/*
	 * The method checks for the presence of an "ecore" entry in the dsl file.
	 * (non-Javadoc)
	 * @see metaprogramming.extensionpoint.IRule#execute(org.eclipse.gemoc.dsl.Dsl)
	 */
	@Override
	public Message execute(Dsl dsl) {
		
		ArrayList<String> entriesNames = new ArrayList<String>();
		
		for (Entry e : dsl.getEntries()) {
			entriesNames.add(e.getKey());
		}
		
		if(!entriesNames.contains("ecore")) {
			return (new Message("Missing entry \"ecore\"", Severity.ERROR));
		}
			
		return (new Message("",Severity.DEFAULT));
		
	}
	
	
	/*
	 * This method checks if the adress given in the "ecore" entry in the dsl file points to an existing file,
	 * the method then checks if the file contains both an ecore model and ecore classes.
	 * (non-Javadoc)
	 * @see metaprogramming.extensionpoint.IRule#execute(org.eclipse.gemoc.dsl.Entry)
	 */
	@Override
	public Message execute(Entry entry) {
		if("ecore".matches(entry.getKey())) {
			
			URI uri = URI.createURI(entry.getValue());
			
			if(!uri.isPlatformResource()) {
				return (new Message("File for \"ecore\" entry not in the workspace", Severity.ERROR));
			}
						
			ResourceSet rs = new ResourceSetImpl();
			Resource res;
			
			try {
				
				res = rs.getResource(uri, true);
				List<EObject> test = res.getContents().get(0).eContents();
				
				ArrayList<String> featuresNames = new ArrayList<>();
				for (EObject e : test) {
					featuresNames.add(e.eContainingFeature().getName());
				}
				
				if(!featuresNames.contains("eAnnotations")) {
					return new Message("The ecore file does not contain an ecore model", Severity.WARNING);
				}
				
				if(!featuresNames.contains("eClassifiers")) {
					return new Message("No class found in the ecore file.", Severity.WARNING);
				}
				
			}catch (RuntimeException e) {
				return (new Message("The file for the \"ecore\" entry does not exist", Severity.ERROR));
			}
			
		}
		return (new Message("",Severity.DEFAULT));
	}
}
