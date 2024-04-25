
package acme.features.authenticated.sponsor;

import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.services.AbstractService;
import acme.roles.Sponsor;

@Service
public class AuthenticatedSponsorUpdateService extends AbstractService<Authenticated, Sponsor> {

}
