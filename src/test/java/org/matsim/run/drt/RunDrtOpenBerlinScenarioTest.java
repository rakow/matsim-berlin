package org.matsim.run.drt;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.testcases.MatsimTestUtils;

/**
 * 
 * @author gleich
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunDrtOpenBerlinScenarioTest {
		private static final Logger log = Logger.getLogger( RunDrtOpenBerlinScenarioTest.class ) ;
		
		@Rule public MatsimTestUtils utils = new MatsimTestUtils() ;
	
	// During debug some exceptions only occured at the replanning stage of the 3rd
	// iteration, so we need at least 3 iterations.
	// Have at least 0.1 pct of the population to have as many strange corner cases
	// as possible (because those tend to cause exceptions otherwise not found).
	@Test
	public final void eTest0_1pctUntilIteration3() {
		try {
			final String[] args = new String[0];// = {"./scenarios/berlin-v5.5-1pct/input/berlin-v5.5-1pct-Berlkoenig.config.xml"};
			
			Config config = RunDrtOpenBerlinScenario.prepareConfig( args ) ;
			config.controler().setLastIteration(3);
			config.strategy().setFractionOfIterationsToDisableInnovation(1);
			config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
			config.controler().setOutputDirectory( utils.getOutputDirectory() );
			
			Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario( config ) ;
			// Decrease population to 0.1% sample 
			List<Id<Person>> agentsToRemove = new ArrayList<>();
			for (Id<Person> id: scenario.getPopulation().getPersons().keySet()) {
				if (MatsimRandom.getRandom().nextDouble() < 0.1) {agentsToRemove.add(id);}
			}
			for (Id<Person> id: agentsToRemove) {
				scenario.getPopulation().removePerson(id);
			}
			
			Controler controler = RunDrtOpenBerlinScenario.prepareControler( scenario ) ;
			
			controler.run() ;			
			
			// TODO: test the scores in iteration 0 and 4
		} catch ( Exception ee ) {
			throw new RuntimeException(ee) ;
		}
	}
}