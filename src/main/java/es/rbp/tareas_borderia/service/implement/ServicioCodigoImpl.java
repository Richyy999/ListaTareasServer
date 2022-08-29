package es.rbp.tareas_borderia.service.implement;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.rbp.tareas_borderia.entidad.bbdd.Codigo;
import es.rbp.tareas_borderia.repositorio.RepositorioCodigo;
import es.rbp.tareas_borderia.service.ServicioCodigo;

@Service
public class ServicioCodigoImpl implements ServicioCodigo {

	@Autowired
	private RepositorioCodigo repo;

	@Override
	public List<Codigo> findAll() {
		return repo.findAll();
	}

	@Override
	public Codigo findByTipoAndCodigo(String tipo, int codigo) {
		return repo.findByCodigoAndTipoCodigo(codigo, tipo);
	}

	@Override
	public boolean crearCodigo(Codigo codigo) {
		if (codigo.getId() > 0)
			return false;

		Codigo codigoBBDD = repo.findByCodigoAndTipoCodigo(codigo.getCodigo(), codigo.getTipoCodigo());
		if (codigoBBDD != null)
			return false;

		String lenCodigo = String.valueOf(codigo.getCodigo());
		if (lenCodigo.strip().length() != 4)
			return false;

		return repo.save(codigo) != null;
	}

	@Override
	public boolean modificarCodigo(Codigo codigo) {
		long id = codigo.getId();
		Optional<Codigo> optional = repo.findById(id);
		if (optional.isEmpty())
			return false;

		Codigo codigoBBDD = optional.get();
		codigoBBDD.setCodigo(codigo.getCodigo());
		codigoBBDD.setTipoCodigo(codigo.getTipoCodigo());

		return repo.save(codigoBBDD) != null;
	}

	@Override
	public boolean eliminarCodigo(Codigo codigo) {
		long id = codigo.getId();
		Optional<Codigo> optional = repo.findById(id);
		if (optional.isEmpty())
			return false;

		repo.delete(optional.get());
		return true;
	}
}
