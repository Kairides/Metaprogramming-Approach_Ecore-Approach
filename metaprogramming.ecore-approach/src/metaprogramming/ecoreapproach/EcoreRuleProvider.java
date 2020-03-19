package metaprogramming.ecoreapproach;

import java.util.Collection;
import java.util.HashSet;

import metaprogramming.extensionpoint.IRule;
import metaprogramming.extensionpoint.IRuleProvider;
import rules.*;

public class EcoreRuleProvider implements IRuleProvider {
	
	private Collection<IRule> ruleSet = new HashSet<>();
	
	public EcoreRuleProvider() {
		this.ruleSet.add(new EcoreRule());
	}

	@Override
	public Collection<IRule> getValidationRules() {
		return this.ruleSet;
	}

}
