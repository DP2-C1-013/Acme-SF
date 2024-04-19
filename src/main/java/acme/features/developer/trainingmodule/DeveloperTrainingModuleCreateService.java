
package acme.features.developer.trainingmodule;

import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.trainingmodule.TrainingModule;
import acme.roles.Developer;

@Service
public class DeveloperTrainingModuleCreateService extends AbstractService<Developer, TrainingModule> {

}
