package es.rbp.tareas_borderia.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.DeudaConfigBBDD;
import es.rbp.tareas_borderia.entidad.bbdd.UsuarioBBDD;
import es.rbp.tareas_borderia.entidad.config.DeudaConfig;
import es.rbp.tareas_borderia.repositorio.RepositorioDeudaConfig;
import es.rbp.tareas_borderia.service.ServicioDeudaConfig;

@Service
public class ServicioDeudaConfigImpl implements ServicioDeudaConfig {

	@Autowired
	private RepositorioDeudaConfig repo;

	@Override
	public DeudaConfigBBDD getUltimaDeudaConfig() {
		List<DeudaConfigBBDD> deudasConfigBBDD = repo.findAll();
		if (deudasConfigBBDD.size() == 0)
			return null;

		return deudasConfigBBDD.get(deudasConfigBBDD.size() - 1);
	}

	@Override
	public boolean crearDeudaConfig(DeudaConfig deudaConfig, UsuarioBBDD usuario) {
		List<DeudaConfigBBDD> deudasConfigBBDD = repo.findAll();
		if (deudasConfigBBDD.size() >= 1)
			return modificarDeudaConfig(deudaConfig, usuario);

		DeudaConfigBBDD deudaConfigBBDD = new DeudaConfigBBDD();
		deudaConfigBBDD.setAcumular(deudaConfig.isAcumular());
		deudaConfigBBDD.setDeudaMax(deudaConfig.getDeudaMax());
		deudaConfigBBDD.setUserMod(usuario.getNombre());

		return repo.save(deudaConfigBBDD) != null;
	}

	@Override
	public boolean modificarDeudaConfig(DeudaConfig deudaConfig, UsuarioBBDD usuario) {
		Optional<DeudaConfigBBDD> optional = repo.findById(deudaConfig.getId());
		if (optional.isEmpty())
			return false;

		boolean actualizada = false;
		DeudaConfigBBDD deudaConfigBBDD = optional.get();
		if (deudaConfigBBDD.isAcumular() != deudaConfig.isAcumular()) {
			deudaConfigBBDD.setAcumular(deudaConfig.isAcumular());
			actualizada = true;
		}

		if (deudaConfig.getDeudaMax() > 0) {
			deudaConfigBBDD.setDeudaMax(deudaConfig.getDeudaMax());
			actualizada = true;
		}

		if (actualizada) {
			deudaConfigBBDD.setUserMod(usuario.getNombre());
			return repo.save(deudaConfigBBDD) != null;
		}

		return false;
	}
}
