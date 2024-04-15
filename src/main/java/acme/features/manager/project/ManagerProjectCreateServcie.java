
package acme.features.manager.project;

import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.roles.Manager;

@Service
public class ManagerProjectCreateServcie extends AbstractService<Manager, Project> {

}
